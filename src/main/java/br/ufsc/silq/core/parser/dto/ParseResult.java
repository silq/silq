package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufsc.silq.core.utils.parser.ConverterHelper;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = { "nome", "areaGrandeAreaConhecimento", "nomeEspecialidade", "nomeSubAreaConhecimento" })
public class ParseResult implements Serializable {

	private static final long serialVersionUID = 2847835713658377152L;

	// Dados extraídos do currículo Lattes:
	private String nome;
	private AreaConhecimento areaGrandeAreaConhecimento;
	private String nomeEspecialidade;
	private String nomeSubAreaConhecimento;
	private List<Trabalho> trabalhos; // conceitos são setados posteriormente pelo serviço de similaridade
	private List<Artigo> artigos; // conceitos são setados posteriormente pelo serviço de similaridade

	// Dados setados após extração:
	private String areaAvaliada;
	private String nivelSimilaridade;
	private String anoPublicacaoDe;
	private String anoPublicacaoAte;

	public ParseResult() {
		this.nome = "";
		this.areaGrandeAreaConhecimento = new AreaConhecimento();
		this.nomeEspecialidade = "";
		this.nomeSubAreaConhecimento = "";
		this.trabalhos = new ArrayList<>();
		this.artigos = new ArrayList<>();
	}

	public String getTotalizador() {
		ConcurrentMap<String, AtomicInteger> totalizadorMap = new ConcurrentHashMap<String, AtomicInteger>();

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

		List<String> keySetList = new ArrayList<>();
		for (String key : totalizadorMap.keySet()) {
			keySetList.add(key);
		}

		Collections.sort(keySetList);

		String totalizador = "";

		for (String key : keySetList) {
			totalizador += totalizadorMap.get(key) + "x " + key + " - ";
		}

		if (totalizador.length() > 3) {
			totalizador = totalizador.substring(0, totalizador.length() - 3);
		}

		return totalizador;
	}

	public void order() {
		Collections.sort(this.getArtigos());
		Collections.sort(this.getTrabalhos());
	}

	public List<Trabalho> getTrabalhosByAno() {
		Integer anoDe = ConverterHelper.parseIntegerSafely(this.getAnoPublicacaoDe());
		Integer anoAte = ConverterHelper.parseIntegerSafely(this.getAnoPublicacaoAte());
		ArrayList<Trabalho> trabalhos = new ArrayList<Trabalho>();

		for (Trabalho trabalho : this.getTrabalhos()) {
			if (anoDe <= trabalho.getAnoPublicacao() && trabalho.getAnoPublicacao() <= anoAte) {
				trabalhos.add(trabalho);
			}
		}

		return trabalhos;
	}

	public List<Trabalho> getTrabalhosByAno(Integer ano) {
		ArrayList<Trabalho> trabalhos = new ArrayList<Trabalho>();

		for (Trabalho trabalho : this.getTrabalhos()) {
			if (ano.equals(trabalho.getAnoPublicacao())) {
				trabalhos.add(trabalho);
			}
		}

		return trabalhos;
	}

	public List<Artigo> getArtigosByAno() {
		Integer anoDe = ConverterHelper.parseIntegerSafely(this.getAnoPublicacaoDe());
		Integer anoAte = ConverterHelper.parseIntegerSafely(this.getAnoPublicacaoAte());
		ArrayList<Artigo> artigos = new ArrayList<Artigo>();

		for (Artigo artigo : this.getArtigos()) {
			if (anoDe <= artigo.getAno() && artigo.getAno() <= anoAte) {
				artigos.add(artigo);
			}
		}

		return artigos;
	}

	public List<Artigo> getArtigosByAno(Integer ano) {
		ArrayList<Artigo> artigos = new ArrayList<Artigo>();

		for (Artigo artigo : this.getArtigos()) {
			if (ano.equals(artigo.getAno())) {
				artigos.add(artigo);
			}
		}

		return artigos;
	}
}
