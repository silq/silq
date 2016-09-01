package br.ufsc.silq.core.data;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class ConceituadoTest {

	private Conceito conceito1;
	private Conceito conceito2;
	private Conceito conceito1b;
	private Conceituado<String> conceituado;

	@Before
	public void setUp() {
		this.conceituado = new Conceituado<>("Test");

		this.conceito1 = new Conceito(1L, "Título 1", "B1", new NivelSimilaridade(0.87f), 2012);
		this.conceito2 = new Conceito(2L, "Título 2", "B2", new NivelSimilaridade(0.32f), 2010);
		this.conceito1b = new Conceito(1L, "Título 1", "B1", NivelSimilaridade.TOTAL, 2012);
		this.conceito1b.setFlagged(true);

		Assertions.assertThat(this.conceito1).isEqualTo(this.conceito1b);
	}

	@Test
	public void testAddConceitosIguais() {
		this.conceituado.addConceito(this.conceito1b);
		this.conceituado.addConceito(this.conceito1);
		this.conceituado.addConceito(this.conceito2);

		Assertions.assertThat(this.conceituado.getConceitos()).hasSize(2);
		Assertions.assertThat(this.conceituado.getConceitos().get(0).getSimilaridade()).isEqualTo(1f);
		Assertions.assertThat(this.conceituado.getConceitos().get(0).getId()).isEqualTo(1L);
		Assertions.assertThat(this.conceituado.getConceitos().get(1).getId()).isEqualTo(2L);
	}

	@Test
	public void testAddAllConceitosIguais() {
		this.conceituado.addConceitos(Arrays.asList(this.conceito1b, this.conceito2, this.conceito1));

		Assertions.assertThat(this.conceituado.getConceitos()).hasSize(2);
		Assertions.assertThat(this.conceituado.getConceitos().get(0).getSimilaridade()).isEqualTo(1f);
	}

	@Test
	public void testConstructorConceitosIguais() {
		this.conceituado = new Conceituado<>("Test 2", Arrays.asList(this.conceito1b, this.conceito2, this.conceito1));

		Assertions.assertThat(this.conceituado.getConceitos()).hasSize(2);
		Assertions.assertThat(this.conceituado.getConceitos().get(0).getSimilaridade()).isEqualTo(1f);
	}

}
