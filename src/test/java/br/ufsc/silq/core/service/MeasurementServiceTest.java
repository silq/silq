package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
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
	public void testMeasureFeedbackExactMatch() {
		this.feedbackService.sugerirMatchingEvento(new FeedbackEventoForm(2L, "symposium 3d user interfaces", 2010));

		MeasurementResult result = this.measurementService.measure(this.usuarioLogado, NivelSimilaridade.NORMAL);
		// result.debug();

		Assertions.assertThat(result.getMatches().get(0)).isEqualTo(true);
		Assertions.assertThat(result.getPrecisions().get(0)).isEqualTo(1);
		Assertions.assertThat(result.getRecalls().get(0)).isEqualTo(1);

		Assertions.assertThat(result.size()).isEqualTo(1);
		Assertions.assertThat(result.threshold()).isEqualTo(NivelSimilaridade.NORMAL.getValue());
		Assertions.assertThat(result.match()).isEqualTo(1);
		Assertions.assertThat(result.recall()).isEqualTo(1);
		Assertions.assertThat(result.precision()).isEqualTo(1);
	}

	@Test
	public void testMeasureFeedbackExactButLessPrecision() {
		this.feedbackService.sugerirMatchingEvento(new FeedbackEventoForm(2L, "symposium 3d user interfaces", 2010));

		MeasurementResult result = this.measurementService.measure(this.usuarioLogado, NivelSimilaridade.BAIXO);
		// result.debug();

		Assertions.assertThat(result.getMatches().get(0)).isEqualTo(true);
		Assertions.assertThat(result.getPrecisions().get(0)).isEqualTo(0.5);
		Assertions.assertThat(result.getRecalls().get(0)).isEqualTo(1);

		Assertions.assertThat(result.size()).isEqualTo(1);
		Assertions.assertThat(result.threshold()).isEqualTo(NivelSimilaridade.BAIXO.getValue());
		Assertions.assertThat(result.match()).isEqualTo(1);
		Assertions.assertThat(result.recall()).isEqualTo(1);
		Assertions.assertThat(result.precision()).isEqualTo(0.5);
	}

	@Test
	public void testMeasure() {
		this.feedbackService.sugerirMatchingEvento(new FeedbackEventoForm(2L, "3d user interfaces conference", 2010));

		MeasurementResult result = this.measurementService.measure(this.usuarioLogado, NivelSimilaridade.BAIXO);
		// result.debug();

		Assertions.assertThat(result.getMatches().get(0)).isEqualTo(false);
		Assertions.assertThat(result.getPrecisions().get(0)).isCloseTo(0.3333, Offset.offset(0.001));
		Assertions.assertThat(result.getRecalls().get(0)).isEqualTo(1);

		Assertions.assertThat(result.size()).isEqualTo(1);
		Assertions.assertThat(result.threshold()).isEqualTo(NivelSimilaridade.BAIXO.getValue());
		Assertions.assertThat(result.match()).isEqualTo(0);
		Assertions.assertThat(result.recall()).isEqualTo(1);
		Assertions.assertThat(result.precision()).isCloseTo(0.3333, Offset.offset(0.001));
	}
}
