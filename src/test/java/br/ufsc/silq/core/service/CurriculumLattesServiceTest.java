package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import br.ufsc.silq.Fixtures;
import br.ufsc.silq.WebContextTest;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;

public class CurriculumLattesServiceTest extends WebContextTest {

	@Inject
	private CurriculumLattesService curriculumService;

	@Test
	public void testSaveComReuso() throws SilqException {
		CurriculumLattes lattes1 = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		CurriculumLattes lattes2 = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		Assertions.assertThat(lattes2.getId()).isEqualTo(lattes1.getId());
	}

}
