package br.ufsc.silq.core.dto.parser.struct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

		info += "\nNome do Artigo: " + nomeArtigo;
		info += "\nAno: " + ano;
		info += "\nTítulo do Periódico: " + tituloPeriodico;
		info += "\nISSN: " + issn;
		info += "\nConceitos: " + conceitos.toString() + ";";

		return info;
	}

	public String getNomeArtigo() {
		return nomeArtigo;
	}

	public void setNomeArtigo(String nomeArtigo) {
		this.nomeArtigo = nomeArtigo;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getTituloPeriodico() {
		return tituloPeriodico;
	}

	public void setTituloPeriodico(String tituloPeriodico) {
		this.tituloPeriodico = tituloPeriodico;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = (issn.length() == 8 ? (issn.substring(0, 4) + "-" + issn.substring(4, 8)) : "-");
	}

	public List<Conceito> getConceitos() {
		return conceitos;
	}

	public void setConceitos(List<Conceito> conceitos) {
		this.conceitos = conceitos;
	}

	@Override
	public int compareTo(Artigo o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
