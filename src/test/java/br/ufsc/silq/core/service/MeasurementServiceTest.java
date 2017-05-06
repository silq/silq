package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.measure.MeasureList;
import br.ufsc.silq.core.data.measure.MeasureResult;
import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.FeedbackEventoRepository;
import br.ufsc.silq.test.WebContextTest;

public class MeasurementServiceTest extends WebContextTest {

	@Inject
	MeasurementService measurementService;

	@Inject
	FeedbackService feedbackService;

	@Inject
	FeedbackEventoRepository feedbackEventoRepo;

	private Usuario usuarioLogado;

	@Before
	public void setUp() {
		this.usuarioLogado = this.loginUser();
	}

	private void saveFeedback(FeedbackEventoForm form) {
		FeedbackEvento feedback = this.feedbackService.sugerirMatchingEvento(form);
		feedback.setValidation(true); // Marca como sendo um feedback de validação, para ser considerado pela medição
		this.feedbackEventoRepo.save(feedback);
	}

	@Test
	public void testMeasureFeedbackExactMatch() {
		this.saveFeedback(new FeedbackEventoForm(2L, "symposium 3d user interfaces", 2010));

		MeasureResult result = this.measurementService.measure(this.usuarioLogado, NivelSimilaridade.NORMAL, 100);
		MeasureList noFeedback = result.getNoFeedback();
		MeasureList withFeedback = result.getWithFeedback();

		Assertions.assertThat(result.getSize()).isEqualTo(1);
		Assertions.assertThat(result.getThreshold()).isEqualTo(NivelSimilaridade.NORMAL.getValue());
		Assertions.assertThat(noFeedback.getMatch()).isEqualTo(1);
		Assertions.assertThat(noFeedback.getMeanReciprocralRank()).isEqualTo(1.0);
	}

	@Test
	public void testMeasure() {
		this.saveFeedback(new FeedbackEventoForm(2L, "3d user interfaces conference", 2010));

		MeasureResult result = this.measurementService.measure(this.usuarioLogado, NivelSimilaridade.BAIXO, 100);
		MeasureList noFeedback = result.getNoFeedback();
		MeasureList withFeedback = result.getWithFeedback();

		Assertions.assertThat(result.getSize()).isEqualTo(1);
		Assertions.assertThat(result.getThreshold()).isEqualTo(NivelSimilaridade.BAIXO.getValue());
		Assertions.assertThat(noFeedback.getMatch()).isEqualTo(0);
		Assertions.assertThat(noFeedback.getMeanReciprocralRank()).isCloseTo(0.25, Offset.offset(0.01));
	}

	@Test
	public void testMeasureFeedbackNegativo() {
		this.saveFeedback(new FeedbackEventoForm(null, "symposium 3d user interfaces", 2010));

		MeasureResult result = this.measurementService.measure(this.usuarioLogado, NivelSimilaridade.BAIXO, 100);
		MeasureList noFeedback = result.getNoFeedback();
		MeasureList withFeedback = result.getWithFeedback();

		Assertions.assertThat(result.getSize()).isEqualTo(1);
		Assertions.assertThat(result.getThreshold()).isEqualTo(NivelSimilaridade.BAIXO.getValue());
		Assertions.assertThat(noFeedback.getMatch()).isEqualTo(0);
		Assertions.assertThat(noFeedback.getMeanReciprocralRank()).isEqualTo(0.0);
	}
}
