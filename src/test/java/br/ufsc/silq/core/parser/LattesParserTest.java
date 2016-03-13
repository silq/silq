package br.ufsc.silq.core.parser;

import java.io.File;
import java.text.ParseException;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.PesquisadorResult;

public class LattesParserTest {

	public static final File CURRICULUM_CHRISTIANE = new File("src/test/resources/fixtures/curricula/christiane.xml");
	public static final File CURRICULUM_RAUL = new File("src/test/resources/fixtures/curricula/raul.xml");
	public static final File CURRICULUM_RONALDO = new File("src/test/resources/fixtures/curricula/ronaldo.xml");

	private LattesParser lattesParser = new LattesParser();

	@Test
	public void testParseCurriculaDadosGerais() throws ParseException, SilqErrorException {
		DadosGeraisResult dados = this.lattesParser.parseCurriculaDadosGerais(CURRICULUM_CHRISTIANE);
		// System.out.println(dados);

		Assertions.assertThat(dados.getNome()).isEqualTo("Christiane Anneliese Gresse von Wangenheim");
		Assertions.assertThat(dados.getAreaGrandeAreaConhecimento().toString())
				.isEqualTo("Ciência da Computação - Ciências Exatas e da Terra");
		Assertions.assertThat(dados.getNomeSubAreaConhecimento()).isEqualTo("Metodologia e Técnicas da Computação");
		Assertions.assertThat(dados.getIdCurriculo()).isEqualTo("3879944876244096");

		Assertions.assertThat(this.lattesParser.parseCurriculaDadosGerais(CURRICULUM_RAUL)).isNotNull();
		Assertions.assertThat(this.lattesParser.parseCurriculaDadosGerais(CURRICULUM_RONALDO)).isNotNull();
	}

	@Test
	public void testParseCurriculaPesquisador() throws SilqErrorException {
		PesquisadorResult result = this.lattesParser.parseCurriculaPesquisador(CURRICULUM_RAUL);
		// System.out.println(result);

		Assertions.assertThat(result.getNome()).isEqualTo("Raul Sidnei Wazlawick");
		Assertions.assertThat(result.getIdCurriculo()).isEqualTo(7541399131195077L);

		Assertions.assertThat(this.lattesParser.parseCurriculaPesquisador(CURRICULUM_CHRISTIANE)).isNotNull();
		Assertions.assertThat(this.lattesParser.parseCurriculaPesquisador(CURRICULUM_RONALDO)).isNotNull();
	}

}
