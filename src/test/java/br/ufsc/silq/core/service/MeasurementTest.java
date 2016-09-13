package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.FeedbackEventoRepository;
import br.ufsc.silq.core.service.SimilarityService.TipoAvaliacao;
import br.ufsc.silq.test.WebContextTest;
import lombok.Data;

/**
 * Teste da medição de Precision e Recall do algoritmo de avaliação do sistema.
 */
public class MeasurementTest extends WebContextTest {

	@Inject
	SimilarityService similarityService;

	@Inject
	FeedbackService feedbackService;

	@Inject
	FeedbackEventoRepository feedbackEventoRepo;

	private Usuario usuarioLogado;

	@Before
	public void setUp() {
		this.usuarioLogado = this.loginUser();
	}

	@Test
	public void testPrecisionAndRecall() {
		this.feedbackService.sugerirMatchingEvento(new FeedbackEventoForm(2L, "3d user interfaces conference", 2010));

		for (float threshold = 0.1f; threshold <= 1.0; threshold += 0.1) {
			this.measure(this.usuarioLogado, new NivelSimilaridade(threshold));
		}
	}

	public void measure(Usuario usuario, NivelSimilaridade threshold) {
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
				e.printStackTrace();
			}
		});

		System.out.println(result);
	}

	@Data
	public static class MeasurementResult {
		private final NivelSimilaridade threshold;
		private List<Boolean> matches = new ArrayList<>();
		private List<Double> precisions = new ArrayList<>();
		private List<Double> recalls = new ArrayList<>();

		public void addResult(Boolean match, Double precision, Double recall) {
			this.matches.add(match);
			this.precisions.add(precision);
			this.recalls.add(recall);
		}

		public float threshold() {
			return this.threshold.getValue();
		}

		public int size() {
			return this.precisions.size();
		}

		public double match() {
			double numberOfMatches = this.matches.stream().filter(m -> m.booleanValue()).collect(Collectors.toList()).size();
			return numberOfMatches / this.size();
		}

		public double precision() {
			double sum = this.precisions.stream().mapToDouble(Double::doubleValue).sum();
			return sum / this.size();
		}

		public double recall() {
			double sum = this.recalls.stream().mapToDouble(Double::doubleValue).sum();
			return sum / this.size();
		}

		@Override
		public String toString() {
			return "MeasurementResult[size=" + this.size()
					+ ",threshold=" + this.threshold()
					+ ",match=" + this.match()
					+ ",precision=" + this.precision()
					+ ",recall=" + this.recall()
					+ "]";
		}

		public void debug() {
			System.out.println(this.toString());
			System.out.println("Matches: " + this.matches);
			System.out.println("Precisions: " + this.precisions);
			System.out.println("Recalls: " + this.recalls);
		}
	}
}
