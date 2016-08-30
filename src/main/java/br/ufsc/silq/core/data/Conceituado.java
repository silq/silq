package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Trabalho;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Encapsula um objeto qualquer (geralmente um {@link Artigo} ou {@link Trabalho} que foi conceituado.
 *
 * @param <T> Tipo do objeto conceituado.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Conceituado<T extends Comparable<T>> implements Comparable<Conceituado<T>> {

	private final T obj;

	@Setter(value = AccessLevel.PROTECTED)
	private List<Conceito> conceitos = new ArrayList<>();

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
	 * Retorna o conceito com maior similaridade atribuído.
	 *
	 * @return O {@link Conceito} com maior valor de {@link Conceito#getSimilaridade()} ou null caso não
	 *         haja conceito atribuído.
	 */
	@JsonIgnore
	public Conceito getConceitoMaisSimilar() {
		return this.conceitos.stream().max((c1, c2) -> c1.compareTo(c2)).orElse(null);
	}

	@Override
	public int compareTo(Conceituado<T> o) {
		return this.obj.compareTo(o.obj);
	}
}
