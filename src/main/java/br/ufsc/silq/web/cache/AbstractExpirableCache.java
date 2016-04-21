package br.ufsc.silq.web.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import lombok.ToString;

/**
 * Cache abstrato que mapeia IDs (String) para listas de dados do tipo definido e que limpa os itens de cache
 * após passados um intervalo de tempo definido.
 *
 * @param <T>
 *            Tipo de dado cacheado.
 */
@ToString
public abstract class AbstractExpirableCache<T> {
	private ConcurrentMap<String, CacheList<T>> map = new ConcurrentHashMap<>();

	/**
	 * Salva um novo item no cache com ID especificado.
	 *
	 * @param cacheId
	 *            ID do cache a ser utilizado. Um mesmo ID mapeia vários itens
	 *            de cache.
	 * @param T
	 *            Dado a ser salvo no cache com ID especificado, junto com os
	 *            demais itens já salvos com este ID.
	 */
	public void insert(String cacheId, T data) {
		if (!this.map.containsKey(cacheId)) {
			this.map.put(cacheId, new CacheList<>());
		}

		CacheList<T> list = this.map.get(cacheId);
		list.add(data);
	}

	/**
	 * Retorna os itens salvos no cache com ID especificado.
	 *
	 * @param cacheId
	 * @return A lista com os itens de cache com ID especificado ou uma lista vazia caso não existam itens neste ID.
	 */
	public CacheList<T> get(String cacheId) {
		CacheList<T> list = this.map.get(cacheId);
		return list != null ? list : new CacheList<>();
	}

	/**
	 * @return O número de listas de cache (um para cada ID de cache informado).
	 */
	public int count() {
		return this.map.size();
	}

	/**
	 * Informa a data de última modificação da lista de cache com ID especificado.
	 *
	 * @param cacheId
	 * @return A data de modificação do cache.
	 */
	public LocalDateTime getModifiedAt(String cacheId) {
		return this.get(cacheId).modifiedAt;
	}

	/**
	 * Remove os itens do cache com ID especificado.
	 *
	 * @param cacheId
	 *            ID do cache que deve ser limpo.
	 */
	public void clear(String cacheId) {
		this.map.remove(cacheId);
	}

	/**
	 * Limpa todas as caches já expiradas.
	 *
	 * @return O número de {@link CacheList}s expirados.
	 */
	public int clearExpired() {
		int count = 0;
		for (String cacheId : this.map.keySet()) {
			if (this.get(cacheId).modifiedAt.plus(this.expirePeriod()).isBefore(LocalDateTime.now())) {
				this.clear(cacheId);
				count++;
			}
		}
		return count;
	}

	/**
	 * Período de expiração de caches. Deve retornar o tempo em que os itens da cache ficam ativos após serem modificados.
	 * Operações de modificação renovam a data de modificação de uma cache.
	 * Não há garantia que os itens de cache sejam de fato limpos. Para isso, o método clearExpired() deve ser invocado
	 * de tempos em tempos.
	 *
	 * @return
	 */
	abstract Period expirePeriod();

	private static class CacheList<T> extends ArrayList<T> {
		private LocalDateTime modifiedAt;

		protected LocalDateTime updateModifiedAt() {
			this.modifiedAt = LocalDateTime.now();
			return this.modifiedAt;
		}

		@Override
		public boolean add(T e) {
			this.updateModifiedAt();
			return super.add(e);
		}

		@Override
		public void add(int index, T element) {
			this.updateModifiedAt();
			super.add(index, element);
		}

		@Override
		public boolean addAll(Collection<? extends T> c) {
			this.updateModifiedAt();
			return super.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends T> c) {
			this.updateModifiedAt();
			return super.addAll(index, c);
		}
	}
}
