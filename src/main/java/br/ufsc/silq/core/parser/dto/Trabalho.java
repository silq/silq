package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Trabalho implements Comparable<Trabalho>, Serializable {

	private static final long serialVersionUID = -2890991030815261275L;

	private Integer anoPublicacao;
	private String tituloTrabalho;
	private String nomeEvento;
	private List<Conceito> conceitos;

	public Trabalho() {
		this.anoPublicacao = new Integer(0);
		this.tituloTrabalho = "";
		this.nomeEvento = "";
		this.conceitos = new ArrayList<Conceito>();
	}

	@Override
	public String toString() {
		String info = "";

		info += "\nTítulo: " + this.tituloTrabalho + "; ";
		info += "\nAno de publicação: " + this.anoPublicacao + "; ";
		info += "\nNome do Evento: " + this.nomeEvento + ";";
		info += "\nConceitos: " + this.conceitos.toString() + ";";

		return info;
	}

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAnoPublicacao().compareTo(o.getAnoPublicacao());
	}
}
