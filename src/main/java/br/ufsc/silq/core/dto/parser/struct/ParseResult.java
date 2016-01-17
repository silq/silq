package br.ufsc.silq.core.dto.parser.struct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufsc.silq.core.utils.parser.ConverterHelper;

public class ParseResult implements Serializable {

	private static final long serialVersionUID = 2847835713658377152L;

	private String nome;
	private String areaAvaliada;
	private AreaConhecimento areaGrandeAreaConhecimento;
	private String nomeEspecialidade;
	private String nomeSubAreaConhecimento;
	private List<Trabalho> trabalhos;
	private List<Artigo> artigos;
	private String nivelSimilaridade;
	private String anoPublicacaoDe;
	private String anoPublicacaoAte;
	private Boolean hasConceitosTrabalhos = true;

	public ParseResult() {
		this.nome = "";
		this.areaGrandeAreaConhecimento = new AreaConhecimento();
		this.nomeEspecialidade = "";
		this.nomeSubAreaConhecimento = "";
		this.trabalhos = new ArrayList<>();
		this.artigos = new ArrayList<>();
	}

	@Override
	public String toString() {
		String info = "";

		info += "Nome do pesquisador: " + this.getNome();
		info += "\nNome Área do Conhecimento: " + this.getAreaGrandeAreaConhecimento().getNomeArea();
		info += "\nNome da Especialidade: " + this.getNomeEspecialidade();
		info += "\nNome Sub Área Conhecimento: " + this.getNomeSubAreaConhecimento();
		info += "\nNome Grande Área do Conhecimento: " + this.getAreaGrandeAreaConhecimento().getNomeGrandeArea();

		info += "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
		info += "\n\nTrabalhos:\n";
		for (Trabalho trabalho : this.trabalhos) {
			info += "\t" + trabalho + "\n";
		}

		info += "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
		info += "\nArtigos:\n";
		for (Artigo artigo : this.artigos) {
			info += "\t" + artigo + "\n";
		}

		return info;
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

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeEspecialidade() {
		return this.nomeEspecialidade;
	}

	public void setNomeEspecialidade(String nomeEspecialidade) {
		this.nomeEspecialidade = nomeEspecialidade;
	}

	public String getNomeSubAreaConhecimento() {
		return this.nomeSubAreaConhecimento;
	}

	public void setNomeSubAreaConhecimento(String nomeSubAreaConhecimento) {
		this.nomeSubAreaConhecimento = nomeSubAreaConhecimento;
	}

	public List<Trabalho> getTrabalhos() {
		return this.trabalhos;
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

	public void setTrabalhos(List<Trabalho> trabalhos) {
		this.trabalhos = trabalhos;
	}

	public List<Artigo> getArtigos() {
		return this.artigos;
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

	public void setArtigos(List<Artigo> artigos) {
		this.artigos = artigos;
	}

	public AreaConhecimento getAreaGrandeAreaConhecimento() {
		return this.areaGrandeAreaConhecimento;
	}

	public void setAreaGrandeAreaConhecimento(AreaConhecimento areaGrandeAreaConhecimento) {
		this.areaGrandeAreaConhecimento = areaGrandeAreaConhecimento;
	}

	public String getNivelSimilaridade() {
		return this.nivelSimilaridade;
	}

	public void setNivelSimilaridade(String nivelSimilaridade) {
		this.nivelSimilaridade = nivelSimilaridade;
	}

	public String getAnoPublicacaoDe() {
		return this.anoPublicacaoDe;
	}

	public void setAnoPublicacaoDe(String anoPublicacaoDe) {
		this.anoPublicacaoDe = anoPublicacaoDe;
	}

	public String getAnoPublicacaoAte() {
		return this.anoPublicacaoAte;
	}

	public void setAnoPublicacaoAte(String anoPublicacaoAte) {
		this.anoPublicacaoAte = anoPublicacaoAte;
	}

	public Boolean getHasConceitosTrabalhos() {
		return this.hasConceitosTrabalhos;
	}

	public void setHasConceitosTrabalhos(Boolean hasConceitosTrabalhos) {
		this.hasConceitosTrabalhos = hasConceitosTrabalhos;
	}

	public String getAreaAvaliada() {
		return this.areaAvaliada;
	}

	public void setAreaAvaliada(String areaAvaliada) {
		this.areaAvaliada = areaAvaliada;
	}
}
