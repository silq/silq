package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.MeasurementResult;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.FeedbackEventoRepository;
import br.ufsc.silq.core.service.SimilarityService.TipoAvaliacao;
import lombok.extern.slf4j.Slf4j;

/**
 * Realiza medições de valores de validação do desempenho e acurácia do algoritmo de avaliação do sistema, incluindo Precisão e Revocação.
 */
@Service
@Transactional
@Slf4j
public class MeasurementService {

	@Inject
	private SimilarityService similarityService;

	@Inject
	private FeedbackEventoRepository feedbackEventoRepo;

	@Inject
	private UsuarioService usuarioService;

	/**
	 * Realiza uma medição de acurácia do algoritmo de avaliação do sistema utilizando como "valores verdade" os feedbacks dados pelo usuário indicado.
	 *
	 * @param usuario Utiliza os feedbacks deste usuário para realizar as medições.
	 * @param threshold Nível de similaridade do algorimo de avaliação a ser utilizado.
	 * @return O resultado da medição, incluindo Precisão, Revocação e Número correto de matches feitos pelo sistema.
	 */
	public MeasurementResult measure(Usuario usuario, NivelSimilaridade threshold) {
		List<FeedbackEvento> feedbacksEventos = this.feedbackEventoRepo.findAllByUsuario(usuario);

		AvaliarForm avaliarForm = new AvaliarForm();
		avaliarForm.setMaxConceitos(100);
		avaliarForm.setAvaliarArtigoPorSimilaridade(false);
		avaliarForm.setUsarFeedback(false);
		avaliarForm.setNivelSimilaridade(threshold);

		MeasurementResult result = new MeasurementResult(threshold);
		feedbacksEventos.forEach(feedback -> this.measureFeedback(feedback, result, avaliarForm));
		return result;
	}

	/**
	 * Realiza uma série de medições, da mesma forma que {@link #measure(Usuario, NivelSimilaridade)}, variando
	 * o threshold dado como parâmetro.
	 * Utiliza os feedbacks do usuário atual como resultados de controle.
	 *
	 * @param initialThreshold Valor inicial de threshold a ser utilizado
	 * @param finalThreshold Valor final de threshold a ser utilizado.
	 * @param step O step a ser usado para incremento do threshold.
	 * @return Uma lista dos resultados das medições, incluindo Precisão, Revocação e Número correto de matches feitos pelo sistema.
	 */
	public List<MeasurementResult> measure(float initialThreshold, float finalThreshold, float step) {
		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();
		List<MeasurementResult> results = new ArrayList<>();

		int i = 0;
		float threshold = initialThreshold;

		while (threshold <= finalThreshold) {
			MeasurementResult measure = this.measure(usuarioLogado, new NivelSimilaridade(threshold));
			results.add(measure);
			log.debug("Measurement for threshold {}: {}", threshold, measure);

			i++;
			threshold = initialThreshold + step * i;
		}
		return results;
	}

	private void measureFeedback(FeedbackEvento feedback, MeasurementResult result, AvaliarForm avaliarForm) {
		double precision = 0;
		double recall = 0;
		boolean match = false;

		QualisEvento eventoFeedback = feedback.getEvento();

		if (eventoFeedback != null) {
			avaliarForm.setArea(eventoFeedback.getAreaAvaliacao()); // TODO: se área do feedback for diferente da área do evento?
		}

		Trabalho trabalho = new Trabalho("", feedback.getAno(), feedback.getQuery()); // O Título do trabalho é ignorado pela avaliação

		List<Conceito> conceitos;
		try {
			conceitos = this.similarityService.getConceitos(trabalho, avaliarForm, TipoAvaliacao.EVENTO);
		} catch (SQLException e) {
			log.error("Erro ao obter conceitos do feedback: " + feedback, e);
			return;
		}

		if (eventoFeedback == null) {
			// Caso seja um feedback negativo
			if (!conceitos.isEmpty()) {
				// e o sistema encontrou conceitos (erroneamente)
				result.addResult(false, 0D, 0D); // TODO: a revocação aqui é 0 mesmo?
			} else {
				// e o sistema NÃO encontrou conceitos (corretamente)
				result.addResult(true, 1D, 1D);
			}
			return;
		}

		if (!conceitos.isEmpty()) {
			Conceito chosen = conceitos.get(0);
			if (chosen.getId().equals(eventoFeedback.getId())) {
				match = true;
			}

			List<Long> ids = conceitos.stream().map(c -> c.getId()).collect(Collectors.toList());
			if (ids.contains(eventoFeedback.getId())) {
				// Sistema retornou o resultado real!
				precision = 1.0 / ids.size();
				recall = 1.0;
			}
		}

		result.addResult(match, precision, recall);
	}

}
