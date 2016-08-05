package br.ufsc.silq.core.parser.dto;

import java.util.ArrayList;

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

	/**
	 * Cria uma cópia exata do artigo.
	 *
	 * @return Um novo objeto {@link Artigo} contendo valores idênticos ao original.
	 */
	@Override
	public Artigo copy() {
		Artigo novo = new Artigo(this.titulo, this.ano, this.tituloVeiculo, this.issn);
		novo.setConceitos(new ArrayList<>(this.getConceitos()));
		return novo;
	}
}
