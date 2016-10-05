package br.ufsc.silq.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQuery;

import br.ufsc.silq.core.data.AvaliacaoCollectionResult;
import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.data.AvaliacaoStats;
import br.ufsc.silq.core.data.AvaliacaoType;
import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.Conceituado;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.exception.SilqError;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.FeedbackPeriodico;
import br.ufsc.silq.core.persistence.entities.QQualisPeriodico;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.entities.Usuario;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AvaliacaoService {

	@Inject
	private LattesParser lattesParser;

	@Inject
	private SimilarityService similarityService;

	@Inject
	private FeedbackService feedbackService;

	@Inject
	private UsuarioService usuarioService;

	@PersistenceContext
	private EntityManager em;

	/**
	 * Avalia um currículo lattes, usando informações de feedback do usuário atual.
	 *
	 * @param lattes Currículo a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqError Caso haja um erro no parsing ou avaliação do currículo.
	 */
	public AvaliacaoResult avaliar(CurriculumLattes lattes, @Valid AvaliarForm avaliarForm) {
		return this.avaliar(lattes, avaliarForm, this.usuarioService.getUsuarioLogado());
	}

	/**
	 * Avalia um currículo lattes, usando informações de feedback do usuário parâmetro
	 *
	 * @param lattes Currículo a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @param usuario Usuário cujos feedbacks devem ser utilizados para a avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqError Caso haja um erro no parsing ou avaliação do currículo.
	 */
	public AvaliacaoResult avaliar(CurriculumLattes lattes, @Valid AvaliarForm avaliarForm, Usuario usuario) {
		ParseResult parseResult = null;
		try {
			parseResult = this.lattesParser.parseCurriculum(lattes);
		} catch (SilqLattesException e) {
			throw new SilqError(e);
		}
		return this.avaliar(parseResult, avaliarForm, usuario);
	}

	/**
	 * Avalia uma coleção de currículos, retornando estatísticas desta coleção.
	 * Usa feedbacks do usuário logado para a avaliação.
	 *
	 * @param curriculos Coleção de currículos a serem avaliados.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Resultado de avaliação contendo as estatísticas contabilizadas com base na avaliação dos currículos da coleção.
	 * @throws SilqLattesException Caso haja um erro no parsing ou avaliação de algum currículo da coleção.
	 */
	public AvaliacaoCollectionResult avaliarCollection(Collection<CurriculumLattes> curriculos, @Valid AvaliarForm avaliarForm)
			throws SilqLattesException {
		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();

		AvaliacaoStats stats = curriculos.parallelStream()
				.map(curriculo -> this.avaliar(curriculo, avaliarForm, usuarioLogado))
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
	 * @param usuario Usuário do pesquisador que está sendo avaliado. Caso especificado,
	 *            os feedbacks deste usuário serão considerados para o fim de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 */
	public AvaliacaoResult avaliar(ParseResult parseResult, @Valid AvaliarForm form, @Nullable Usuario usuario) {
		AvaliacaoResult result = new AvaliacaoResult(form, parseResult.getDadosGerais());

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			List<Conceituado<Artigo>> artigosAvaliados = parseResult.getArtigos().parallelStream()
					.filter(artigo -> form.getPeriodoAvaliacao().inclui(artigo.getAno()))
					.map(artigo -> this.avaliarArtigo(artigo, form, usuario))
					.collect(Collectors.toList());

			result.setArtigos(artigosAvaliados);
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			List<Conceituado<Trabalho>> trabalhosAvaliados = parseResult.getTrabalhos().parallelStream()
					.filter(trabalho -> form.getPeriodoAvaliacao().inclui(trabalho.getAno()))
					.map(trabalho -> this.avaliarTrabalho(trabalho, form, usuario))
					.collect(Collectors.toList());

			result.setTrabalhos(trabalhosAvaliados);
		}

		result.sort();
		return result;
	}

	/**
	 * Avalia um artigo conforme as opções de avaliação, retornando um objeto {@link Conceituado}
	 * contendo o Artigo e os conceitos atribuídos.
	 *
	 * @param artigo Artigo a ser avaliado.
	 * @param avaliarForm Opções de avaliação.
	 * @param usuario Usuário do pesquisador que está sendo avaliado. Caso especificado,
	 *            os feedbacks deste usuário serão considerados para o fim de avaliação.
	 * @return Um objeto {@link Conceituado} contendo o artigo original e seus conceitos atribuídos.
	 */
	public Conceituado<Artigo> avaliarArtigo(Artigo artigo, @Valid AvaliarForm avaliarForm, @Nullable Usuario usuario) {
		Conceituado<Artigo> artigoConceituado = new Conceituado<>(artigo);

		if (StringUtils.isNotBlank(artigo.getIssn())) {
			artigoConceituado = this.avaliarArtigoPorIssn(artigo, avaliarForm);
		}

		if (avaliarForm.isAvaliarArtigoPorSimilaridade() && !artigoConceituado.hasConceito()) {
			// Se não encontrou conceito por ISSN, busca por similaridade de título
			artigoConceituado = this.avaliarArtigoPorSimilaridade(artigo, avaliarForm);
		}

		if (usuario != null && avaliarForm.isUsarFeedback()) {
			// Checa pelo feedback do usuário
			Optional<SimilarityResult<FeedbackPeriodico>> feedback = this.feedbackService.getFeedbackPeriodico(artigo.getTituloVeiculo(),
					usuario, avaliarForm.getNivelSimilaridade());
			if (feedback.isPresent()) {
				if (feedback.get().getResultado().isNegativo()) {
					// Se for um feedback negativo, marca o artigo como tendo um feedback negativo
					artigoConceituado.setFeedbackNegativo(true);
				} else {
					// Se o feedback é um periódico válido, adiciona-o como conceito
					artigoConceituado.addConceito(this.feedbackService.feedbackPeriodicoToConceito(feedback.get()));
				}
			}
		}

		artigoConceituado.keepTopK(avaliarForm.getMaxConceitos());
		return artigoConceituado;
	}

	private Conceituado<Artigo> avaliarArtigoPorIssn(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		QQualisPeriodico path = QQualisPeriodico.qualisPeriodico;

		JPAQuery<QualisPeriodico> query = new JPAQuery<>(this.em);
		query.from(path);
		query.where(path.issn.eq(artigo.getIssn()));
		query.where(path.areaAvaliacao.eq(avaliarForm.getArea().toUpperCase()));
		query.orderBy(path.ano.subtract(artigo.getAno()).abs().asc());
		query.limit(avaliarForm.getMaxConceitos());
		List<QualisPeriodico> results = query.fetch();

		List<Conceito> conceitos = new ArrayList<>();
		for (QualisPeriodico result : results) {
			conceitos.add(new Conceito(result.getId(), result.getTitulo(), result.getEstrato(), NivelSimilaridade.TOTAL, result.getAno()));
		}

		return new Conceituado<>(artigo, conceitos);
	}

	private Conceituado<Artigo> avaliarArtigoPorSimilaridade(Artigo artigo, @Valid AvaliarForm avaliarForm) {
		List<Conceito> conceitos = new ArrayList<>();
		try {
			conceitos = this.similarityService.getConceitos(QualisPeriodico.class, artigo, avaliarForm);
		} catch (Exception e) {
			throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
		}
		return new Conceituado<>(artigo, conceitos);
	}

	/**
	 * Avalia um trabalho conforme as opções de avaliação, retornando um objeto {@link Conceituado} que encapsula
	 * o trabalho parâmetro original e seus conceitos atribuídos.
	 *
	 * @param trabalho Trabalho a ser avaliado.
	 * @param avaliarForm Opções de avaliação.
	 * @param usuario Usuário do pesquisador que está sendo avaliado. Caso especificado,
	 *            os feedbacks deste usuário serão considerados para o fim de avaliação.
	 * @return Um objeto {@link Conceituado} contendo o trabalho original e seus conceitos atribuídos.
	 */
	public Conceituado<Trabalho> avaliarTrabalho(Trabalho trabalho, @Valid AvaliarForm avaliarForm, @Nullable Usuario usuario) {
		List<Conceito> conceitos;
		try {
			conceitos = this.similarityService.getConceitos(QualisEvento.class, trabalho, avaliarForm);
		} catch (Exception e) {
			throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
		}

		Conceituado<Trabalho> trabalhoConceituado = new Conceituado<>(trabalho, conceitos);

		if (usuario != null && avaliarForm.isUsarFeedback()) {
			// Checa pelo feedback do usuário
			Optional<SimilarityResult<FeedbackEvento>> feedback = this.feedbackService.getFeedbackEvento(trabalho.getTituloVeiculo(),
					usuario, avaliarForm.getNivelSimilaridade());
			if (feedback.isPresent()) {
				if (feedback.get().getResultado().isNegativo()) {
					// Se for um feedback negativo, marca o trabalho como tendo um feedback negativo
					trabalhoConceituado.setFeedbackNegativo(true);
				} else {
					// Se o feedback é um evento válido, adiciona-o como conceito
					trabalhoConceituado.addConceito(this.feedbackService.feedbackEventoToConceito(feedback.get()));
				}
			}
		}

		trabalhoConceituado.keepTopK(avaliarForm.getMaxConceitos());
		return trabalhoConceituado;
	}
}
