package br.ufsc.silq.core.data;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Uma lista que ordena automaticamente os itens nela inseridas.
 *
 * NOTA/USE COM CUIDADO: Esta abstração é uma afronta ao {@link List} do java.utils, pois não honra o
 * método {@link List#add(int, Object)}. O comportamento esperado deste método é inserir o elemento
 * em determinada posição, mas esta implementação ignora o índice parâmetro para manter a lista
 * sempre ordenada, ou seja, tem o mesmo comportamento do método {@link #add(Comparable)}.
 *
 * @param <T> Tipo do dado da lista.
 */
public class SortedList<T extends Comparable<T>> extends LinkedList<T> {

	private static final long serialVersionUID = 4995595997396534512L;

	public SortedList() {
	}

	public SortedList(Collection<? extends T> c) {
		super(c);
	}

	/**
	 * Insere o item na lista, IGNORANDO O ÍNDICE PARÂMETRO a fim de manter a lista ordenada.
	 *
	 * @param index Parâmetro ignorado.
	 * @param element Elemento a ser inserido.
	 */
	@Override
	public void add(int index, T element) {
		this.add(element);
	}

	/**
	 * Insere o item na lista, mantendo sua ordenação.
	 * A complexidade do método é de O(log n), pois a posição do elemento é determinada
	 * usando uma busca binária. A inserção cai para O(1) pois estamos usando um {@link LinkedList}.
	 *
	 * @param e Elemento a ser inserido.
	 * @return True
	 */
	@Override
	public boolean add(T e) {
		int i = Collections.binarySearch(this, e);
		int pos = i >= 0 ? i + 1 : -i - 1;
		super.add(pos, e);
		return true;
	}

	/**
	 * Adiciona a coleção à lista, matendo sua ordenação.
	 * A complexidade do método é O(m * log n), sendo m o tamanho da coleção a ser inserida.
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		c.forEach(this::add);
		return !c.isEmpty();
	}
}
