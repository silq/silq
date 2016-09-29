package br.ufsc.silq.core.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.SimilarityResult;
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
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		FeedbackEvento feedback = this.feedbackEventoRepo.findOneByQueryAndUsuarioAndValidation(form.getQuery(), usuario, false)
				.orElse(new FeedbackEvento());

		if (!form.isNegativo()) {
			QualisEvento evento = this.eventoRepo.findOneById(form.getEventoId())
					.orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));
			feedback.setEvento(evento);
		} else {
			feedback.setEvento(null);
		}

		feedback.setQuery(form.getQuery());
		feedback.setAno(form.getAno());
		feedback.setUsuario(usuario);
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
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		FeedbackPeriodico feedback = this.feedbackPeriodicoRepo.findOneByQueryAndUsuarioAndValidation(form.getQuery(), usuario, false)
				.orElse(new FeedbackPeriodico());

		if (!form.isNegativo()) {
			QualisPeriodico periodico = this.periodicoRepo.findOneById(form.getPeriodicoId())
					.orElseThrow(() -> new IllegalArgumentException("Periódico não encontrado"));

			feedback.setPeriodico(periodico);
		} else {
			feedback.setPeriodico(null);
		}

		feedback.setQuery(form.getQuery());
		feedback.setAno(form.getAno());
		feedback.setUsuario(usuario);
		feedback.setDate(new Date());
		this.feedbackPeriodicoRepo.save(feedback);
		return feedback;
	}

	public Optional<SimilarityResult<FeedbackPeriodico>> getFeedbackPeriodico(String query, Usuario usuario) {
		return Optional.ofNullable(this.similarityService.searchFeedback(FeedbackPeriodico.class, query, usuario));
	}

	public Optional<SimilarityResult<FeedbackEvento>> getFeedbackEvento(String query, Usuario usuario) {
		return Optional.ofNullable(this.similarityService.searchFeedback(FeedbackEvento.class, query, usuario));
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
		Optional<SimilarityResult<FeedbackEvento>> feedback = this.getFeedbackEvento(query, usuario);
		return feedback.map(this::feedbackEventoToConceito);
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
		Optional<SimilarityResult<FeedbackPeriodico>> feedback = this.getFeedbackPeriodico(query, usuario);
		return feedback.map(this::feedbackPeriodicoToConceito);
	}

	public Conceito feedbackEventoToConceito(SimilarityResult<FeedbackEvento> result) {
		FeedbackEvento feedback = result.getResultado();
		QualisEvento evento = feedback.getEvento();
		Conceito conceito = new Conceito(evento.getId(), evento.getTitulo(), evento.getEstrato(),
				result.getSimilaridade(), evento.getAno());
		conceito.setSiglaVeiculo(evento.getSigla());
		conceito.setFeedback(true);
		return conceito;
	}

	public Conceito feedbackPeriodicoToConceito(SimilarityResult<FeedbackPeriodico> result) {
		FeedbackPeriodico feedback = result.getResultado();
		QualisPeriodico periodico = feedback.getPeriodico();
		Conceito conceito = new Conceito(periodico.getId(), periodico.getTitulo(), periodico.getEstrato(),
				result.getSimilaridade(), periodico.getAno());
		conceito.setFeedback(true);
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
