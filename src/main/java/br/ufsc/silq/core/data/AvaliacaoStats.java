package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.Conceito.TotalizadorConceito;
import br.ufsc.silq.core.parser.dto.Trabalho;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Estatísticas de avaliação.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoStats {

	/**
	 * Lista de artigos utilizados para gerar estas estatísticas.
	 */
	@JsonIgnore
	private List<Artigo> artigos = new ArrayList<>();

	/**
	 * Lista de trabalhos utilizados para gerar estas estatísticas.
	 */
	@JsonIgnore
	private List<Trabalho> trabalhos = new ArrayList<>();

	/**
	 * Concatena um {@link AvaliacaoStats} ao objeto atual, unindo as respectivas listas de artigos e trabalhos
	 * com o objetivo de gerar estatísticas dos dois objetos combinados.
	 *
	 * @param o O outro objeto de estatísticas a ser agregado ao atual.
	 * @return O objeto atual contendo as estatísticas antigas combinadas com as novas.
	 */
	public AvaliacaoStats reduce(AvaliacaoStats o) {
		this.artigos.addAll(o.getArtigos());
		this.trabalhos.addAll(o.getTrabalhos());
		return this;
	}

	/**
	 * @return O primeiro ano em que houve publicação.
	 */
	public Integer getAnoPrimeiraPublicacao() {
		Stream<Integer> artigoAnos = this.artigos.stream().map((artigo) -> artigo.getAno());
		Stream<Integer> trabalhoAnos = this.trabalhos.stream().map((trabalho) -> trabalho.getAno());
		Stream<Integer> anos = Stream.concat(artigoAnos, trabalhoAnos);
		return anos.min((a1, a2) -> a1.compareTo(a2)).orElse(0);
	}

	/**
	 * @return O último ano em que houve publicação.
	 */
	public Integer getAnoUltimaPublicacao() {
		Stream<Integer> artigoAnos = this.artigos.stream().map((artigo) -> artigo.getAno());
		Stream<Integer> trabalhoAnos = this.trabalhos.stream().map((trabalho) -> trabalho.getAno());
		Stream<Integer> anos = Stream.concat(artigoAnos, trabalhoAnos);
		return anos.max((a1, a2) -> a1.compareTo(a2)).orElse(0);
	}

	/**
	 * Conta a quantidade de publicações de cada ano.
	 *
	 * @return Um map do tipo (ano -> quantidade de publicações deste ano).
	 */
	public Map<String, Map<?, ?>> getPublicacoesPorAno() {
		HashMap<String, Map<?, ?>> hash = new HashMap<>();
		hash.put("artigos", this.getQtdeArtigosPorAno());
		hash.put("trabalhos", this.getQtdeTrabalhosPorAno());
		return hash;
	}

	/**
	 * Conta a quantidade de ARTIGOS de cada ano, separados por conceito.
	 *
	 * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
	 */
	protected Map<Integer, ContadorConceitos> getQtdeArtigosPorAno() {
		Map<Integer, ContadorConceitos> map = new HashMap<>();

		this.artigos.stream().forEach((c) -> {
			map.putIfAbsent(c.getAno(), new ContadorConceitos());
			ContadorConceitos contador = map.get(c.getAno());

			if (c.getConceitos().isEmpty()) {
				contador.increment(ContadorConceitos.SEM_CONCEITO);
			} else {
				c.getConceitos().stream()
						.forEach((conceito) -> contador.increment(conceito.getConceito()));
			}
		});

		return map;
	}

	/**
	 * Conta a quantidade de TRABALHOS de cada ano, separados por conceito.
	 *
	 * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
	 */
	protected Map<Integer, ContadorConceitos> getQtdeTrabalhosPorAno() {
		Map<Integer, ContadorConceitos> map = new HashMap<>();

		this.trabalhos.stream().forEach((c) -> {
			map.putIfAbsent(c.getAno(), new ContadorConceitos());
			ContadorConceitos contador = map.get(c.getAno());

			if (c.getConceitos().isEmpty()) {
				contador.increment(ContadorConceitos.SEM_CONCEITO);
			} else {
				c.getConceitos().stream()
						.forEach((conceito) -> contador.increment(conceito.getConceito()));
			}
		});

		return map;
	}

	/**
	 * Conta a quantidade de avaliações de cada conceito. Ex.: A: 3, B1: 5, C: 10.
	 *
	 * @return Uma lista de {@link TotalizadorConceito} representado a quantidade de avaliações dadas para cada conceito.
	 */
	public List<TotalizadorConceito> getTotalizador() {
		ConcurrentMap<String, AtomicInteger> totalizadorMap = new ConcurrentHashMap<>();

		for (Artigo artigo : this.getArtigos()) {
			List<Conceito> conceitos = artigo.getConceitos();
			if (!conceitos.isEmpty()) {
				Conceito conceito = conceitos.get(0);
				totalizadorMap.putIfAbsent(conceito.getConceito(), new AtomicInteger(0));
				totalizadorMap.get(conceito.getConceito()).incrementAndGet();
			}
		}

		for (Trabalho trabalho : this.getTrabalhos()) {
			List<Conceito> conceitos = trabalho.getConceitos();
			if (!conceitos.isEmpty()) {
				Conceito conceito = conceitos.get(0);
				totalizadorMap.putIfAbsent(conceito.getConceito(), new AtomicInteger(0));
				totalizadorMap.get(conceito.getConceito()).incrementAndGet();
			}
		}

		List<TotalizadorConceito> totalizador = new ArrayList<>();
		totalizadorMap.forEach((conceito, qtde) -> {
			totalizador.add(new TotalizadorConceito(conceito, qtde.get()));
		});

		Collections.sort(totalizador);
		return totalizador;
	}

	public static class ContadorConceitos extends ConcurrentHashMap<String, Integer> {
		private static final long serialVersionUID = 4651129274330152258L;
		public static final String SEM_CONCEITO = "sem-conceito";
		public static final String TOTAL = "total";

		public ContadorConceitos() {
			this.put(TOTAL, 0);
		}

		/**
		 * Incrementa o contador associado ao conceito em uma unidade.
		 *
		 * @param conceito Conceito a ter seu valor total incrementado.
		 */
		public void increment(String conceito) {
			this.putIfAbsent(conceito, 0);
			this.put(conceito, this.get(conceito) + 1);
			this.put(TOTAL, this.get(TOTAL) + 1);
		}
	}
}
