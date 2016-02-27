package br.ufsc.silq.core.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.attribute.ArtigoAttributeGetter;
import br.ufsc.silq.core.parser.attribute.AttributeGetter;
import br.ufsc.silq.core.parser.dto.AreaConhecimento;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.PesquisadorResult;
import br.ufsc.silq.core.parser.dto.TipoOrigemCurriculo;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.parser.qualis.similarity.CompareSimilarity;
import br.ufsc.silq.core.utils.SilqDataUtils;
import br.ufsc.silq.core.utils.SilqStringUtils;
import br.ufsc.silq.core.utils.files.FileManager;
import br.ufsc.silq.core.utils.parser.ConverterHelper;

@Component
public class LattesParser {

	@Inject
	private CompareSimilarity compareSimilarity;

	public DadosGeraisResult parseCurriculaDadosGerais(File file) throws SilqErrorException {
		DadosGeraisResult dadosGeraisResult = new DadosGeraisResult();

		Node nodoRaiz = FileManager.getNodoRaiz(file);

		List<String> dadoGeralList = AttributeGetter.iterateNodes(ParserSets.DADOS_GERAIS_SET, nodoRaiz);
		// TODO Currículo sem ID! Desatualizado!
		dadosGeraisResult.setIdCurriculo(dadoGeralList.get(2));
		dadosGeraisResult.setUltimaAtualizacao(SilqDataUtils.formatDates(dadoGeralList.get(0), dadoGeralList.get(1)));
		if (dadoGeralList.get(3).equals("LATTES_OFFLINE")) {
			dadosGeraisResult.setTipoOrigemCurriculo(TipoOrigemCurriculo.OFFLINE);
		}

		List<String> areas = AttributeGetter.iterateNodes(ParserSets.AREAS_SET, nodoRaiz);
		if (areas.size() > 3) {
			AreaConhecimento areaConhecimento = new AreaConhecimento();
			areaConhecimento.setNomeArea(SilqStringUtils.setHifenIfVazio(areas.get(0)));
			areaConhecimento.setNomeGrandeArea(SilqStringUtils.setHifenIfVazio(areas.get(3)));

			dadosGeraisResult.setNomeEspecialidade(SilqStringUtils.setHifenIfVazio(areas.get(1)));
			dadosGeraisResult.setNomeSubAreaConhecimento(SilqStringUtils.setHifenIfVazio(areas.get(2)));
			dadosGeraisResult.setAreaGrandeAreaConhecimento(areaConhecimento);
		}

		List<String> nomeList = AttributeGetter.iterateNodes(ParserSets.NOME_SET, nodoRaiz);
		if (nomeList.size() == 1) {
			dadosGeraisResult.setNome(nomeList.get(0));
		}

		return dadosGeraisResult;
	}

	public PesquisadorResult parseCurriculaPesquisador(File file) throws SilqErrorException {
		PesquisadorResult pesquisadorResult = new PesquisadorResult();

		Node nodoRaiz = FileManager.getNodoRaiz(file);

		List<String> pesquisadorList = AttributeGetter.iterateNodes(ParserSets.DADOS_GERAIS_SET, nodoRaiz);
		// TODO Currículo sem ID! Desatualizado!
		pesquisadorResult.setIdCurriculo(Long.parseLong(pesquisadorList.get(2)));
		pesquisadorResult
				.setUltimaAtualizacao(SilqDataUtils.formatDates(pesquisadorList.get(0), pesquisadorList.get(1)));

		List<String> nomeList = AttributeGetter.iterateNodes(ParserSets.NOME_SET, nodoRaiz);
		if (nomeList.size() == 1) {
			pesquisadorResult.setNome(nomeList.get(0));
		}

		return pesquisadorResult;
	}

	public ParseResult parseCurriculaAvaliacao(Document document, AvaliarForm form, AvaliacaoType tipoAvaliacao) {
		return this.parseCurricula(document, form, tipoAvaliacao);
	}

	public ParseResult parseCurriculaAvaliacao(File file, AvaliarForm form, AvaliacaoType tipoAvaliacao) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;

		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(file));
			document.getDocumentElement().normalize();

			return this.parseCurricula(document, form, tipoAvaliacao);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ParseResult parseCurricula(Document document, AvaliarForm form, AvaliacaoType tipoAvaliacao) {
		ParseResult parseResult = new ParseResult();
		parseResult.setAreaAvaliada(form.area);

		String anoPublicacaoDe = form.getAnoPublicacaoDe();
		if (anoPublicacaoDe == null || anoPublicacaoDe.equals("")) {
			parseResult.setAnoPublicacaoDe(1985 + "");
		} else {
			parseResult.setAnoPublicacaoDe(anoPublicacaoDe);
		}

		String anoPublicacaoAte = form.getAnoPublicacaoAte();
		if (anoPublicacaoAte == null || anoPublicacaoDe.equals("")) {
			parseResult.setAnoPublicacaoAte(10000 + ""); // TODO Pegar data
															// atual;
		} else {

			parseResult.setAnoPublicacaoAte(anoPublicacaoAte);
		}

		Node raiz = null;

		try {
			document.getDocumentElement().normalize();
			NodeList qualisList = document.getElementsByTagName("CURRICULO-VITAE");
			raiz = qualisList.item(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> nomeList = AttributeGetter.iterateNodes(ParserSets.NOME_SET, raiz);
		if (nomeList.size() == 1) {
			parseResult.setNome(nomeList.get(0));
		}

		List<String> areas = AttributeGetter.iterateNodes(ParserSets.AREAS_SET, raiz);
		if (areas.size() > 3) {
			AreaConhecimento areaConhecimento = new AreaConhecimento();
			areaConhecimento.setNomeArea(areas.get(0));
			areaConhecimento.setNomeGrandeArea(areas.get(3));

			parseResult.setNomeEspecialidade(areas.get(1));
			parseResult.setNomeSubAreaConhecimento(areas.get(2));
			parseResult.setAreaGrandeAreaConhecimento(areaConhecimento);
		}

		List<String> trabalhos = AttributeGetter.iterateNodes(ParserSets.PRODUCOES_SET, raiz);
		if (trabalhos.size() > 0) {
			List<Trabalho> trabalhosParse = new ArrayList<>();
			for (int i = 0; i < trabalhos.size(); i += 3) {
				Trabalho trabalho = new Trabalho();

				trabalho.setAnoPublicacao(ConverterHelper.parseIntegerSafely(trabalhos.get(i)));
				trabalho.setTituloTrabalho(trabalhos.get(i + 1));
				trabalho.setNomeEvento(trabalhos.get(i + 2));

				trabalhosParse.add(trabalho);
			}

			parseResult.setTrabalhos(trabalhosParse);
		}

		List<Artigo> artigos = ArtigoAttributeGetter.iterateUntilArtigos(raiz);
		parseResult.setArtigos(artigos);

		this.compareSimilarity.compare(parseResult, form, tipoAvaliacao);

		parseResult.order();

		return parseResult;
	}
}
