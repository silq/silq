package br.ufsc.silq.core.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.ufsc.silq.core.SilqConfig;
import br.ufsc.silq.core.data.AvaliacaoCollectionResult;
import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.data.AvaliacaoStats;
import br.ufsc.silq.core.data.AvaliacaoType;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.exception.SilqError;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.QQualisPeriodico;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.utils.SilqStringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@Transactional(readOnly = true)
public class AvaliacaoService {

	@Inject
	private DataSource dataSource;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private QualisPeriodicoRepository qualisPeriodicoRepository;

	@Inject
	private LattesParser lattesParser;

	/**
	 * Avalia um currículo lattes.
	 *
	 * @param lattes Currículo a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqError Caso haja um erro no parsing ou avaliação do currículo.
	 */
	@Cacheable(cacheNames = "avaliacoes")
	public AvaliacaoResult avaliar(CurriculumLattes lattes, @Valid AvaliarForm avaliarForm) {
		ParseResult parseResult = null;
		try {
			parseResult = this.lattesParser.parseCurriculum(lattes);
		} catch (SilqLattesException e) {
			throw new SilqError(e);
		}
		return this.avaliar(parseResult, avaliarForm);
	}

	/**
	 * Avalia uma coleção de currículos, retornando estatísticas desta coleção.
	 *
	 * @param curriculos Coleção de currículos a serem avaliados.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Resultado de avaliação contendo as estatísticas contabilizadas com base na avaliação dos currículos da coleção.
	 * @throws SilqLattesException Caso haja um erro no parsing ou avaliação de algum currículo da coleção.
	 */
	public AvaliacaoCollectionResult avaliarCollection(Collection<CurriculumLattes> curriculos, @Valid AvaliarForm avaliarForm)
			throws SilqLattesException {
		AvaliacaoStats stats = curriculos.parallelStream()
				.map(curriculo -> this.avaliar(curriculo, avaliarForm))
				.map(result -> result.getStats())
				.reduce((r1, r2) -> r1.reduce(r2))
				.orElse(new AvaliacaoStats());

		return new AvaliacaoCollectionResult(avaliarForm, stats);
	}

	/**
	 * Avalia dados de um currículo Lattes já parseados pelo {@link LattesParser}.
	 *
	 * @param parseResult Resultado do parsing do currículo Lattes de um pesquisador.
	 * @param form Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 */
	public AvaliacaoResult avaliar(ParseResult parseResult, @Valid AvaliarForm form) {
		AvaliacaoResult result = new AvaliacaoResult(form, parseResult.getDadosGerais());

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			TreeSet<Artigo> artigosAvaliados = parseResult.getArtigos().parallelStream()
					.filter(artigo -> form.getPeriodoAvaliacao().inclui(artigo.getAno()))
					.map(artigo -> this.avaliarArtigo(artigo, form))
					.collect(Collectors.toCollection(() -> new TreeSet<>()));

			result.setArtigos(artigosAvaliados);
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			TreeSet<Trabalho> trabalhosAvaliados = parseResult.getTrabalhos().parallelStream()
					.filter(trabalho -> form.getPeriodoAvaliacao().inclui(trabalho.getAno()))
					.map(trabalho -> this.avaliarTrabalho(trabalho, form))
					.collect(Collectors.toCollection(() -> new TreeSet<>()));

			result.setTrabalhos(trabalhosAvaliados);
		}

