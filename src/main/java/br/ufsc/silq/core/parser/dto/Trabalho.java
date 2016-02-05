package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

		info += "\nTítulo: " + tituloTrabalho + "; ";
		info += "\nAno de publicação: " + anoPublicacao + "; ";
		info += "\nNome do Evento: " + nomeEvento + ";";
		info += "\nConceitos: " + conceitos.toString() + ";";

		return info;
	}

	public String getTituloTrabalho() {
		return tituloTrabalho;
	}

	public void setTituloTrabalho(String tituloTrabalho) {
		this.tituloTrabalho = tituloTrabalho;
	}

	public String getNomeEvento() {
		return nomeEvento;
	}

	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}

	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(Integer anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public List<Conceito> getConceitos() {
		return conceitos;
	}

	public void setConceitos(List<Conceito> conceitos) {
		this.conceitos = conceitos;
	}

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAnoPublicacao().compareTo(o.getAnoPublicacao());
	}
}
