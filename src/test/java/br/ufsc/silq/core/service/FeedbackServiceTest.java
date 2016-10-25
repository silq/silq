package br.ufsc.silq.core.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.data.TipoConceito;
import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.forms.FeedbackPeriodicoForm;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.FeedbackPeriodico;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.test.WebContextTest;

public class FeedbackServiceTest extends WebContextTest {

	@Inject
	private FeedbackService feedbackService;

	@Inject
	private QualisEventoRepository eventoRepo;

	@Inject
	private QualisPeriodicoRepository periodicoRepo;

	private Usuario usuarioLogado;

	private FeedbackEventoForm feedbackEventoForm;

	private QualisEvento evento;

	private FeedbackPeriodicoForm feedbackPeriodicoForm;

	private QualisPeriodico periodico;

	@Before
	public void setUp() {
		this.usuarioLogado = this.loginUser();

		this.evento = this.eventoRepo.findOne(1L);
		this.feedbackEventoForm = new FeedbackEventoForm(this.evento.getId(), "The evento query here", this.evento.getAno());

		this.periodico = this.periodicoRepo.findOne(1L);
		this.feedbackPeriodicoForm = new FeedbackPeriodicoForm(this.periodico.getId(), "The periódico query here", this.periodico.getAno());
	}

	@Test
	public void testSugerirMatchingEvento() {
		FeedbackEvento feedback = this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);