		return result;
	}

	public Artigo avaliarArtigo(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		if (artigo.getIssn().isEmpty()) {
			// TODO (bonetti): Ordenar por ano aqui também
			return this.avaliarArtigoPorSimilaridade(artigo, avaliarForm);
		} else {
			return this.avaliarArtigoPorIssn(artigo, avaliarForm);
		}
	}

	private Artigo avaliarArtigoPorSimilaridade(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos = new ArrayList<>();
		try {
			conceitos = this.getConceitos(artigo.getTituloVeiculo(), avaliarForm, TipoAvaliacao.PERIODICO);
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
		}
		artigo.addConceitos(conceitos);
		return artigo;
	}

	private Artigo avaliarArtigoPorIssn(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		QQualisPeriodico path = QQualisPeriodico.qualisPeriodico;
		BooleanExpression query = path.issn.eq(artigo.getIssn())
				.and(path.areaAvaliacao.eq(avaliarForm.getArea().toUpperCase()));

		Iterable<QualisPeriodico> results = this.qualisPeriodicoRepository.findAll(query,
				path.ano.subtract(artigo.getAno()).abs().asc());

		List<Conceito> conceitos = new ArrayList<>();
		for (QualisPeriodico result : results) {
			conceitos.add(new Conceito(result.getTitulo(), result.getEstrato(), NivelSimilaridade.TOTAL, result.getAno()));
		}

		artigo.addConceitos(conceitos);
		return artigo;
	}

	public Trabalho avaliarTrabalho(Trabalho trabalho, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos;
		try {
			conceitos = this.getConceitos(trabalho.getTituloVeiculo(), avaliarForm, TipoAvaliacao.EVENTO);
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
		}

		trabalho.addConceitos(conceitos);
		return trabalho;
	}

	/**
	 * Obtém os conceitos de um evento ou periódico realizando uma busca por similaridade na base Qualis.
	 *
	 * @param tituloVeiculo Título do evento ou periódico que deseja-se avaliar.
	 * @param avaliarForm Opções de avaliação.
	 * @param tipoAvaliacao Tipo de avaliação (altera a tabela do banco a ser consultada).
	 * @return A lista de conceitos do veículo.
	 * @throws SQLException Caso haja um erro ao executar o SQL.
	 */
	public List<Conceito> getConceitos(String tituloVeiculo, @Valid AvaliarForm avaliarForm, TipoAvaliacao tipoAvaliacao) throws SQLException {
		this.setSimilarityThreshold(avaliarForm.getNivelSimilaridade().getValue());
		String tituloNormalizado = SilqStringUtils.normalizeString(tituloVeiculo);

		List<Conceito> conceitos = new ArrayList<>();

		Connection connection = this.dataSource.getConnection();
		Statement st = connection.createStatement();
		String sqlStatement = this.createSqlStatement(tipoAvaliacao, tituloNormalizado, avaliarForm.getArea(), SilqConfig.MAX_PARSE_RESULTS);
		ResultSet rs = st.executeQuery(sqlStatement);

		while (rs.next()) {
			conceitos.add(this.createConceito(rs));
		}

		rs.close();
		st.close();
		connection.close();

		return conceitos;
	}

	/**
	 * Seta o nível de similaridade mínimo (threshold) que será usado para as queries de similaridade no banco.
	 *
	 * @param value Valor numérico de 0 a 1 representando o threshold de similaridade.
	 */
	private void setSimilarityThreshold(Float value) {
		Query query = this.em.createNativeQuery("SELECT set_limit((?1))");
		query.setParameter(1, value);
		query.getSingleResult();
	}

	/**
	 * Cria um conceito a partir de um ResultSet.
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected Conceito createConceito(ResultSet rs) throws SQLException {
		return new Conceito(rs.getString("NO_TITULO"), rs.getString("NO_ESTRATO"),
				new NivelSimilaridade(rs.getFloat("SML")), rs.getInt("NU_ANO"));
	}

	/**
	 * Cria uma instrução SQL (Postgres) que, ao executada, retorna os veículos mais similares ao artigo ou trabalho parâmetro.
	 *
	 * @param tipoAvaliacao Tipo de avaliação.
	 * @param tituloVeiculo Título do veículo onde o artigo ou trabalho foi publicado/apresentado.
	 * @param area Área de avaliação a ser utilizada.
	 * @param limit Número máximo de registros similares a serem retornados.
	 * @return Uma instrução SQL que utiliza a função de similaridade do PostgreSQL.
	 */
	protected String createSqlStatement(TipoAvaliacao tipoAvaliacao, String tituloVeiculo, String area, int limit) {
		String sql = "";
		sql += "SELECT *, SIMILARITY(NO_TITULO, \'" + tituloVeiculo + "\') AS SML";
		sql += " FROM " + tipoAvaliacao.getTable() + " WHERE NO_TITULO % \'" + tituloVeiculo + "\'";
		sql += " AND NO_AREA_AVALIACAO LIKE \'%" + area.toUpperCase() + "\'";
		sql += " ORDER BY SML DESC LIMIT " + limit;
		return sql;
	}

	/**
	 * Tipos de avaliação.
	 */
	@AllArgsConstructor
	@Getter
	public enum TipoAvaliacao {
		PERIODICO("TB_QUALIS_PERIODICO"),
		EVENTO("TB_QUALIS_EVENTO");

		/**
		 * Nome da tabela utilizada para avaliação.
		 */
		private final String table;
	}
}
