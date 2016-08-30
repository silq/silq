package br.ufsc.silq.core.parser.dto;

import br.ufsc.silq.core.data.NivelSimilaridade;
import lombok.Data;

@Data
public class Conceito implements Comparable<Conceito> {

	private final Long id;
	private final String tituloVeiculo;
	private final String conceito;
	private final NivelSimilaridade similaridade;
	private final Integer ano;

	public Float getSimilaridade() {
		return this.similaridade.getValue();
	}

	@Override
	public int compareTo(Conceito o) {
		return this.getSimilaridade().compareTo(o.getSimilaridade());
	}
}
