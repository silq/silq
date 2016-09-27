package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.MeasurementResult;
import br.ufsc.silq.core.data.MeasurementResult.MeasureEntry;
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
	 * @param limit O número máximo de feedbacks a serem considerados na avaliação.
	 * @return O resultado da medição, incluindo Precisão, Revocação e Número correto de matches feitos pelo sistema.
	 */
	public MeasurementResult measure(Usuario usuario, NivelSimilaridade threshold, int limit) {
		List<FeedbackEvento> feedbacksEventos = this.feedbackEventoRepo.findAllByUsuario(usuario);

		AvaliarForm avaliarForm = new AvaliarForm();
		avaliarForm.setMaxConceitos(100);
		avaliarForm.setAvaliarArtigoPorSimilaridade(false);
		avaliarForm.setUsarFeedback(false);
		avaliarForm.setNivelSimilaridade(threshold);

		MeasurementResult result = new MeasurementResult(threshold);
		feedbacksEventos.parallelStream().limit(limit).forEach(feedback -> {
			MeasureEntry measure = this.measureFeedback(feedback, avaliarForm);
			if (measure != null) {
				result.addResult(measure);
			}
		});
		return result;
	}

	/**
	 * Realiza uma série de medições, da mesma forma que {@link #measure(Usuario, NivelSimilaridade, int)}, variando
	 * o threshold dado como parâmetro.
	 * Utiliza os feedbacks do usuário atual como resultados de controle.
	 *
	 * @param initialThreshold Valor inicial de threshold a ser utilizado
	 * @param finalThreshold Valor final de threshold a ser utilizado.
	 * @param step O step a ser usado para incremento do threshold.
	 * @param limit O número máximo de feedbacks a ser considerado na avaliação.
	 * @return Uma lista dos resultados das medições, incluindo Precisão, Revocação e Número correto de matches feitos pelo sistema.
	 */
	public List<MeasurementResult> measure(float initialThreshold, float finalThreshold, float step, int limit) {
		if (initialThreshold < 0 || finalThreshold > 1 || initialThreshold > finalThreshold) {
			throw new IllegalArgumentException("Parâmetros inválidos");
		}

		StopWatch watch = new StopWatch();
		watch.start();
		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();
		List<MeasurementResult> results = new ArrayList<>();

		int i = 0;
		float threshold = initialThreshold;

		while (threshold <= finalThreshold) {
			MeasurementResult measure = this.measure(usuarioLogado, new NivelSimilaridade(threshold), limit);
			results.add(measure);
			log.debug("Measurement for threshold {}: {}", threshold, measure);

			i++;
			threshold = initialThreshold + step * i;
		}
		watch.stop();
		log.info("Measure finished in {}s", watch.getTotalTimeMillis() / 1000.0);
		return results;
	}

	private MeasureEntry measureFeedback(FeedbackEvento feedback, AvaliarForm avaliarForm) {
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
			return null;
		}

		if (eventoFeedback == null) {
			// Caso seja um feedback negativo: não existe um resultado real
			if (!conceitos.isEmpty()) {
				// e o sistema encontrou conceitos (erroneamente)
				return new MeasureEntry(false, null);
			} else {
				// e o sistema NÃO encontrou conceitos (corretamente)
				return new MeasureEntry(true, null);
			}
		}

		// Existe um resultado real:
		MeasureEntry measure = new MeasureEntry();

		List<Long> ids = conceitos.stream().map(c -> c.getId()).collect(Collectors.toList());
		int rank = ids.indexOf(eventoFeedback.getId()) + 1;

		measure.setMatch(rank == 1);
		measure.setReciprocralRank(rank < 1 ? 0.0 : 1.0 / rank);
		return measure;
	}

}
