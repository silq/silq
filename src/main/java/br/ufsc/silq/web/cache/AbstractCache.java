package br.ufsc.silq.web.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.ToString;

@ToString
public abstract class AbstractCache<T> {
	protected Map<String, ArrayList<T>> map = new HashMap<>();

	/**
	 * Salva um novo item no cache com ID especificado.
	 *
	 * @param cacheId
	 *            ID do cache a ser utilizado. Um mesmo ID mapeia vários itens
	 *            de cache
	 * @param T
	 *            Dado a ser salvo no cache com ID especificado
	 */
	public void insert(String cacheId, T data) {
		if (!this.map.containsKey(cacheId)) {
			this.map.put(cacheId, new ArrayList<>());
		}

		ArrayList<T> list = this.map.get(cacheId);
		list.add(data);
	}

	/**
	 * Retorna os itens salvos no cache com ID especificado.
	 *
	 * @param cacheId
	 * @return
	 */
	public ArrayList<T> get(String cacheId) {
		ArrayList<T> list = this.map.get(cacheId);
		return list != null ? list : new ArrayList<>();
	}

	/**
	 * Remove os itens do cache com ID especificado
	 *
	 * @param cacheId
	 *            ID do cache que deve ser limpo
	 */
	public void clear(String cacheId) {
		this.map.remove(cacheId);
	}
}
