package br.ufsc.silq.core.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import com.mysema.query.types.expr.BooleanExpression;

import br.ufsc.silq.core.SilqConfig;
import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.enums.AvaliacaoType;
import br.ufsc.silq.core.exception.SilqError;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.QQualisPeriodico;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.utils.SilqStringUtils;

@Service
@Transactional(readOnly = true)
public class AvaliacaoService {

	@Inject
	private DataSource dataSource;

	@Inject
	private QualisPeriodicoRepository qualisPeriodicoRepository;

	@Inject
	private LattesParser lattesParser;

	/**
	 * Extrai dados do currículo Lattes utilizando {@link LattesParser} e avalia-o.
	 *
	 * @param lattes XML do currículo Lattes do pesquisador a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqLattesException
	 */
	public AvaliacaoResult avaliar(Document lattes, @Valid AvaliarForm avaliarForm) throws SilqLattesException {
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		return this.avaliar(parseResult, avaliarForm);
	}

	/**
	 * Extrai dados do currículo Lattes utilizando {@link LattesParser} e avalia-o.
	 *
	 * @param lattes Byte array do XML do currículo Lattes do pesquisador a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqLattesException
	 */
	public AvaliacaoResult avaliar(String lattes, @Valid AvaliarForm avaliarForm) throws SilqLattesException {
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		return this.avaliar(parseResult, avaliarForm);
	}

	/**
	 * Avalia dados de um currículo Lattes já parseados pelo {@link LattesParser}.
	 *
	 * @param parseResult Resultado do parsing do currículo Lattes de um pesquisador.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqLattesException
	 */
	public AvaliacaoResult avaliar(ParseResult parseResult, AvaliarForm form) {
		AvaliacaoResult result = new AvaliacaoResult(form, parseResult.getDadosGerais());

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			parseResult.getArtigos().parallelStream().forEach((artigo) -> {
				if (!form.getPeriodoAvaliacao().inclui(artigo.getAno())) {
					// Não avalia artigo que não pertence ao período de avaliação informado.
					return;
				}

				result.getArtigos().add(this.avaliarArtigo(artigo, form));
			});
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			parseResult.getTrabalhos().parallelStream().forEach((trabalho) -> {
				if (!form.getPeriodoAvaliacao().inclui(trabalho.getAno())) {
					// Não avalia trabalho que não pertence ao período de avaliação informado.
					return;
				}

				result.getTrabalhos().add(this.avaliarTrabalho(trabalho, form));
			});
		}

		result.order();
		return result;
	}

	public Artigo avaliarArtigo(Artigo artigo, AvaliarForm avaliarForm) {
		if (artigo.getIssn().isEmpty()) {
			// TODO (bonetti): Ordenar por ano aqui também
			return this.avaliarArtigoPorSimilaridade(artigo, avaliarForm);
		} else {
			return this.avaliarArtigoPorIssn(artigo, avaliarForm);
		}
	}

	private Artigo avaliarArtigoPorSimilaridade(Artigo artigo, AvaliarForm avaliarForm) {
		List<Conceito> conceitos = new ArrayList<>();
		try {
			conceitos = this.getConceitos(artigo.getTituloVeiculo(), avaliarForm, "TB_QUALIS_PERIODICO");
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
		}
		artigo.setConceitos(conceitos);
		return artigo;
	}

	public Artigo avaliarArtigoPorIssn(Artigo artigo, AvaliarForm avaliarForm) {
		QQualisPeriodico path = QQualisPeriodico.qualisPeriodico;
		BooleanExpression query = path.issn.eq(artigo.getIssn())
				.and(path.areaAvaliacao.eq(avaliarForm.getArea().toUpperCase()));

		Iterable<QualisPeriodico> results = this.qualisPeriodicoRepository.findAll(query,
				path.ano.subtract(artigo.getAno()).abs().asc());

		List<Conceito> conceitos = new ArrayList<>();
		for (QualisPeriodico result : results) {
			conceitos.add(new Conceito(result.getTitulo(), result.getEstrato(), NivelSimilaridade.TOTAL, result.getAno()));
		}

		artigo.setConceitos(conceitos);
		return artigo;
	}

	public Trabalho avaliarTrabalho(Trabalho trabalho, AvaliarForm avaliarForm) {
		List<Conceito> conceitos;
		try {
			conceitos = this.getConceitos(trabalho.getTituloVeiculo(), avaliarForm, "TB_QUALIS_EVENTO");
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
		}

		trabalho.setConceitos(conceitos);
		return trabalho;
	}

	/**
	 * Obtém os conceitos de um evento ou periódico realizando uma busca por similaridade na base Qualis.
	 *
	 * @param tituloVeiculo Título do evento ou periódico que deseja-se avaliar.
	 * @param avaliarForm Opções de avaliação.
	 * @param table Tabela base de registros Qualis a ser utilizada na avaliação.
	 * @return
	 * @throws SQLException
	 */
	public List<Conceito> getConceitos(String tituloVeiculo, AvaliarForm avaliarForm, String table) throws SQLException {
		tituloVeiculo = SilqStringUtils.normalizeString(tituloVeiculo);

		List<Conceito> conceitos = new ArrayList<>();

		Connection connection = this.dataSource.getConnection();
		Statement st = connection.createStatement();
		st.executeQuery("SELECT set_limit(" + avaliarForm.getNivelSimilaridade().getValue() + "::real)");
		String sqlStatement = this.createSqlStatement(table, tituloVeiculo, avaliarForm.getArea(), SilqConfig.MAX_PARSE_RESULTS);
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
	 * @param table Tabela a ser utilizada na avaliação.
	 * @param tituloVeiculo Título do veículo onde o artigo ou trabalho foi publicado/apresentado.
	 * @param area Área de avaliação a ser utilizada.
	 * @param limit Número máximo de registros similares a serem retornados.
	 * @return Uma instrução SQL que utiliza a função de similaridade do PostgreSQL.
	 */
	protected String createSqlStatement(String table, String tituloVeiculo, String area, int limit) {
		String sql = "";
		sql += "SELECT *, SIMILARITY(NO_TITULO, \'" + tituloVeiculo + "\') AS SML";
		sql += " FROM " + table + " WHERE NO_TITULO % \'" + tituloVeiculo + "\'";
		sql += " AND NO_AREA_AVALIACAO LIKE \'%" + area.toUpperCase() + "\'";
		sql += " ORDER BY SML DESC LIMIT " + limit;
		return sql;
	}

}
