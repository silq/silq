package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import br.ufsc.silq.core.service.MeasurementService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Resultado de uma avaliação de precisão/revocação do algoritmo de avaliação do sistema, realizado por {@link MeasurementService}.
 */
@RequiredArgsConstructor
public class MeasurementResult {
	private final NivelSimilaridade threshold;

	/**
	 * Fazemos todas as estruturas de dados serem thread-safe para processar os resultados de forma paralela
	 * em {@link MeasurementService#measure}.
	 */
	private List<MeasureEntry> measures = Collections.synchronizedList(new ArrayList<>());
	private AtomicInteger size = new AtomicInteger(0);

	public void addResult(MeasureEntry measure) {
		this.size.incrementAndGet();
		this.measures.add(measure);
	}

	public int getSize() {
		return this.size.get();
	}

	public float getThreshold() {
		return this.threshold.getValue();
	}

	public double getMatch() {
		double numberOfMatches = this.measures.stream()
				.map(m -> m.getMatch())
				.filter(m -> m.booleanValue())
				.collect(Collectors.toList()).size();
		return numberOfMatches / this.measures.size();
	}

	public double getMeanReciprocralRank() {
		return this.measures.stream()
				.map(m -> m.getReciprocralRank())
				.filter(rr -> rr != null)
				.mapToDouble(Double::doubleValue)
				.average()
				.orElse(0.0);
	}

	@Override
	public String toString() {
		return "MeasurementResult[size=" + this.getSize()
				+ ",threshold=" + this.getThreshold()
				+ ",match=" + this.getMatch()
				+ ",MRR=" + this.getMeanReciprocralRank()
				+ "]";
	}

	public void debug() {
		System.out.println(this.toString());
		System.out.println("Measures: " + this.measures);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MeasureEntry {
		private Boolean match;
		private Double reciprocralRank;
	}
}
