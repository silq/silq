package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Trabalho implements Comparable<Trabalho>, Serializable {

	private static final long serialVersionUID = -2890991030815261275L;

	private String titulo;
	private Integer ano;
	private String tituloVeiculo;
	private List<Conceito> conceitos;

	public Trabalho() {
		this.ano = new Integer(0);
		this.titulo = "";
		this.tituloVeiculo = "";
		this.conceitos = new ArrayList<Conceito>();
	}

	@Override
	public String toString() {
		String info = "";

		info += "\nTítulo: " + this.titulo + "; ";
		info += "\nAno de publicação: " + this.ano + "; ";
		info += "\nNome do Evento: " + this.tituloVeiculo + ";";
		info += "\nConceitos: " + this.conceitos.toString() + ";";

		return info;
	}

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
