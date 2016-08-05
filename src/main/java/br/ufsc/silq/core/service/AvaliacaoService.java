package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQuery;

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
import br.ufsc.silq.core.utils.SilqStringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AvaliacaoService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private LattesParser lattesParser;

	/**
	 * Nível de similaridade atual sendo usado pelo serviço.
	 * Ao setar um novo threshold via {@link #setSimilarityThreshold(Float)} guardamos o valor setado aqui
	 * para que novas chamadas a este método não resultem em queries desnecessárias caso o valor não tenha sido alterado.
	 */
	private Float similarityThreshold = Float.valueOf(-1);

	/**
	 * Avalia um currículo lattes.
	 *
	 * @param lattes Currículo a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqError Caso haja um erro no parsing ou avaliação do currículo.
	 */
	@Cacheable(cacheNames = "avaliacoes", key = "{ #lattes.id, #avaliarForm.hashCode() }")
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
			List<Artigo> artigosAvaliados = parseResult.getArtigos().parallelStream()
					.filter(artigo -> form.getPeriodoAvaliacao().inclui(artigo.getAno()))
					.map(artigo -> this.avaliarArtigo(artigo, form))
					.collect(Collectors.toList());

			result.setArtigos(artigosAvaliados);
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			List<Trabalho> trabalhosAvaliados = parseResult.getTrabalhos().parallelStream()
					.filter(trabalho -> form.getPeriodoAvaliacao().inclui(trabalho.getAno()))
					.map(trabalho -> this.avaliarTrabalho(trabalho, form))
					.collect(Collectors.toList());

			result.setTrabalhos(trabalhosAvaliados);
		}

		result.sort();
		return result;
	}

	@SuppressWarnings("unused")
	public Artigo avaliarArtigo(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		// Criamos uma cópia do artigo para realizar a avaliação assim não modificamos o objeto Artigo
		// em cache retornado por LattesParser#parseCurriculum
		Artigo artigoConceituado = artigo.copy();

		if (StringUtils.isNotBlank(artigo.getIssn())) {
			artigoConceituado = this.avaliarArtigoPorIssn(artigoConceituado, avaliarForm);
		}

		if (SilqConfig.AVALIAR_ARTIGO_POR_SIMILARIDADE && !artigoConceituado.hasConceito()) {
			// Se não encontrou conceito por ISSN, busca por similaridade de título
			artigoConceituado = this.avaliarArtigoPorSimilaridade(artigoConceituado, avaliarForm);
		}

		return artigoConceituado;
	}

	private Artigo avaliarArtigoPorIssn(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		QQualisPeriodico path = QQualisPeriodico.qualisPeriodico;

		JPAQuery<QualisPeriodico> query = new JPAQuery<>(this.em);
		query.from(path);
		query.where(path.issn.eq(artigo.getIssn()));
		query.where(path.areaAvaliacao.eq(avaliarForm.getArea().toUpperCase()));
		query.orderBy(path.ano.subtract(artigo.getAno()).abs().asc());
		query.limit(SilqConfig.MAX_SIMILARITY_RESULTS);
		List<QualisPeriodico> results = query.fetch();

		List<Conceito> conceitos = new ArrayList<>();
		for (QualisPeriodico result : results) {
			conceitos.add(new Conceito(result.getTitulo(), result.getEstrato(), NivelSimilaridade.TOTAL, result.getAno()));
		}

		artigo.addConceitos(conceitos);
		return artigo;
	}

	private Artigo avaliarArtigoPorSimilaridade(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos = new ArrayList<>();
		try {
			conceitos = this.getConceitos(artigo, avaliarForm, TipoAvaliacao.PERIODICO);
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
		}
		artigo.addConceitos(conceitos);
		return artigo;
	}

	public Trabalho avaliarTrabalho(Trabalho trabalho, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos;
		try {
			conceitos = this.getConceitos(trabalho, avaliarForm, TipoAvaliacao.EVENTO);
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
		}

		// Criamos uma cópia do trabalho para realizar a avaliação assim não modificamos o objeto Trabalho
		// em cache retornado por LattesParser#parseCurriculum
		Trabalho trabalhoConceituado = trabalho.copy();
		trabalhoConceituado.addConceitos(conceitos);
		return trabalhoConceituado;
	}

	/**
	 * Seta o nível de similaridade mínimo (threshold) que será usado para as queries de similaridade no banco.
	 *
	 * @param value Valor numérico de 0 a 1 representando o threshold de similaridade.
	 * @return Verdadeiro caso o valor tenha sido alterado ou falso caso não precisou ser alterado pois
	 *         a avaliação anterior já setou este mesmo valor.
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean setSimilarityThreshold(Float value) {
		if (!value.equals(this.similarityThreshold)) {
			Query query = this.em.createNativeQuery("SELECT set_limit(?1)");
			query.setParameter(1, value);
			Float result = (Float) query.getSingleResult();
			log.trace("Similarity threshold set to {}", result);
			this.similarityThreshold = result;
			return true;
		}

		return false;
	}

	/**
	 * Obtém os conceitos de um evento ou periódico realizando uma busca por similaridade na base Qualis.
	 *
	 * @param trabalho Trabalho a ser avaliado (deve conter título do veículo e ano).
	 * @param avaliarForm Opções de avaliação.
	 * @param tipoAvaliacao Tipo de avaliação (altera a tabela do banco a ser consultada).
	 * @return A lista de conceitos do veículo.
	 * @throws SQLException Caso haja um erro ao executar o SQL.
	 */
	public List<Conceito> getConceitos(Trabalho trabalho, @Valid AvaliarForm avaliarForm, TipoAvaliacao tipoAvaliacao) throws SQLException {
		this.setSimilarityThreshold(avaliarForm.getNivelSimilaridade().getValue());

		String sql = "SELECT NO_TITULO, NO_ESTRATO, SIMILARITY(NO_TITULO, ?1) AS SML, NU_ANO";
		sql += " FROM " + tipoAvaliacao.getTable() + " WHERE NO_TITULO % ?1";
		sql += " AND NO_AREA_AVALIACAO = ?2";
		sql += " ORDER BY SML DESC, ABS(NU_ANO - ?3) ASC";
		sql += " LIMIT ?4";

		Query query = this.em.createNativeQuery(sql);
		query.setParameter(1, SilqStringUtils.normalizeString(trabalho.getTituloVeiculo()));
		query.setParameter(2, avaliarForm.getArea().toUpperCase());
		query.setParameter(3, trabalho.getAno());
		query.setParameter(4, SilqConfig.MAX_SIMILARITY_RESULTS);

		List<Object[]> results = query.getResultList();
		return results.stream()
				.map(obj -> new Conceito((String) obj[0], (String) obj[1], new NivelSimilaridade((Float) obj[2]), (Integer) obj[3]))
				.collect(Collectors.toList());
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
