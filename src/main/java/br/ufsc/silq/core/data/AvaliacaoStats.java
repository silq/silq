package br.ufsc.silq.core.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.ufsc.silq.config.EstradosEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.parser.dto.Artigo;
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
	 * Conjunto de artigos utilizados para gerar estas estatísticas.
	 */
	@JsonIgnore
	private List<Conceituado<Artigo>> artigos = new ArrayList<>();

	/**
	 * Conjunto de trabalhos utilizados para gerar estas estatísticas.
	 */
	@JsonIgnore
	private List<Conceituado<Trabalho>> trabalhos = new ArrayList<>();

	/**
	 * Concatena um {@link AvaliacaoStats} ao objeto atual, unindo as respectivas listas de artigos e trabalhos
	 * com o objetivo de gerar estatísticas dos dois objetos combinados.
	 *
	 * @param o O outro objeto de estatísticas a ser agregado ao atual.
	 * @return Um novo objeto {@link AvaliacaoStats} contendo as estatísticas combinadas dos dois objetos.
	 */
	public AvaliacaoStats reduce(AvaliacaoStats o) {
		ArrayList<Conceituado<Artigo>> copyArtigos = new ArrayList<>(this.artigos);
		ArrayList<Conceituado<Trabalho>> copyTrabalhos = new ArrayList<>(this.trabalhos);
		copyArtigos.addAll(o.getArtigos());
		copyTrabalhos.addAll(o.getTrabalhos());
		return new AvaliacaoStats(copyArtigos, copyTrabalhos);
	}

	/**
	 * @return O primeiro ano em que houve publicação.
	 */
	public Integer getAnoPrimeiraPublicacao() {
		Stream<Integer> anos = this.streamAnos();
		return anos.min((a1, a2) -> a1.compareTo(a2)).orElse(0);
	}

	/**
	 * @return O último ano em que houve publicação.
	 */
	public Integer getAnoUltimaPublicacao() {
		Stream<Integer> anos = this.streamAnos();
		return anos.max((a1, a2) -> a1.compareTo(a2)).orElse(0);
	}

	/**
	 * Cria um Stream contendo os anos de todos os Trabalhos e Artigos.
	 *
	 * @return Um Stream de anos.
	 */
	private Stream<Integer> streamAnos() {
		Stream<Integer> artigoAnos = this.artigos.stream().map(artigo -> artigo.getObj().getAno());
		Stream<Integer> trabalhoAnos = this.trabalhos.stream().map(trabalho -> trabalho.getObj().getAno());
		return Stream.concat(artigoAnos, trabalhoAnos);
	}

	/**
	 * Conta a quantidade de publicações de cada ano.
	 *
	 * @return Um map do tipo (ano -> quantidade de publicações deste ano).
	 */
	public Map<String, Map<Integer, ContadorConceitos>> getPublicacoesPorAno() {
		HashMap<String, Map<Integer, ContadorConceitos>> hash = new HashMap<>();
		hash.put("artigos", this.getQtdeArtigosPorAno());
        hash.put("artigosSICLAP", this.getQtdeArtigosPorAnoSICLAP());
		hash.put("trabalhos", this.getQtdeTrabalhosPorAno());
        hash.put("trabalhosSICLAP", this.getQtdeTrabalhosPorAnoSICLAP());
        hash.put("resumos", this.getQtdeResumosPorAno());
        hash.put("resumosSICLAP", this.getQtdeResumosPorAnoSICLAP());
		return hash;
	}

	/**
	 * Conta a quantidade de ARTIGOS de cada ano, separados por conceito.
	 *
	 * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
	 */
	protected Map<Integer, ContadorConceitos> getQtdeArtigosPorAno() {
		Map<Integer, ContadorConceitos> map = new HashMap<>();

		this.artigos.stream().forEach(c -> {
			map.putIfAbsent(c.getObj().getAno(), new ContadorConceitos());
			ContadorConceitos contador = map.get(c.getObj().getAno());
            Conceito conceitoQualis = getConceitoQualis(c.getConceitos(), c.getObj().getAno());
            if (conceitoQualis != null) {
                contador.increment(conceitoQualis.getConceito());
			} else {
				contador.increment(ContadorConceitos.SEM_CONCEITO);
			}
		});

		return map;
	}

    /**
     * Conta a quantidade de ARTIGOS de cada ano, separados por conceito segundo a métodologia SICLAP.
     *
     * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
     */
    protected Map<Integer, ContadorConceitos> getQtdeArtigosPorAnoSICLAP() {
        Map<Integer, ContadorConceitos> map = new HashMap<>();

        this.artigos.stream().forEach(c -> {
            map.putIfAbsent(c.getObj().getAno(), new ContadorConceitos());
            ContadorConceitos contador = map.get(c.getObj().getAno());

            Integer maiorEstrato = -1;
            maiorEstrato = calcularNotaSICLAP(c.getConceitos(), c.getObj().getAno());
            if (maiorEstrato >= 0){
                contador.increment(EstradosEnum.values()[maiorEstrato].name());
            } else {
                contador.increment(ContadorConceitos.SEM_CONCEITO);
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

        this.trabalhos.stream().forEach(c -> {
            map.putIfAbsent(c.getObj().getAno(), new ContadorConceitos());
            ContadorConceitos contador = map.get(c.getObj().getAno());
            if (c.getObj().getNatureza().getDesc().contains("Completo")) {
                Conceito conceitoQualis = getConceitoQualis(c.getConceitos(), c.getObj().getAno());
                if (conceitoQualis != null) {
                    contador.increment(conceitoQualis.getConceito());
                } else {
                    contador.increment(ContadorConceitos.SEM_CONCEITO);
                }
            }
        });

        return map;
    }

    /**
     * Conta a quantidade de TRABALHOS de cada ano, separados por conceito segundo a métodologia SICLAP.
     *
     * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
     */
    protected Map<Integer, ContadorConceitos> getQtdeTrabalhosPorAnoSICLAP() {
        Map<Integer, ContadorConceitos> map = new HashMap<>();

        this.trabalhos.stream().forEach(c -> {
            map.putIfAbsent(c.getObj().getAno(), new ContadorConceitos());
            ContadorConceitos contador = map.get(c.getObj().getAno());
            if (c.getObj().getNatureza().getDesc().contains("Completo")) {
                Integer maiorEstrato = -1;
                maiorEstrato = calcularNotaSICLAP(c.getConceitos(),c.getObj().getAno());
                if (maiorEstrato >= 0){
                    contador.increment(EstradosEnum.values()[maiorEstrato].name());
                } else {
                    contador.increment(ContadorConceitos.SEM_CONCEITO);
                }
            }
        });

        return map;
    }

    /**
     * Conta a quantidade de RESUMOS de cada ano, separados por conceito.
     *
     * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
     */
    protected Map<Integer, ContadorConceitos> getQtdeResumosPorAno() {
        Map<Integer, ContadorConceitos> map = new HashMap<>();

        this.trabalhos.stream().forEach(c -> {
            map.putIfAbsent(c.getObj().getAno(), new ContadorConceitos());
            ContadorConceitos contador = map.get(c.getObj().getAno());
            if (!c.getObj().getNatureza().getDesc().contains("Completo")) {
                Conceito conceitoQualis = getConceitoQualis(c.getConceitos(), c.getObj().getAno());
                if (conceitoQualis != null) {
                    contador.increment(conceitoQualis.getConceito());
                } else {
                    contador.increment(ContadorConceitos.SEM_CONCEITO);
                }
            }
        });

        return map;
    }

    /**
     * Conta a quantidade de RESUMOS de cada ano, separados por conceito segundo a métodologia SICLAP.
     *
     * @return Um map do tipo { ano: { total: 220, a1: 2, a2: 34, ... }} de publicações deste ano).
     */
    protected Map<Integer, ContadorConceitos> getQtdeResumosPorAnoSICLAP() {
        Map<Integer, ContadorConceitos> map = new HashMap<>();

        this.trabalhos.stream().forEach(c -> {
            map.putIfAbsent(c.getObj().getAno(), new ContadorConceitos());
            ContadorConceitos contador = map.get(c.getObj().getAno());
            if (!c.getObj().getNatureza().getDesc().contains("Completo")) {
                Integer maiorEstrato = -1;
                maiorEstrato = calcularNotaSICLAP(c.getConceitos(), c.getObj().getAno());
                if (maiorEstrato >= 0){
                    contador.increment(EstradosEnum.values()[maiorEstrato].name());
                } else {
                    contador.increment(ContadorConceitos.SEM_CONCEITO);
                }
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

        for (Conceituado<Artigo> artigo : this.getArtigos()) {
            if(artigo.hasConceito()){
                Conceito conceitoQualis = getConceitoQualis(artigo.getConceitos(), artigo.getObj().getAno());
                if (conceitoQualis != null){
                    totalizadorMap.putIfAbsent(conceitoQualis.getConceito(), new AtomicInteger(0));
                    totalizadorMap.get(conceitoQualis.getConceito()).incrementAndGet();
                }
            }
        }

        for (Conceituado<Trabalho> trabalho : this.getTrabalhos()) {
            if(trabalho.hasConceito()){
                Conceito conceitoQualis = getConceitoQualis(trabalho.getConceitos(), trabalho.getObj().getAno());
                if (conceitoQualis != null){
                    totalizadorMap.putIfAbsent(conceitoQualis.getConceito(), new AtomicInteger(0));
                    totalizadorMap.get(conceitoQualis.getConceito()).incrementAndGet();
                }
            }
        }

		List<TotalizadorConceito> totalizador = new ArrayList<>();
		totalizadorMap.forEach((conceito, qtde) -> totalizador.add(new TotalizadorConceito(conceito, qtde.get())));
		Collections.sort(totalizador);
		return totalizador;
	}

    /**
     * Conta a quantidade de avaliações de cada conceito. Ex.: A: 3, B1: 5, C: 10 segundo a métodologia SICLAP.
     *
     * @return Uma lista de {@link TotalizadorConceito} representado a quantidade de avaliações dadas para cada conceito.
     */
    public List<TotalizadorConceito> getTotalizadorSICLAP() {
        ConcurrentMap<String, AtomicInteger> totalizadorMap = new ConcurrentHashMap<>();

        Integer maiorEstrato = -1;

        for (Conceituado<Artigo> artigo : this.getArtigos()) {
            maiorEstrato = calcularNotaSICLAP(artigo.getConceitos(), artigo.getObj().getAno());
            if (maiorEstrato >= 0) {
                totalizadorMap.putIfAbsent(EstradosEnum.values()[maiorEstrato].name(), new AtomicInteger(0));
                totalizadorMap.get(EstradosEnum.values()[maiorEstrato].name()).incrementAndGet();
            }
        }
        for (Conceituado<Trabalho> trabalho : this.getTrabalhos()) {
            maiorEstrato = calcularNotaSICLAP(trabalho.getConceitos(), trabalho.getObj().getAno());
            if (maiorEstrato >= 0) {
                totalizadorMap.putIfAbsent(EstradosEnum.values()[maiorEstrato].name(), new AtomicInteger(0));
                totalizadorMap.get(EstradosEnum.values()[maiorEstrato].name()).incrementAndGet();
            }
        }

        List<TotalizadorConceito> totalizador = new ArrayList<>();
        totalizadorMap.forEach((conceito, qtde) -> totalizador.add(new TotalizadorConceito(conceito, qtde.get())));
        Collections.sort(totalizador);
        return totalizador;
    }

    /**
     * Calcula a nota segundo a métodologia SICLAP.
     *
     * @return Integer O maior conceito
     */
    private Integer calcularNotaSICLAP(List<Conceito> conceitos, Integer ano) {
        Conceito primeiroConceito;
        Conceito segundoConceito = null;

        List<Conceito> primeirosConceitos = conceitos.stream().filter(p -> p.getAno() < ano).collect(Collectors.toList());
        primeiroConceito = getConceito(primeirosConceitos);
        if (primeiroConceito != null) {
            int anoPrimeiroConceito = primeiroConceito.getAno();
            List<Conceito> segundosConceitos = conceitos.stream().filter(p -> p.getAno() < anoPrimeiroConceito).collect(Collectors.toList());
            segundoConceito = getConceito(segundosConceitos);
        }
        Integer estratoPrimeiro = primeiroConceito != null ? EstradosEnum.valueOf(primeiroConceito.getConceito()).getEstratoNumber() : -1;
        Integer estratoSegundo = segundoConceito != null ? EstradosEnum.valueOf(segundoConceito.getConceito()).getEstratoNumber() : -1;

        return Math.max(estratoPrimeiro, estratoSegundo);
    }

    private Conceito getConceito(List<Conceito> conceitos) {
        Conceito conceito = null;
        if (!conceitos.isEmpty()) {
            conceitos = conceitos.stream().sorted(Comparator.comparing(Conceito::isFeedback,Comparator.reverseOrder())).collect(Collectors.toList());
            if(!conceitos.get(0).isFeedback()){
                conceitos.sort(Comparator.comparing(Conceito::getSimilaridade,Comparator.reverseOrder()).thenComparing(Conceito::getAno,Comparator.reverseOrder()));
            }
            conceito = conceitos.get(0);
        }
        return conceito;
    }

    private Conceito getConceitoQualis(List<Conceito> conceitos, Integer ano) {
        return conceitos.stream()
            .filter(x -> ano.equals(x.getAno())|| x.getTipoConceito().equals(TipoConceito.FEEDBACK))
            .findFirst()
            .orElse(null);
    }

	public static class ContadorConceitos extends ConcurrentHashMap<String, Integer> {
		private static final long serialVersionUID = 4651129274330152258L;
		public static final String SEM_CONCEITO = "sem-conceito";
		public static final String TOTAL = "total";

		public ContadorConceitos() {
			this.put(TOTAL, 0);
			this.put(SEM_CONCEITO, 0);
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

	@Data
	public static class TotalizadorConceito implements Comparable<TotalizadorConceito> {
		private final String conceito;
		private final Integer qtde;

		@Override
		public int compareTo(TotalizadorConceito o) {
			return this.conceito.compareTo(o.getConceito());
		}
	}
}
