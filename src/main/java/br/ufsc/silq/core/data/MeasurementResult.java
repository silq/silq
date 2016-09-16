package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.ufsc.silq.core.service.MeasurementService;
import lombok.RequiredArgsConstructor;

/**
 * Resultado de uma avaliação de precisão/revocação do algoritmo de avaliação do sistema, realizado por {@link MeasurementService}.
 */
@RequiredArgsConstructor
public class MeasurementResult {
	private final NivelSimilaridade threshold;
	private List<Boolean> matches = new ArrayList<>();
	private List<Double> precisions = new ArrayList<>();
	private List<Double> recalls = new ArrayList<>();

	public void addResult(Boolean match, Double precision, Double recall) {
		this.matches.add(match);
		this.precisions.add(precision);
		this.recalls.add(recall);
	}

	public float getThreshold() {
		return this.threshold.getValue();
	}

	public int getSize() {
		return this.precisions.size();
	}

	public double getMatch() {
		double numberOfMatches = this.matches.stream().filter(m -> m.booleanValue()).collect(Collectors.toList()).size();
		return numberOfMatches / this.getSize();
	}

	public double getPrecision() {
		double sum = this.precisions.stream().mapToDouble(Double::doubleValue).sum();
		return sum / this.getSize();
	}

	public double getRecall() {
		double sum = this.recalls.stream().mapToDouble(Double::doubleValue).sum();
		return sum / this.getSize();
	}

	@Override
	public String toString() {
		return "MeasurementResult[size=" + this.getSize()
				+ ",threshold=" + this.getThreshold()
				+ ",match=" + this.getMatch()
				+ ",precision=" + this.getPrecision()
				+ ",recall=" + this.getRecall()
				+ "]";
	}

	public void debug() {
		System.out.println(this.toString());
		System.out.println("Matches: " + this.matches);
		System.out.println("Precisions: " + this.precisions);
		System.out.println("Recalls: " + this.recalls);
	}
}