		Assertions.assertThat(feedback.getId()).isNotNull();
		Assertions.assertThat(feedback.getQuery()).isEqualTo(this.feedbackEventoForm.getQuery());
		Assertions.assertThat(feedback.getAno()).isEqualTo(this.feedbackEventoForm.getAno());
		Assertions.assertThat(feedback.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback.getEvento()).isEqualTo(this.evento);
		Assertions.assertThat(feedback.getDate()).isCloseTo(new Date(), 1000);
		Assertions.assertThat(feedback.getValidation()).isFalse();
	}

	@Test
	public void testSugerirMatchingPeriodico() {
		FeedbackPeriodico feedback = this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);

		Assertions.assertThat(feedback.getId()).isNotNull();
		Assertions.assertThat(feedback.getQuery()).isEqualTo(this.feedbackPeriodicoForm.getQuery());
		Assertions.assertThat(feedback.getAno()).isEqualTo(this.feedbackPeriodicoForm.getAno());
		Assertions.assertThat(feedback.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback.getPeriodico()).isEqualTo(this.periodico);
		Assertions.assertThat(feedback.getDate()).isCloseTo(new Date(), 1000);
		Assertions.assertThat(feedback.getValidation()).isFalse();
	}

	@Test
	public void testSugerirNovamenteEvento() {
		FeedbackEvento feedback1 = this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);

		// Ao salvar a mesma query com feedback de evento diferente
		QualisEvento outroEvento = this.eventoRepo.findOne(2L);
		this.feedbackEventoForm.setEventoId(outroEvento.getId());
		FeedbackEvento feedback2 = this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);

		// Deve sobrescrever o feedback anterior, mantendo somente um feedback para cada dupla (usuário, query)
		Assertions.assertThat(feedback2.getId()).isEqualTo(feedback1.getId());
		Assertions.assertThat(feedback2.getQuery()).isEqualTo(this.feedbackEventoForm.getQuery());
		Assertions.assertThat(feedback2.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback2.getEvento()).isEqualTo(outroEvento);
		Assertions.assertThat(feedback2.getDate()).isCloseTo(new Date(), 1000);
		Assertions.assertThat(feedback2.getValidation()).isFalse();
	}

	@Test
	public void testSugerirNovamentePeriodico() {
		FeedbackPeriodico feedback1 = this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);

		// Ao salvar a mesma query com feedback de periódico diferente
		QualisPeriodico outroPeriodico = this.periodicoRepo.findOne(2L);
		this.feedbackPeriodicoForm.setPeriodicoId(outroPeriodico.getId());
		FeedbackPeriodico feedback2 = this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);

		// Deve sobrescrever o feedback anterior, mantendo somente um feedback para cada dupla (usuário, query)
		Assertions.assertThat(feedback2.getId()).isEqualTo(feedback1.getId());
		Assertions.assertThat(feedback2.getQuery()).isEqualTo(this.feedbackPeriodicoForm.getQuery());
		Assertions.assertThat(feedback2.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback2.getPeriodico()).isEqualTo(outroPeriodico);
		Assertions.assertThat(feedback2.getDate()).isCloseTo(new Date(), 1000);
		Assertions.assertThat(feedback2.getValidation()).isFalse();
	}

	@Test
	public void testGetFeedbackPeriodicoComSimilaridadeTotal() {
		this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);
		Optional<SimilarityResult<FeedbackPeriodico>> result = this.feedbackService.getFeedbackPeriodico(
				this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);

		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getResultado().getPeriodico().getId()).isEqualTo(this.feedbackPeriodicoForm.getPeriodicoId());
		Assertions.assertThat(result.get().getSimilaridade()).isEqualTo(NivelSimilaridade.TOTAL);
	}

	@Test
	public void testGetFeedbackEventoComSimilaridadeTotal() {
		this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);
		Optional<SimilarityResult<FeedbackEvento>> result = this.feedbackService.getFeedbackEvento(
				this.feedbackEventoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);

		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getResultado().getEvento().getId()).isEqualTo(this.feedbackEventoForm.getEventoId());
		Assertions.assertThat(result.get().getSimilaridade()).isEqualTo(NivelSimilaridade.TOTAL);
	}

	@Test
	public void testGetFeedbackPeriodicoComSimilaridadeParcial() {
		this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);
		Optional<SimilarityResult<FeedbackPeriodico>> result = this.feedbackService.getFeedbackPeriodico(
				this.feedbackPeriodicoForm.getQuery() + " ftw", this.usuarioLogado, NivelSimilaridade.NORMAL);

		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getResultado().getPeriodico().getId()).isEqualTo(this.feedbackPeriodicoForm.getPeriodicoId());
		Assertions.assertThat(result.get().getSimilaridade().getValue()).isCloseTo(0.86f, Assertions.within(0.02f));
	}

	@Test
	public void testGetFeedbackEventoComSimilaridadeParial() {
		this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);
		Optional<SimilarityResult<FeedbackEvento>> result = this.feedbackService.getFeedbackEvento(
				this.feedbackEventoForm.getQuery() + " ftw", this.usuarioLogado, NivelSimilaridade.NORMAL);

		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getResultado().getEvento().getId()).isEqualTo(this.feedbackEventoForm.getEventoId());
		Assertions.assertThat(result.get().getSimilaridade().getValue()).isCloseTo(0.86f, Assertions.within(0.02f));
	}

	@Test
	public void testFeedbackPeriodicoToConceito() {
		this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);
		Optional<SimilarityResult<FeedbackPeriodico>> result = this.feedbackService.getFeedbackPeriodico(
				this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);

		Assertions.assertThat(result).isPresent();
		Conceito conceito = this.feedbackService.feedbackPeriodicoToConceito(result.get());
		Assertions.assertThat(conceito.getTituloVeiculo()).isEqualTo(this.periodico.getTitulo());
		Assertions.assertThat(conceito.getAno()).isEqualTo(this.periodico.getAno());
		Assertions.assertThat(conceito.getConceito()).isEqualTo(this.periodico.getEstrato());
		Assertions.assertThat(conceito.getSimilaridade()).isEqualTo(NivelSimilaridade.TOTAL.getValue());
		Assertions.assertThat(conceito.getTipoConceito()).isEqualTo(TipoConceito.FEEDBACK);
	}

	@Test
	public void testFeedbackEventoToConceito() {
		this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);
		Optional<SimilarityResult<FeedbackEvento>> result = this.feedbackService.getFeedbackEvento(
				this.feedbackEventoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);

		Assertions.assertThat(result).isPresent();
		Conceito conceito = this.feedbackService.feedbackEventoToConceito(result.get());
		Assertions.assertThat(conceito.getTituloVeiculo()).isEqualTo(this.evento.getTitulo());
		Assertions.assertThat(conceito.getAno()).isEqualTo(this.evento.getAno());
		Assertions.assertThat(conceito.getConceito()).isEqualTo(this.evento.getEstrato());
		Assertions.assertThat(conceito.getSimilaridade()).isEqualTo(NivelSimilaridade.TOTAL.getValue());
		Assertions.assertThat(conceito.getTipoConceito()).isEqualTo(TipoConceito.FEEDBACK);
		Assertions.assertThat(conceito.getSiglaVeiculo()).isEqualTo(this.evento.getSigla());
	}

	@Test
	public void testDeleteFeedbackPeriodico() {
		this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);
		Optional<SimilarityResult<FeedbackPeriodico>> result = this.feedbackService.getFeedbackPeriodico(
				this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);
		Assertions.assertThat(result).isPresent();

		Long removed = this.feedbackService.deleteFeedbackPeriodico(this.feedbackPeriodicoForm);
		result = this.feedbackService.getFeedbackPeriodico(this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);
		Assertions.assertThat(result).isEmpty();
		Assertions.assertThat(removed).isEqualTo(1);
	}

	@Test
	public void testDeleteFeedbackEvento() {
		this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);
		Optional<SimilarityResult<FeedbackEvento>> result = this.feedbackService.getFeedbackEvento(
				this.feedbackEventoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);
		Assertions.assertThat(result).isPresent();

		Long removed = this.feedbackService.deleteFeedbackEvento(this.feedbackEventoForm);
		result = this.feedbackService.getFeedbackEvento(this.feedbackEventoForm.getQuery(), this.usuarioLogado, NivelSimilaridade.TOTAL);
		Assertions.assertThat(result).isEmpty();
		Assertions.assertThat(removed).isEqualTo(1);
	}

	@Test
	public void testSugerirMatchingNegativoEvento() {
		FeedbackEventoForm form = new FeedbackEventoForm(null, "Query apenas", null);
		Assertions.assertThat(form.isNegativo()).isTrue();

		FeedbackEvento feedback = this.feedbackService.sugerirMatchingEvento(form);

		Assertions.assertThat(feedback.isNegativo()).isTrue();
		Assertions.assertThat(feedback.getId()).isNotNull();
		Assertions.assertThat(feedback.getQuery()).isEqualTo(form.getQuery());
		Assertions.assertThat(feedback.getAno()).isEqualTo(form.getAno());
		Assertions.assertThat(feedback.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback.getEvento()).isNull();
		Assertions.assertThat(feedback.getDate()).isCloseTo(new Date(), 1000);
	}

	@Test
	public void testSugerirMatchingNegativoPeriodico() {
		FeedbackPeriodicoForm form = new FeedbackPeriodicoForm(null, "Apenas a query", null);
		Assertions.assertThat(form.isNegativo()).isTrue();

		FeedbackPeriodico feedback = this.feedbackService.sugerirMatchingPeriodico(form);

		Assertions.assertThat(feedback.isNegativo()).isTrue();
		Assertions.assertThat(feedback.getId()).isNotNull();
		Assertions.assertThat(feedback.getQuery()).isEqualTo(form.getQuery());
		Assertions.assertThat(feedback.getAno()).isEqualTo(form.getAno());
		Assertions.assertThat(feedback.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback.getPeriodico()).isNull();
		Assertions.assertThat(feedback.getDate()).isCloseTo(new Date(), 1000);
	}
}
