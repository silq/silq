package br.ufsc.silq.core.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import br.ufsc.silq.Fixtures;
import br.ufsc.silq.WebContextTest;
import br.ufsc.silq.core.business.service.DocumentManager;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.PesquisadorResult;

public class LattesParserTest extends WebContextTest {

	public Document documentXmlChristiane;
	public Document documentXmlRaul;
	public Document documentXmlRonaldo;

	@Inject
	private LattesParser lattesParser;

	@Inject
	private DocumentManager documentManager;

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
	public void testParseCurriculaPesquisador() throws SilqException {
		PesquisadorResult result = this.lattesParser.parseCurriculumPesquisador(this.documentXmlRaul);
		// System.out.println(result);

		Assertions.assertThat(result.getNome()).isEqualTo("Raul Sidnei Wazlawick");
		Assertions.assertThat(result.getIdCurriculo()).isEqualTo(7541399131195077L);

		Assertions.assertThat(this.lattesParser.parseCurriculumPesquisador(this.documentXmlChristiane)).isNotNull();
		Assertions.assertThat(this.lattesParser.parseCurriculumPesquisador(this.documentXmlRonaldo)).isNotNull();
	}

}
