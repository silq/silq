package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.Conceito.TotalizadorConceito;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import lombok.Data;

/**
 * Resultado da avaliação de um currículo Lattes, realizado pelo serviço {@link AvaliacaoResult}.
 */
@Data
public class AvaliacaoResult {

	/**
	 * Form de configuração utilizado na avaliação
	 */
	private final AvaliarForm form;

	/**
	 * Dados gerais extraídos do currículo avaliado
	 */
	private final DadosGeraisResult dadosGerais;

	/**
	 * Lista de artigos avaliados, contendo os respectivos {@link Conceito}s
	 */
	private List<Artigo> artigos = new ArrayList<>();

	/**
	 * Lista de trabalhos avaliados, contendo os respectivos {@link Conceito}s
	 */
	private List<Trabalho> trabalhos = new ArrayList<>();

	/**
	 * Ordena os artigos e trabalhos por ano (decrescente).
	 */
	public void order() {
		Collections.sort(this.getArtigos());
		Collections.sort(this.getTrabalhos());
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
			if (conceitos.size() > 0) {
				Conceito conceito = conceitos.get(0);
				totalizadorMap.putIfAbsent(conceito.getConceito(), new AtomicInteger(0));
				totalizadorMap.get(conceito.getConceito()).incrementAndGet();
			}
		}

		for (Trabalho trabalho : this.getTrabalhos()) {
			List<Conceito> conceitos = trabalho.getConceitos();
			if (conceitos.size() > 0) {
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

	/**
	 * Conta a quantidade de publicações de cada ano.
	 *
	 * @return Um map do tipo (ano -> quantidade de publicações deste ano).
	 */
	public Map<Integer, Integer> getPublicacoesPorAno() {
		Map<Integer, Integer> map = new HashMap<>();

		this.getArtigos().stream().forEach((artigo) -> {
			map.putIfAbsent(artigo.getAno(), 0);
			map.put(artigo.getAno(), map.get(artigo.getAno()) + 1);
		});

		this.getTrabalhos().stream().forEach((trabalho) -> {
			map.putIfAbsent(trabalho.getAno(), 0);
			map.put(trabalho.getAno(), map.get(trabalho.getAno()) + 1);
		});

		return map;
	}
}
