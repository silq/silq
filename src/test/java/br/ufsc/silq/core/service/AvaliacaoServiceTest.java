package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;

public class AvaliacaoServiceTest extends WebContextTest {

	@Inject
	AvaliacaoService avaliacaoService;

	@Inject
	CurriculumLattesService curriculumService;

	private AvaliarForm avaliarForm;

	@Before
	public void setup() {
		this.avaliarForm = new AvaliarForm();
		this.avaliarForm.setArea("Ciência da Computação");
	}

	@Test
	public void test() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.GUNTZEL_ZIP_UPLOAD);
		AvaliacaoResult result = this.avaliacaoService.avaliar(lattes, this.avaliarForm);

		Assertions.assertThat(result.getArtigos()).isNotEmpty();
		Assertions.assertThat(result.getTrabalhos()).isNotEmpty();
		Assertions.assertThat(result.getDadosGerais().getNome()).isEqualTo("José Luís Almada Güntzel");
		Assertions.assertThat(result.getStats().getTotalizador()).isNotEmpty();
		Assertions.assertThat(result.getArtigos().get(0).getConceitos()).isNotEmpty();
		Assertions.assertThat(result.getTrabalhos().get(0).getConceitos()).isNotEmpty();
	}

}
