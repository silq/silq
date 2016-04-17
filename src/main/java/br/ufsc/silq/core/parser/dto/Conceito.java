package br.ufsc.silq.core.parser.dto;

import lombok.Data;

@Data
public class Conceito implements Comparable<Conceito> {

	private final String tituloVeiculo;
	private final String conceito;
	private final String similaridade;

	@Override
	public int compareTo(Conceito o) {
		return o.getSimilaridade().compareTo(this.getSimilaridade());
	}

}
