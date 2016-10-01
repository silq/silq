package br.ufsc.silq.core.data.measure;

import br.ufsc.silq.core.data.NivelSimilaridade;
import lombok.Data;

/**
 * Um resultado de avaliação do sistema.
 */
@Data
public class MeasureResult {
	private final MeasureList noFeedback;
	private final MeasureList withFeedback;
	private final Integer size;
	private final NivelSimilaridade threshold;

	public float getThreshold() {
		return this.threshold.getValue();
	}
}
