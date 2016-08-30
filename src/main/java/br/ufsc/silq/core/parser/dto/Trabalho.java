package br.ufsc.silq.core.parser.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.data.Conceito;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Trabalho implements Comparable<Trabalho> {
	protected final String titulo;
	protected final Integer ano;
	protected final String tituloVeiculo;

	@Setter(value = AccessLevel.PROTECTED)
	protected List<Conceito> conceitos = new ArrayList<>();

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
	public Conceito getConceitoMaisSimilar() {
		return this.conceitos.stream().max((c1, c2) -> c1.compareTo(c2)).orElse(null);
	}

	/**
	 * Cria uma cópia exata do trabalho.
	 *
	 * @return Um novo objeto {@link Trabalho} contendo valores idênticos ao original.
	 */
	public Trabalho copy() {
		Trabalho novo = new Trabalho(this.titulo, this.ano, this.tituloVeiculo);
		novo.setConceitos(new ArrayList<>(this.getConceitos()));
		return novo;
	}
}
