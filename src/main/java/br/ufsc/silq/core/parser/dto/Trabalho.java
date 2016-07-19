package br.ufsc.silq.core.parser.dto;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Trabalho implements Comparable<Trabalho> {
	private final String titulo;
	private final Integer ano;
	private final String tituloVeiculo;

	@Setter(value = AccessLevel.PROTECTED)
	private TreeSet<Conceito> conceitos = new TreeSet<>();

	@Override
	public int compareTo(Trabalho o) {
		return -this.getAno().compareTo(o.getAno());
	}

	/**
	 * Adiciona um conceito ao trabalho.
	 *
	 * @param conceito O {@link Conceito} a ser adicionado.
	 */
	public void addConceito(Conceito conceito) {
		this.conceitos.add(conceito);
	}

	/**
	 * Adiciona uma coleção de conceitos ao trabalho.
	 *
	 * @param conceitos A coleção de {@link Conceito} a ser adicionada.
	 */
	public void addConceitos(Collection<Conceito> conceitos) {
		this.conceitos.addAll(conceitos);
	}

	/**
	 * Checa se o trabalho tem pelo menos um conceito.
	 *
	 * @return Verdadeiro se o trabalho tiver ao menos um conceito.
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
		try {
			return this.conceitos.first();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
}
