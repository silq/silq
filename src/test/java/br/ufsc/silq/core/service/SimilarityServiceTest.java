package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import br.ufsc.silq.test.WebContextTest;

public class SimilarityServiceTest extends WebContextTest {

	@Inject
	SimilarityService similarityService;

	@Test
	public void testCalcularSimilaridade() {
		Assertions.assertThat(this.similarityService.calcularSimilaridade("abc", "ab").getValue()).isEqualTo(0.4f);
		Assertions.assertThat(this.similarityService.calcularSimilaridade("abc", "abc").getValue()).isEqualTo(1f);
		Assertions.assertThat(this.similarityService.calcularSimilaridade("abc", "z").getValue()).isEqualTo(0f);
	}

}
