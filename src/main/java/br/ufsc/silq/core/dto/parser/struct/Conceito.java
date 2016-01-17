package br.ufsc.silq.core.dto.parser.struct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Conceito implements Comparable<Conceito>{

	private String nomeEvento;
	private String conceito;
	private String similaridade;

	public Conceito() {
	}

	@Override
	public String toString() {
		String info = "";

		info += this.conceito;

		if (this.similaridade != null && this.similaridade.length() > 4) {
			this.similaridade = this.similaridade.substring(0, 4);
		}

		info += " (" + this.similaridade + ")";

		return info;
	}

	@Override
	public int compareTo(Conceito o) {
		return o.getSimilaridade().compareTo(this.getSimilaridade());
	}

}
