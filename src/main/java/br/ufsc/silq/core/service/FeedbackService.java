package br.ufsc.silq.core.service;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.forms.FeedbackPeriodicoForm;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.FeedbackPeriodico;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.FeedbackEventoRepository;
import br.ufsc.silq.core.persistence.repository.FeedbackPeriodicoRepository;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;

@Service
@Transactional
public class FeedbackService {

	@Inject
	private FeedbackEventoRepository feedbackEventoRepo;

	@Inject
	private FeedbackPeriodicoRepository feedbackPeriodicoRepo;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private QualisEventoRepository eventoRepo;

	@Inject
	private QualisPeriodicoRepository periodicoRepo;

	/**
	 * Cria um novo feedback de matching para um {@link QualisEvento} e o usuário atual.
	 * Sobrescreve a dupla (usuário, evento) caso já exista no banco.
	 *
	 * @param form Formulário contendo os dados do feedback.
	 * @return A entidade {@link FeedbackEvento} resultante da operação.
	 */
	public FeedbackEvento sugerirMatchingEvento(@Valid FeedbackEventoForm form) {
		QualisEvento evento = this.eventoRepo.findOneById(form.getEventoId())
				.orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));
		Usuario usuario = this.usuarioService.getUsuarioLogado();

		FeedbackEvento feedback = this.feedbackEventoRepo.findOneByEventoAndUsuario(evento, usuario)
				.orElse(new FeedbackEvento());

		feedback.setQuery(form.getQuery());
		feedback.setUsuario(usuario);
		feedback.setEvento(evento);
		this.feedbackEventoRepo.save(feedback);
		return feedback;
	}

	/**
	 * Cria um novo feedback de matching para um {@link QualisPeriodico} e o usuário atual.
	 * Sobrescreve a dupla (usuário, periódico) caso já exista no banco.
	 *
	 * @param form Formulário contendo os dados do feedback.
	 * @return A entidade {@link FeedbackPeriodico} resultante da operação.
	 */
	public FeedbackPeriodico sugerirMatchingPeriodico(@Valid FeedbackPeriodicoForm form) {
		QualisPeriodico periodico = this.periodicoRepo.findOneById(form.getPeriodicoId())
				.orElseThrow(() -> new IllegalArgumentException("Periódico não encontrado"));
		Usuario usuario = this.usuarioService.getUsuarioLogado();

		FeedbackPeriodico feedback = this.feedbackPeriodicoRepo.findOneByPeriodicoAndUsuario(periodico, usuario)
				.orElse(new FeedbackPeriodico());

		feedback.setQuery(form.getQuery());
		feedback.setUsuario(usuario);
		feedback.setPeriodico(periodico);
		this.feedbackPeriodicoRepo.save(feedback);
		return feedback;
	}
}
