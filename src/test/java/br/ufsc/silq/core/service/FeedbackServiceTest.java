package br.ufsc.silq.core.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.Conceito;
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
		this.feedbackEventoForm = new FeedbackEventoForm(this.evento.getId(), "The evento query here");

		this.periodico = this.periodicoRepo.findOne(1L);
		this.feedbackPeriodicoForm = new FeedbackPeriodicoForm(this.periodico.getId(), "The periódico query here");
	}

	@Test
	public void testSugerirMatchingEvento() {
		FeedbackEvento feedback = this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);

		Assertions.assertThat(feedback.getId()).isNotNull();
		Assertions.assertThat(feedback.getQuery()).isEqualTo(this.feedbackEventoForm.getQuery());
		Assertions.assertThat(feedback.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback.getEvento()).isEqualTo(this.evento);
		Assertions.assertThat(feedback.getDate()).isCloseTo(new Date(), 1000);
	}

	@Test
	public void testSugerirMatchingPeriodico() {
		FeedbackPeriodico feedback = this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);

		Assertions.assertThat(feedback.getId()).isNotNull();
		Assertions.assertThat(feedback.getQuery()).isEqualTo(this.feedbackPeriodicoForm.getQuery());
		Assertions.assertThat(feedback.getUsuario()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(feedback.getPeriodico()).isEqualTo(this.periodico);
		Assertions.assertThat(feedback.getDate()).isCloseTo(new Date(), 1000);
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
	}

	@Test
	public void testGetConceitoFeedbackPeriodico() {
		this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);
		Optional<Conceito> conceito = this.feedbackService.getConceitoFeedbackPeriodico(this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado);

		Assertions.assertThat(conceito).isPresent();
		Assertions.assertThat(conceito.get().getId()).isEqualTo(this.feedbackPeriodicoForm.getPeriodicoId());
		Assertions.assertThat(conceito.get().getTituloVeiculo()).isEqualTo(this.periodico.getTitulo());
		Assertions.assertThat(conceito.get().getConceito()).isEqualTo(this.periodico.getEstrato());
		Assertions.assertThat(conceito.get().getAno()).isEqualTo(this.periodico.getAno());
		Assertions.assertThat(conceito.get().isFeedback()).isTrue();
	}

	@Test
	public void testGetConceitoFeedbackEvento() {
		this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);
		Optional<Conceito> conceito = this.feedbackService.getConceitoFeedbackEvento(this.feedbackEventoForm.getQuery(), this.usuarioLogado);

		Assertions.assertThat(conceito).isPresent();
		Assertions.assertThat(conceito.get().getId()).isEqualTo(this.feedbackEventoForm.getEventoId());
		Assertions.assertThat(conceito.get().getTituloVeiculo()).isEqualTo(this.evento.getTitulo());
		Assertions.assertThat(conceito.get().getConceito()).isEqualTo(this.evento.getEstrato());
		Assertions.assertThat(conceito.get().getAno()).isEqualTo(this.evento.getAno());
		Assertions.assertThat(conceito.get().isFeedback()).isTrue();
	}

	@Test
	public void testDeleteFeedbackPeriodico() {
		this.feedbackService.sugerirMatchingPeriodico(this.feedbackPeriodicoForm);
		Optional<Conceito> conceito = this.feedbackService.getConceitoFeedbackPeriodico(this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado);
		Assertions.assertThat(conceito).isPresent();

		Long removed = this.feedbackService.deleteFeedbackPeriodico(this.feedbackPeriodicoForm);
		conceito = this.feedbackService.getConceitoFeedbackPeriodico(this.feedbackPeriodicoForm.getQuery(), this.usuarioLogado);
		Assertions.assertThat(conceito).isEmpty();
		Assertions.assertThat(removed).isEqualTo(1);
	}

	@Test
	public void testDeleteFeedbackEvento() {
		this.feedbackService.sugerirMatchingEvento(this.feedbackEventoForm);
		Optional<Conceito> conceito = this.feedbackService.getConceitoFeedbackEvento(this.feedbackEventoForm.getQuery(), this.usuarioLogado);
		Assertions.assertThat(conceito).isPresent();

		Long removed = this.feedbackService.deleteFeedbackEvento(this.feedbackEventoForm);
		conceito = this.feedbackService.getConceitoFeedbackEvento(this.feedbackEventoForm.getQuery(), this.usuarioLogado);
		Assertions.assertThat(conceito).isEmpty();
		Assertions.assertThat(removed).isEqualTo(1);
	}
}
