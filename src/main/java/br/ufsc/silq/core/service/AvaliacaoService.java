package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
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
import br.ufsc.silq.core.service.SimilarityService.TipoAvaliacao;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AvaliacaoService {

	@Inject
	private LattesParser lattesParser;

	@Inject
	private SimilarityService similarityService;

	@PersistenceContext
	private EntityManager em;

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
	private AvaliacaoResult avaliar(ParseResult parseResult, @Valid AvaliarForm form) {
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

	/**
	 * Avalia um artigo conforme as opções de avaliação, retornando uma cópia do Artigo com
	 * os atributos {@link Artigo#getConceitos()} preenchidos.
	 *
	 * @param artigo Artigo a ser avaliado.
	 * @param avaliarForm Opções de avaliação.
	 * @return Uma cópia do Artigo parâmetro com os conceitos preenchidos.
	 */
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
			conceitos.add(new Conceito(result.getId(), result.getTitulo(), result.getEstrato(), NivelSimilaridade.TOTAL, result.getAno()));
		}

		artigo.addConceitos(conceitos);
		return artigo;
	}

	private Artigo avaliarArtigoPorSimilaridade(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos = new ArrayList<>();
		try {
			conceitos = this.similarityService.getConceitos(artigo, avaliarForm, TipoAvaliacao.PERIODICO);
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
		}
		artigo.addConceitos(conceitos);
		return artigo;
	}

	/**
	 * Avalia um trabalho conforme as opções de avaliação, retornando uma cópia do Trabalho com
	 * os atributos {@link Trabalho#getConceitos()} preenchidos.
	 *
	 * @param trabalho Trabalho a ser avaliado.
	 * @param avaliarForm Opções de avaliação.
	 * @return Uma cópia do Trabalho parâmetro com os conceitos preenchidos.
	 */
	public Trabalho avaliarTrabalho(Trabalho trabalho, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos;
		try {
			conceitos = this.similarityService.getConceitos(trabalho, avaliarForm, TipoAvaliacao.EVENTO);
		} catch (SQLException e) {
			throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
		}

		// Criamos uma cópia do trabalho para realizar a avaliação assim não modificamos o objeto Trabalho
		// em cache retornado por LattesParser#parseCurriculum
		Trabalho trabalhoConceituado = trabalho.copy();
		trabalhoConceituado.addConceitos(conceitos);
		return trabalhoConceituado;
	}
}
