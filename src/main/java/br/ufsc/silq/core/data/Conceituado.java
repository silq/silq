package br.ufsc.silq.core.data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Trabalho;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Encapsula um objeto qualquer (geralmente um {@link Artigo} ou {@link Trabalho} que foi conceituado.
 *
 * @param <T> Tipo do objeto conceituado.
 */
@Data
public class Conceituado<T extends Comparable<T>> implements Comparable<Conceituado<T>> {

	private final T obj;

	@Setter(value = AccessLevel.PROTECTED)
	private List<Conceito> conceitos = new SortedList<>();

	/**
	 * Marca o objeto conceituado como tendo um feedback negativo.
	 * Neste caso, todos os conceitos atribuídos a este Conceituado devem ser ignorados.
	 */
	private boolean feedbackNegativo = false;

	public Conceituado(T obj) {
		this.obj = obj;
	}

	public Conceituado(T obj, Collection<Conceito> conceitos) {
		this(obj);
		this.addConceitos(conceitos);
	}

	/**
	 * Adiciona um conceito ao trabalho.
	 * Caso o conceito já exista neste trabalho, então ele é sobrescrito caso algum destes casos seja verdadeiro:
	 * - Se o {@link NivelSimilaridade} do novo conceito for maior que o antigo;
	 * - Se o novo conceito tiver a flag {@link Conceito#isFeedback()}
	 *
	 * @param conceito O {@link Conceito} a ser adicionado.
	 */
	public void addConceito(Conceito conceito) {
		int pos;
		if ((pos = this.conceitos.indexOf(conceito)) >= 0) {
			Conceito antigoConceito = this.conceitos.get(pos);

			if (conceito.compareTo(antigoConceito) <= 0) {
				this.conceitos.remove(pos);
			} else {
				return;
			}
		}

		this.conceitos.add(conceito);
	}

	/**
	 * Adiciona uma coleção de conceitos ao trabalho.
	 *
	 * @param conceitos A coleção de {@link Conceito} a ser adicionada.
	 */
	public void addConceitos(Collection<Conceito> conceitos) {
		conceitos.forEach(this::addConceito);
	}

	/**
	 * Checa se o trabalho tem pelo menos um conceito.
	 *
	 * @return Verdadeiro se o trabalho tiver ao menos um conceito.
	 */
	@JsonIgnore
	public boolean hasConceito() {
		return !this.conceitos.isEmpty() && !this.feedbackNegativo;
	}

	/**
	 * Retorna o conceito com maior similaridade atribuído.
	 *
	 * @return O {@link Conceito} com maior valor de {@link Conceito#getSimilaridade()} ou null caso não
	 *         haja conceito atribuído.
	 */
	@JsonIgnore
	public Conceito getConceitoMaisSimilar() {
		// Se for um feedback negativo, ignora todos os conceitos atribuídos
		return this.conceitos.isEmpty() || this.feedbackNegativo ? null : this.conceitos.get(0);
	}

	@Override
	public int compareTo(Conceituado<T> o) {
		return this.obj.compareTo(o.obj);
	}

	public void keepTopK(int k) {
		this.setConceitos(this.conceitos.stream().limit(k).collect(Collectors.toList()));
	}
}
