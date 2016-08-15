package br.ufsc.silq.core.data;

import lombok.Data;

@Data
public class SimilarityResult<T> {
	private final T resultado;
	private final NivelSimilaridade similaridade;
}
