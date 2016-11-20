package br.ufsc.silq.core.parser.dto;

import br.ufsc.silq.core.data.Conceituavel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Trabalho implements Comparable<Trabalho>, Conceituavel {
	private final String titulo;
	private final Integer ano;
	private final String tituloVeiculo;
	private final NaturezaPublicacao natureza;

	public Trabalho(String titulo, Integer ano, String tituloVeiculo) {
		this(titulo, ano, tituloVeiculo, NaturezaPublicacao.COMPLETO);
	}

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
