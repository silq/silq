package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.MeasurementResult;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.test.WebContextTest;

public class MeasurementServiceTest extends WebContextTest {

	@Inject
	MeasurementService measurementService;

	@Inject
	FeedbackService feedbackService;

	private Usuario usuarioLogado;

	@Before
	public void setUp() {
		this.usuarioLogado = this.loginUser();
	}

	@Test
	public void testPrecisionAndRecall() {
		this.feedbackService.sugerirMatchingEvento(new FeedbackEventoForm(2L, "3d user interfaces conference", 2010));

		for (float threshold = 0.1f; threshold <= 1.0; threshold += 0.1) {
			MeasurementResult result = this.measurementService.measure(this.usuarioLogado, new NivelSimilaridade(threshold));
			System.out.println(result);
			// result.debug();
		}
	}
}
