package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class ConceitoTest {

	private ArrayList<Conceito> conceitos = new ArrayList<>();
	private Conceito conceito1;
	private Conceito conceito2;
	private Conceito conceito3;

	@Before
	public void setUp() {
		this.conceito1 = new Conceito(1L, "Título 1", "A1", new NivelSimilaridade(0.5F), 2009);
		this.conceito2 = new Conceito(2L, "Título 2", "A2", new NivelSimilaridade(0.3F), 2012);
		this.conceito3 = new Conceito(3L, "Título 3", "A3", new NivelSimilaridade(0.75F), 2010);

		this.conceitos.clear();
		this.conceitos.add(this.conceito1);
		this.conceitos.add(this.conceito2);
		this.conceitos.add(this.conceito3);
	}

	@Test
	public void testCompare() {
		Collections.sort(this.conceitos);
		Assertions.assertThat(this.conceitos.get(0)).isSameAs(this.conceito3);
		Assertions.assertThat(this.conceitos.get(1)).isSameAs(this.conceito1);
		Assertions.assertThat(this.conceitos.get(2)).isSameAs(this.conceito2);
	}

	@Test
	public void testCompareFlagged() {
		Conceito conceitoFlagged = new Conceito(1L, "Título Feedback", "B1", new NivelSimilaridade(0.4F), 2016);
		conceitoFlagged.setFeedback(true);
		this.conceitos.add(conceitoFlagged);

		Collections.sort(this.conceitos);
		Assertions.assertThat(this.conceitos.get(0)).isSameAs(conceitoFlagged);
		Assertions.assertThat(this.conceitos.get(1)).isSameAs(this.conceito3);
		Assertions.assertThat(this.conceitos.get(2)).isSameAs(this.conceito1);
		Assertions.assertThat(this.conceitos.get(3)).isSameAs(this.conceito2);
	}

}
