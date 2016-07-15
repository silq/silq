package br.ufsc.silq.core.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;
import org.w3c.dom.Document;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.service.CurriculumLattesService;
import br.ufsc.silq.core.service.DocumentManager;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LattesParserTest extends WebContextTest {

	public Document documentXmlChristiane;
	public Document documentXmlRaul;
	public Document documentXmlRonaldo;

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
	}

	@Test
	public void testParseCurriculaDadosGerais() throws ParseException, SilqException {
		DadosGeraisResult dados = this.lattesParser.extractDadosGerais(this.documentXmlChristiane);
		// System.out.println(dados);

		Assertions.assertThat(dados.getNome()).isEqualTo("Christiane Anneliese Gresse von Wangenheim");
		Assertions.assertThat(dados.getAreaGrandeAreaConhecimento().toString())
				.isEqualTo("Ciência da Computação - Ciências Exatas e da Terra");
		Assertions.assertThat(dados.getNomeSubAreaConhecimento()).isEqualTo("Metodologia e Técnicas da Computação");
		Assertions.assertThat(dados.getIdCurriculo()).isEqualTo("3879944876244096");

		Assertions.assertThat(this.lattesParser.extractDadosGerais(this.documentXmlRaul)).isNotNull();
		Assertions.assertThat(this.lattesParser.extractDadosGerais(this.documentXmlRonaldo)).isNotNull();
	}

	@Test
	public void testParseWithCache() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);

		StopWatch watch1 = new StopWatch();
		watch1.start();
		this.lattesParser.parseCurriculum(lattes);
		watch1.stop();
		log.info("First curriculum parse (WITHOUT CACHE) finished in {}ms", watch1.getTotalTimeMillis());

		// Salva um outro currículo entre uma operação e outra só para garantir que o cache anterior não é limpo
		this.curriculumService.saveFromUpload(Fixtures.GUNTZEL_ZIP_UPLOAD);

		StopWatch watch2 = new StopWatch();
		watch2.start();
		this.lattesParser.parseCurriculum(lattes);
		watch2.stop();
		log.info("Second curriculum parse (WITH CACHE) finished in {}ms", watch2.getTotalTimeMillis());

		Assertions.assertThat(watch2.getLastTaskTimeMillis() * 10).isLessThan(watch1.getLastTaskTimeMillis())
				.as("Segundo parse deve ser cacheado e ao menos 10x mais rápido do que o comum");
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
