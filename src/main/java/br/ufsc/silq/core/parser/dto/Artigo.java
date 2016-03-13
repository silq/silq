package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Artigo implements Comparable<Artigo>, Serializable {

	private static final long serialVersionUID = 1265140499533405908L;

	private String nomeArtigo;
	private Integer ano;
	private String tituloPeriodico;
	private String issn;
	private List<Conceito> conceitos;

	public Artigo() {
		this.nomeArtigo = "";
		this.tituloPeriodico = "";
		this.issn = "";
		this.ano = new Integer(0);
		this.conceitos = new ArrayList<Conceito>();
		this.conceitos.add(new Conceito());
	}

	@Override
	public String toString() {
		String info = "";

		info += "\nNome do Artigo: " + this.nomeArtigo;
		info += "\nAno: " + this.ano;
		info += "\nTítulo do Periódico: " + this.tituloPeriodico;
		info += "\nISSN: " + this.issn;
		info += "\nConceitos: " + this.conceitos.toString() + ";";

		return info;
	}

	public void setIssn(String issn) {
		this.issn = issn.length() == 8 ? issn.substring(0, 4) + "-" + issn.substring(4, 8) : "-";
	}

	@Override
	public int compareTo(Artigo o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
