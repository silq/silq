package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

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

/**
 * Realiza medições de valores de validação do desempenho e acurácia do algoritmo de avaliação do sistema, incluindo Precisão e Revocação.
 */
@Service
@Transactional
@Slf4j
public class MeasurementService {

	@Inject
	SimilarityService similarityService;

	@Inject
	FeedbackEventoRepository feedbackEventoRepo;

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

		feedbacksEventos.forEach(feedback -> {
			try {
				double precision = 0;
				double recall = 0;
				boolean match = false;

				QualisEvento eventoFeedback = feedback.getEvento();

				avaliarForm.setArea(eventoFeedback.getAreaAvaliacao()); // TODO: se área do feedback for diferente da área do evento?

				Trabalho trabalho = new Trabalho("", feedback.getAno(), feedback.getQuery()); // O Título do trabalho é ignorado pela avaliação

				List<Conceito> conceitos = this.similarityService.getConceitos(trabalho, avaliarForm, TipoAvaliacao.EVENTO);
				if (conceitos.isEmpty()) {
					// Sistema não encontrou nada
					// Todos os valores ficam 0!
				} else {
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
			} catch (SQLException e) {
				log.error("Erro ao realizar medição", e);
			}
		});

		return result;
	}

}
