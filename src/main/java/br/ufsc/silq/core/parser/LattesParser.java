package br.ufsc.silq.core.parser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.parser.attribute.ArtigoAttributeGetter;
import br.ufsc.silq.core.parser.attribute.AttributeGetter;
import br.ufsc.silq.core.parser.dto.AreaConhecimento;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.NaturezaPublicacao;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.service.DocumentManager;
import br.ufsc.silq.core.utils.SilqDataUtils;
import br.ufsc.silq.core.utils.SilqStringUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LattesParser {

	@Inject
	private DocumentManager documentManager;

	/**
	 * Extrai dados dos trabalhos e artigos de um pesquisador a partir de seu currículo Lattes (em XML).
	 *
	 * @param lattes A entidade que representa o currículo Lattes (em XML) a ser avaliado.
	 * @return Os resultados ({@link ParseResult}) da avaliação.
	 * @throws SilqLattesException Caso ocorra um erro ao converter o Lattes.
	 */
	@Cacheable(cacheNames = "parseResults", key = "#lattes.id")
	public ParseResult parseCurriculum(CurriculumLattes lattes) throws SilqLattesException {
		return this.parseCurriculum(this.documentManager.stringToDocument(lattes.getLattesXml()));
	}

	/**
	 * Extrai dados gerais do currículo Lattes (em XML) de um pesquisador
	 *
	 * @param lattes Currículo Lattes em XML do pesquisador.
	 * @return O {@link DadosGeraisResult} do pesquisador extraídos do currículo.
	 */
	public DadosGeraisResult extractDadosGerais(Document lattes) {
		DadosGeraisResult dadosGeraisResult = new DadosGeraisResult();

		Node nodoRaiz = this.getNodoRaiz(lattes);

		List<String> dadoGeralList = AttributeGetter.iterateNodes(ParserSets.DADOS_GERAIS_SET, nodoRaiz);
		dadosGeraisResult.setIdCurriculo(dadoGeralList.get(2));
		dadosGeraisResult.setUltimaAtualizacao(SilqDataUtils.formatDates(dadoGeralList.get(0), dadoGeralList.get(1)));

		List<String> areas = AttributeGetter.iterateNodes(ParserSets.AREAS_SET, nodoRaiz);
		if (areas.size() > 3) {
			AreaConhecimento areaConhecimento = new AreaConhecimento();
			areaConhecimento.setNomeArea(SilqStringUtils.setHifenIfVazio(areas.get(0)));
			areaConhecimento.setNomeGrandeArea(SilqStringUtils.setHifenIfVazio(areas.get(3)));
			areaConhecimento.setNomeEspecialidade(SilqStringUtils.setHifenIfVazio(areas.get(1)));
			areaConhecimento.setNomeSubAreaConhecimento(SilqStringUtils.setHifenIfVazio(areas.get(2)));
			dadosGeraisResult.setAreaConhecimento(areaConhecimento);
		}

		List<String> nomeList = AttributeGetter.iterateNodes(ParserSets.NOME_SET, nodoRaiz);
		if (nomeList.size() == 1) {
			dadosGeraisResult.setNome(nomeList.get(0));
		}

		return dadosGeraisResult;
	}

	/**
	 * Extrai dados dos trabalhos e artigos de um pesquisador a partir de seu currículo Lattes (em XML).
	 *
	 * @param lattes Currículo Lattes (em XML) a ser avaliado.
	 * @return Os resultados ({@link ParseResult}) da avaliação.
	 */
	protected ParseResult parseCurriculum(Document lattes) {
		ParseResult parseResult = new ParseResult();

		Node raiz = this.getNodoRaiz(lattes);

		DadosGeraisResult dadosGerais = this.extractDadosGerais(lattes);
		parseResult.setDadosGerais(dadosGerais);

		List<String> trabalhos = AttributeGetter.iterateNodes(ParserSets.PRODUCOES_SET, raiz);
		if (!trabalhos.isEmpty()) {
			List<Trabalho> trabalhosParse = new ArrayList<>();
			for (int i = 0; i < trabalhos.size(); i += 4) {
				Integer ano = ArtigoAttributeGetter.parseIntegerSafely(trabalhos.get(i));
				NaturezaPublicacao natureza = NaturezaPublicacao.parse(trabalhos.get(i + 1));
				String titulo = trabalhos.get(i + 2);
				String tituloVeiculo = trabalhos.get(i + 3);

				trabalhosParse.add(new Trabalho(titulo, ano, tituloVeiculo, natureza));
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
