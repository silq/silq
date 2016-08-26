package br.ufsc.silq.core.data;

import lombok.Data;

@Data
public class SimilarityResult<T> implements Comparable<SimilarityResult<T>> {
	private final T resultado;
	private final NivelSimilaridade similaridade;

	@Override
	public int compareTo(SimilarityResult<T> o) {
		// Resultados com maior similaridade são ordenados por primeiro
		return this.getSimilaridade().compareTo(o.getSimilaridade());
	}
}
