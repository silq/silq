package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

import br.ufsc.silq.core.service.MeasurementService;

/**
 * Resultado de uma avaliação de precisão/revocação do algoritmo de avaliação do sistema, realizado por {@link MeasurementService}.
 */
@Data
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
		return "MeasurementResult[size=" + this.size() + ",threshold=" + this.threshold() + ",match=" + this.match() + ",precision=" + this.precision() + ",recall=" + this.recall()
				+ "]";
	}

	public void debug() {
		System.out.println(this.toString());
		System.out.println("Matches: " + this.matches);
		System.out.println("Precisions: " + this.precisions);
		System.out.println("Recalls: " + this.recalls);
	}
}