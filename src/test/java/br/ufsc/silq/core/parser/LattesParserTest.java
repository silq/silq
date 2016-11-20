package br.ufsc.silq.core.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;
import org.w3c.dom.Document;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.NaturezaPublicacao;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.service.CurriculumLattesService;
import br.ufsc.silq.core.service.DocumentManager;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;
import br.ufsc.silq.test.asserts.CacheAssert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LattesParserTest extends WebContextTest {

	public Document documentXmlChristiane;
	public Document documentXmlRaul;
	public Document documentXmlRonaldo;
	public Document documentXmlCarina;

	@Inject
	private LattesParser lattesParser;

	@Inject
	private DocumentManager documentManager;

	@Inject
	private CurriculumLattesService curriculumService;

	@Before
	public void setup() throws FileNotFoundException, IOException, SilqLattesException {
		this.documentXmlChristiane = this.documentManager.extractXmlDocumentFromUpload(Fixtures.CHRISTIANE_XML_UPLOAD);
		this.documentXmlRaul = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RAUL_XML_UPLOAD);
		this.documentXmlRonaldo = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RONALDO_XML_UPLOAD);
		this.documentXmlCarina = this.documentManager.extractXmlDocumentFromUpload(Fixtures.CARINA_XML_UPLOAD);
	}

	@Test
	public void testParseCurriculaDadosGerais() throws ParseException, SilqException {
		DadosGeraisResult dados = this.lattesParser.extractDadosGerais(this.documentXmlChristiane);
		// System.out.println(dados);

		Assertions.assertThat(dados.getNome()).isEqualTo("Christiane Anneliese Gresse von Wangenheim");
		Assertions.assertThat(dados.getAreaConhecimento().toString())
				.isEqualTo("Ciência da Computação - Ciências Exatas e da Terra");
		Assertions.assertThat(dados.getAreaConhecimento().getNomeSubAreaConhecimento())
				.isEqualTo("Metodologia e Técnicas da Computação");
		Assertions.assertThat(dados.getIdCurriculo()).isEqualTo("3879944876244096");

		Assertions.assertThat(this.lattesParser.extractDadosGerais(this.documentXmlRaul)).isNotNull();
		Assertions.assertThat(this.lattesParser.extractDadosGerais(this.documentXmlRonaldo)).isNotNull();
	}

	@Test
	public void testParseNaturezaPublicacao() {
		Assertions.assertThat(NaturezaPublicacao.parse("dalsldas")).isEqualTo(NaturezaPublicacao.OUTROS);

		ParseResult parseResult = this.lattesParser.parseCurriculum(this.documentXmlCarina);

		List<Trabalho> completos = parseResult.getTrabalhos().stream()
				.filter(t -> NaturezaPublicacao.COMPLETO.equals(t.getNatureza()))
				.collect(Collectors.toList());
		Assertions.assertThat(completos).hasSize(45);

		List<Trabalho> resumosExpandidos = parseResult.getTrabalhos().stream()
				.filter(t -> NaturezaPublicacao.RESUMO_EXPANDIDO.equals(t.getNatureza()))
				.collect(Collectors.toList());
		Assertions.assertThat(resumosExpandidos).hasSize(13);

		List<Trabalho> resumos = parseResult.getTrabalhos().stream()
				.filter(t -> NaturezaPublicacao.RESUMO.equals(t.getNatureza()))
				.collect(Collectors.toList());
		Assertions.assertThat(resumos).hasSize(2);
	}

	@Test
	public void testParseWithCache() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		CacheAssert.assertThat(() -> {
			try {
				return this.lattesParser.parseCurriculum(lattes);
			} catch (SilqLattesException e) {
				log.error("", e);
			}
			return null;
		}).isCached();
	}

	@Test
	public void testParseBenchmark() {
		StopWatch watch = new StopWatch();
		watch.start();
		this.lattesParser.parseCurriculum(this.documentXmlChristiane);
		watch.stop();

		log.info("Parsing do Lattes realizado em " + watch.getTotalTimeMillis() + "ms");
	}
}
