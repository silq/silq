package br.ufsc.silq.core.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.data.Conceito;
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

	@Inject
	private SimilarityService similarityService;

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

		FeedbackEvento feedback = this.feedbackEventoRepo.findOneByQueryAndUsuario(form.getQuery(), usuario)
				.orElse(new FeedbackEvento());

		feedback.setQuery(form.getQuery());
		feedback.setUsuario(usuario);
		feedback.setEvento(evento);
		feedback.setDate(new Date());
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

		FeedbackPeriodico feedback = this.feedbackPeriodicoRepo.findOneByQueryAndUsuario(form.getQuery(), usuario)
				.orElse(new FeedbackPeriodico());

		feedback.setQuery(form.getQuery());
		feedback.setUsuario(usuario);
		feedback.setPeriodico(periodico);
		feedback.setDate(new Date());
		this.feedbackPeriodicoRepo.save(feedback);
		return feedback;
	}

	/**
	 * Busca por um feedback de EVENTO com a dada query pertencente ao dado usuário. Caso encontrado, converte-o para
	 * um objeto {@link Conceito}
	 *
	 * @param query Query do feedback buscado.
	 * @param usuario Usuário dono do feedback buscado.
	 * @return Um objeto Conceito opcional, populado caso tenha sido encontrado um feedback com os dados parâmetros de busca.
	 */
	public Optional<Conceito> getConceitoFeedbackEvento(String query, Usuario usuario) {
		Optional<FeedbackEvento> feedback = this.feedbackEventoRepo.findOneByQueryAndUsuario(query, usuario);
		return feedback.map(this::feedbackToConceito);
	}

	/**
	 * Busca por um feedback de PERIÓDICO com a dada query pertencente ao dado usuário. Caso encontrado, converte-o para
	 * um objeto {@link Conceito}
	 *
	 * @param query Query do feedback buscado.
	 * @param usuario Usuário dono do feedback buscado.
	 * @return Um objeto Conceito opcional, populado caso tenha sido encontrado um feedback com os dados parâmetros de busca.
	 */
	public Optional<Conceito> getConceitoFeedbackPeriodico(String query, Usuario usuario) {
		Optional<FeedbackPeriodico> feedback = this.feedbackPeriodicoRepo.findOneByQueryAndUsuario(query, usuario);
		return feedback.map(this::feedbackToConceito);
	}

	private Conceito feedbackToConceito(FeedbackEvento feedback) {
		QualisEvento evento = feedback.getEvento();
		Conceito conceito = new Conceito(evento.getId(), evento.getTitulo(), evento.getEstrato(),
				this.similarityService.calcularSimilaridade(feedback.getQuery(), evento.getTitulo()), evento.getAno());
		conceito.setFlagged(true);
		return conceito;
	}

	private Conceito feedbackToConceito(FeedbackPeriodico feedback) {
		QualisPeriodico periodico = feedback.getPeriodico();
		Conceito conceito = new Conceito(periodico.getId(), periodico.getTitulo(), periodico.getEstrato(),
				this.similarityService.calcularSimilaridade(feedback.getQuery(), periodico.getTitulo()), periodico.getAno());
		conceito.setFlagged(true);
		return conceito;
	}

	/**
	 * Remove um feedback de PERIÓDICO do usuário logado.
	 *
	 * @param form Form contendo a query a ser removida.
	 * @return O número de registros excluídos.
	 */
	public Long deleteFeedbackPeriodico(@Valid FeedbackPeriodicoForm form) {
		return this.feedbackPeriodicoRepo.deleteByQueryAndUsuario(form.getQuery(), this.usuarioService.getUsuarioLogado());
	}

	/**
	 * Remove um feedback de EVENTO do usuário logado.
	 *
	 * @param form Form contendo a query a ser removida.
	 * @return O número de registros excluídos.
	 */
	public Long deleteFeedbackEvento(@Valid FeedbackEventoForm form) {
		return this.feedbackEventoRepo.deleteByQueryAndUsuario(form.getQuery(), this.usuarioService.getUsuarioLogado());
	}
}
