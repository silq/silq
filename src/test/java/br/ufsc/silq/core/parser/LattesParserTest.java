package br.ufsc.silq.core.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;

import org.junit.Test;

import br.ufsc.silq.core.dto.parser.struct.DadosGeraisResult;
import br.ufsc.silq.core.exceptions.SilqErrorException;

public class LattesParserTest {

	public static final File CURRICULUM_CHRISTIANE = new File("src/test/resources/fixtures/curricula/christiane.xml");;

	@Test
	public void testParseCurriculaDadosGerais() throws ParseException, SilqErrorException {
		DadosGeraisResult dados = LattesParser.parseCurriculaDadosGerais(CURRICULUM_CHRISTIANE);
		// System.out.println(dados);

		assertEquals("Christiane Anneliese Gresse von Wangenheim", dados.getNome());
		assertEquals("Ciência da Computação - Ciências Exatas e da Terra",
				dados.getAreaGrandeAreaConhecimento().toString());
		assertEquals("Metodologia e Técnicas da Computação", dados.getNomeSubAreaConhecimento());
		assertEquals("3879944876244096", dados.getIdCurriculo());
	}

}
