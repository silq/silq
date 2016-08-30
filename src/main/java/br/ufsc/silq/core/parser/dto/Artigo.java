package br.ufsc.silq.core.parser.dto;

import br.ufsc.silq.core.data.Conceituavel;
import lombok.Data;

@Data
public class Artigo implements Comparable<Artigo>, Conceituavel {
	private final String titulo;
	private final Integer ano;
	private final String tituloVeiculo;
	private final String issn;

	public String getIssn() {
		return this.issn.length() == 8 ? this.issn.substring(0, 4) + "-" + this.issn.substring(4, 8) : "-";
	}

	@Override
	public int compareTo(Artigo o) {
		return -this.getAno().compareTo(o.getAno());
	}
}
