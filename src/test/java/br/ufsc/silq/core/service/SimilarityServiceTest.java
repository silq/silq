package br.ufsc.silq.core.service;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.Conceituavel;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.service.SimilarityService.TipoAvaliacao;
import br.ufsc.silq.test.WebContextTest;
import lombok.Data;

public class SimilarityServiceTest extends WebContextTest {

	@Inject
	SimilarityService similarityService;
	private AvaliarForm avaliarForm;

	@Data
	public static class ConcreteConceituavel implements Conceituavel {
		private final String tituloVeiculo;
		private final Integer ano;
	}

	@Before
	public void setUp() {
		this.avaliarForm = new AvaliarForm();
		this.avaliarForm.setArea("Ciência da Computação");
		this.avaliarForm.setNivelSimilaridade(NivelSimilaridade.MUITO_BAIXO);
	}

	@Test
	public void testGetConceitos() throws SQLException {
		ConcreteConceituavel c1 = new ConcreteConceituavel("Testing", 2010);
		List<Conceito> conceitos = this.similarityService.getConceitos(c1, this.avaliarForm, TipoAvaliacao.PERIODICO);
		Assertions.assertThat(conceitos).isNotEmpty();
	}

	@Test
	public void testCalcularSimilaridade() {
		Assertions.assertThat(this.similarityService.calcularSimilaridade("abc", "ab").getValue()).isEqualTo(0.4f);
		Assertions.assertThat(this.similarityService.calcularSimilaridade("abc", "abc").getValue()).isEqualTo(1f);
		Assertions.assertThat(this.similarityService.calcularSimilaridade("abc", "z").getValue()).isEqualTo(0f);
	}

}
