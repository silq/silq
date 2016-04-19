package br.ufsc.silq.core.parser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufsc.silq.core.business.service.DocumentManager;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.parser.attribute.ArtigoAttributeGetter;
import br.ufsc.silq.core.parser.attribute.AttributeGetter;
import br.ufsc.silq.core.parser.dto.AreaConhecimento;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.PesquisadorResult;
import br.ufsc.silq.core.parser.dto.TipoOrigemCurriculo;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.utils.SilqDataUtils;
import br.ufsc.silq.core.utils.SilqStringUtils;
import br.ufsc.silq.core.utils.parser.ConverterHelper;

@Component
public class LattesParser {

	@Inject
	private DocumentManager documentManager;

	/**
	 * Extrai os dados gerais do currículo Lattes (em XML) de um pesquisador
	 *
	 * @param file Currículo Lattes em XML do pesquisador.
	 * @return Os dados gerais do pesquisador extraídos do currículo.
	 * @throws SilqException
	 */
	public DadosGeraisResult parseDadosGerais(Document curriculumXml) throws SilqException {
		DadosGeraisResult dadosGeraisResult = new DadosGeraisResult();

		Node nodoRaiz = this.getNodoRaiz(curriculumXml);

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

	/**
	 * Extrai alguns dados gerais do currículo Lattes (em XML) de um pesquisador.
	 *
	 * @param curriculumXml Currículo Lattes em XML do pesquisador.
	 * @return
	 * @throws SilqException
	 */
	public PesquisadorResult parseCurriculumPesquisador(Document curriculumXml) {
		PesquisadorResult pesquisadorResult = new PesquisadorResult();

		Node nodoRaiz = this.getNodoRaiz(curriculumXml);

		List<String> pesquisadorList = AttributeGetter.iterateNodes(ParserSets.DADOS_GERAIS_SET, nodoRaiz);
		// TODO Currículo sem ID! Desatualizado!
		pesquisadorResult.setIdCurriculo(Long.parseLong(pesquisadorList.get(2)));
		pesquisadorResult.setUltimaAtualizacao(SilqDataUtils.formatDates(pesquisadorList.get(0), pesquisadorList.get(1)));

		List<String> nomeList = AttributeGetter.iterateNodes(ParserSets.NOME_SET, nodoRaiz);
		if (nomeList.size() == 1) {
			pesquisadorResult.setNome(nomeList.get(0));
		}

		return pesquisadorResult;
	}

	/**
	 * Extrai dados dos trabalhos e artigos de um pesquisador a partir de seu currículo Lattes (em XML).
	 *
	 * @param curriculum Byte array do Currículo Lattes (em XML) a ser avaliado.
	 * @return Os resultados ({@link ParseResult}) da avaliação.
	 * @throws SilqLattesException
	 */
	public ParseResult parseCurriculum(byte[] curriculum) throws SilqLattesException {
		return this.parseCurriculum(this.documentManager.stringToDocument(new String(curriculum)));
	}

	/**
	 * Extrai dados dos trabalhos e artigos de um pesquisador a partir de seu currículo Lattes (em XML).
	 *
	 * @param document Currículo Lattes (em XML) a ser avaliado.
	 * @return Os resultados ({@link ParseResult}) da avaliação.
	 * @throws SilqLattesException Caso o documento XML enviado não seja um currículo Lattes válido.
	 */
	public ParseResult parseCurriculum(Document document) throws SilqLattesException {
		ParseResult parseResult = new ParseResult();

		Node raiz = this.getNodoRaiz(document);

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
				String titulo = trabalhos.get(i + 1);
				Integer ano = ConverterHelper.parseIntegerSafely(trabalhos.get(i));
				String tituloVeiculo = trabalhos.get(i + 2);

				trabalhosParse.add(new Trabalho(titulo, ano, tituloVeiculo));
			}

			parseResult.setTrabalhos(trabalhosParse);
		}

		List<Artigo> artigos = ArtigoAttributeGetter.iterateUntilArtigos(raiz);
		parseResult.setArtigos(artigos);

		parseResult.order();
		return parseResult;
	}

	protected Node getNodoRaiz(Document document) {
		NodeList qualisList = document.getElementsByTagName("CURRICULO-VITAE");
		return qualisList.item(0);
	}
}
