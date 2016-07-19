package br.ufsc.silq.core.parser.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Artigo implements Comparable<Artigo> {
	private final String titulo;
	private final Integer ano;
	private final String tituloVeiculo;
	private final String issn;
	private List<Conceito> conceitos = new ArrayList<>();

	public String getIssn() {
		return this.issn.length() == 8 ? this.issn.substring(0, 4) + "-" + this.issn.substring(4, 8) : "-";
	}

	@Override
	public int compareTo(Artigo o) {
		return -this.getAno().compareTo(o.getAno());
	}

	/**
	 * Checa se o artigo tem pelo menos um conceito.
	 *
	 * @return Verdadeiro se o artigo tiver ao menos um conceito.
	 */
	@JsonIgnore
	public boolean hasConceito() {
		return !this.conceitos.isEmpty();
	}

	/**
	 * Retorna o conceito com maior similaridade atribuído a este artigo.
	 *
	 * @return O {@link Conceito} com maior valor de {@link Conceito#getSimilaridade()} ou null caso não
	 *         haja conceito atribuído a este artigo.
	 */
	@JsonIgnore
	public Conceito getConceito() {
		Collections.sort(this.conceitos); // TODO (bonetti)
		return this.hasConceito() ? this.conceitos.get(0) : null;
	}
}
