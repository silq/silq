package br.ufsc.silq.core.parser.dto;

import br.ufsc.silq.core.data.Conceituavel;
import lombok.Data;

@Data
public class Trabalho implements Comparable<Trabalho>, Conceituavel {
	private final String titulo;
	private final Integer ano;
	private final String tituloVeiculo;

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
