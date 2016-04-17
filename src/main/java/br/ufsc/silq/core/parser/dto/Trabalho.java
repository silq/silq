package br.ufsc.silq.core.parser.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Trabalho implements Comparable<Trabalho> {
	private final String titulo;
	private final Integer ano;
	private final String tituloVeiculo;
	private List<Conceito> conceitos = new ArrayList<>();

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
