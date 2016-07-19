package br.ufsc.silq.core.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Artigo extends Trabalho {
	private final String issn;

	public Artigo(String titulo, Integer ano, String tituloVeiculo, String issn) {
		super(titulo, ano, tituloVeiculo);
		this.issn = issn;
	}

	public String getIssn() {
		return this.issn.length() == 8 ? this.issn.substring(0, 4) + "-" + this.issn.substring(4, 8) : "-";
	}
}
