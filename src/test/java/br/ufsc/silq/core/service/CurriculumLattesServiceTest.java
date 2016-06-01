package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.persistence.repository.CurriculumLattesRepository;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;

public class CurriculumLattesServiceTest extends WebContextTest {

	@Inject
	private CurriculumLattesService curriculumService;

	@Inject
	private CurriculumLattesRepository curriculumRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private GrupoService grupoService;

	@Test
	public void testSaveComReuso() throws SilqException {
		CurriculumLattes lattes1 = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		CurriculumLattes lattes2 = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		Assertions.assertThat(lattes2.getId()).isEqualTo(lattes1.getId());
	}

	@Test
	public void testIsCurriculumEmUso() throws SilqException {
		CurriculumLattes lattes1 = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		Assertions.assertThat(this.curriculumService.isCurriculumEmUso(lattes1)).isFalse();

		this.loginUser();
		CurriculumLattes lattes2 = this.usuarioService.saveCurriculumUsuarioLogado(Fixtures.RAUL_ZIP_UPLOAD);
		Assertions.assertThat(this.curriculumService.isCurriculumEmUso(lattes2)).isTrue();

		this.usuarioService.removeCurriculumUsuarioLogado();
		Assertions.assertThat(this.curriculumService.isCurriculumEmUso(lattes2)).isFalse();
	}

	@Test
	public void testCleanCurriculosEmDesuso() throws SilqException {
		Assertions.assertThat(this.curriculumService.cleanCuriculosEmDesuso()).isEqualTo(0);

		this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		this.curriculumService.saveFromUpload(Fixtures.RAUL_ZIP_UPLOAD);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(2);
		Assertions.assertThat(this.curriculumService.cleanCuriculosEmDesuso()).isEqualTo(2)
				.as("Deve remover dois currículos");
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(0)
				.as("Deve ter excluído ambos os currículos da base de dados");

	}

	@Test
	public void testCleanCurriculosEmDesusoUsuarioLogado() throws SilqException {
		this.loginUser();
		this.usuarioService.saveCurriculumUsuarioLogado(Fixtures.RONALDO_ZIP_UPLOAD);
		this.curriculumService.saveFromUpload(Fixtures.RONALDO_ZIP_UPLOAD);
		this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(2);
		Assertions.assertThat(this.curriculumService.cleanCuriculosEmDesuso()).isEqualTo(1)
				.as("Deve remover um currículo");
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1)
				.as("Deve ter excluído um dos currículos da base de dados");
	}

	@Test
	public void testCleanCurriculosEmDesusoPesquisadores() throws SilqException {
		this.loginUser();
		GrupoForm grupoForm = new GrupoForm();
		grupoForm.setNomeArea("Ciência da Computação");
		grupoForm.setNomeGrupo("Grupo 1 de Testes");
		grupoForm.setNomeInstituicao("UFSC");
		Grupo grupo = this.grupoService.create(grupoForm);

		this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.RAUL_XML_UPLOAD);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1);
		Assertions.assertThat(this.curriculumService.cleanCuriculosEmDesuso()).isEqualTo(0);

		this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_XML_UPLOAD);
		this.curriculumService.saveFromUpload(Fixtures.RAUL_XML_UPLOAD);
		this.curriculumService.saveFromUpload(Fixtures.RONALDO_ZIP_UPLOAD);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(3);

		Assertions.assertThat(this.curriculumService.cleanCuriculosEmDesuso()).isEqualTo(2);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1);
	}

	@Test
	public void testSaveCurriculumSemId() throws SilqException {
		CurriculumLattes lattes1 = this.curriculumService.saveFromUpload(Fixtures.SEM_ID_ZIP_UPLOAD);
		CurriculumLattes lattes2 = this.curriculumService.saveFromUpload(Fixtures.SEM_ID_ZIP_UPLOAD);
		Assertions.assertThat(lattes1).isNotEqualTo(lattes2);
	}
}
