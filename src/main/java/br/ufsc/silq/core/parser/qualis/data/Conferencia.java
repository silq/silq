package br.ufsc.silq.core.parser.qualis.data;

public class Conferencia {

	public String sigla, nome, indiceH, estrato;

	public Conferencia() {

	}

	public Conferencia(String sigla, String nome, String indiceH, String estrato) {
		this.sigla = sigla;
		this.nome = nome;
		this.estrato = estrato;
		this.indiceH = indiceH;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndiceH() {
		return indiceH;
	}

	public void setIndiceH(String indiceH) {
		this.indiceH = indiceH;
	}

	public String getEstrato() {
		return estrato;
	}

	public void setEstrato(String estrato) {
		this.estrato = estrato;
	}

}
