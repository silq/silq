package br.ufsc.silq.core.data;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import br.ufsc.silq.core.data.measure.MeasureEntry;
import br.ufsc.silq.core.data.measure.MeasureList;

public class MeasurementResultTest {

	@Test
	public void test() {
		MeasureList result = new MeasureList(NivelSimilaridade.NORMAL);
		Assertions.assertThat(result.getThreshold()).isEqualTo(NivelSimilaridade.NORMAL.getValue());

		result.addResult(new MeasureEntry(true, 1.0));
		Assertions.assertThat(result.getSize()).isEqualTo(1);
		Assertions.assertThat(result.getMatch()).isEqualTo(1.0);
		Assertions.assertThat(result.getMeanReciprocralRank()).isEqualTo(1.0);

		result.addResult(new MeasureEntry(false, 0.0));
		Assertions.assertThat(result.getSize()).isEqualTo(2);
		Assertions.assertThat(result.getMatch()).isEqualTo(0.5);
		Assertions.assertThat(result.getMeanReciprocralRank()).isEqualTo(0.5);

		result.addResult(new MeasureEntry(false, null));
		result.addResult(new MeasureEntry(false, null));
		Assertions.assertThat(result.getSize()).isEqualTo(4);
		Assertions.assertThat(result.getMatch()).isEqualTo(0.25);
		Assertions.assertThat(result.getMeanReciprocralRank()).isEqualTo(0.5);
	}
}
