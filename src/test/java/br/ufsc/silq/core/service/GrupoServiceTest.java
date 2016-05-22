package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.CurriculumLattesRepository;
import br.ufsc.silq.core.persistence.repository.GrupoRepository;

public class GrupoServiceTest extends WebContextTest {

	@Inject
	GrupoService grupoService;

	@Inject
	GrupoRepository grupoRepository;

	@Inject
	CurriculumLattesRepository curriculumRepository;

	private GrupoForm grupoForm;

	private Usuario usuarioLogado;

	@Before
	public void setup() {
		this.usuarioLogado = this.loginUser();
		this.grupoForm = new GrupoForm();
		this.grupoForm.setNomeArea("Ciência da Computação");
		this.grupoForm.setNomeGrupo("Grupo 1 de Testes");
		this.grupoForm.setNomeInstituicao("UFSC");
	}

	@Test
	public void testCreate() {
		Assertions.assertThat(this.grupoRepository.count()).isEqualTo(0);
		Grupo grupo = this.grupoService.create(this.grupoForm);

		Assertions.assertThat(this.grupoRepository.count()).isEqualTo(1);
		Assertions.assertThat(grupo.getNomeArea()).isEqualTo(this.grupoForm.getNomeArea());
		Assertions.assertThat(grupo.getNomeGrupo()).isEqualTo(this.grupoForm.getNomeGrupo());
		Assertions.assertThat(grupo.getNomeInstituicao()).isEqualTo(this.grupoForm.getNomeInstituicao());
		Assertions.assertThat(grupo.getCoordenador()).isEqualTo(this.usuarioLogado);
		Assertions.assertThat(grupo.getPesquisadores()).isNullOrEmpty();
	}

	@Test
	public void testAddPesquisador() throws SilqException {
		Grupo grupo = this.grupoService.create(this.grupoForm);

		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(0);

		this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.RONALDO_ZIP_UPLOAD);
		Assertions.assertThat(grupo.getPesquisadores()).hasSize(1);

		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1)
				.as("Deve ter adicionado um currículo na base de currículos");

		this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.CHRISTIANE_XML_UPLOAD);
		Assertions.assertThat(grupo.getPesquisadores()).hasSize(2);

		// Adiciona o mesmo currículo já adicionado:
		this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.RONALDO_ZIP_UPLOAD);
		Assertions.assertThat(grupo.getPesquisadores()).hasSize(2)
				.as("Não deve duplicar pesquisadores em um mesmo grupo");
	}

	@Test
	public void testRemovePesquisador() throws SilqException {
		Grupo grupo = this.grupoService.create(this.grupoForm);

		CurriculumLattes curriculo1 = this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.RONALDO_ZIP_UPLOAD);
		CurriculumLattes curriculo2 = this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.CHRISTIANE_XML_UPLOAD);
		Assertions.assertThat(grupo.getPesquisadores()).hasSize(2);

		this.grupoService.removePesquisador(grupo, curriculo1.getId());
		Assertions.assertThat(grupo.getPesquisadores()).hasSize(1)
				.as("Deve ter excluído um dos pesquisadores");
		Assertions.assertThat(grupo.getPesquisadores()).contains(curriculo2)
				.as("Currículo não excluído deve permanecer no grupo de pesquisadores");
	}

	@Test
	public void testDelete() throws SilqException {
		Grupo grupo = this.grupoService.create(this.grupoForm);
		this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.RONALDO_ZIP_UPLOAD);

		Assertions.assertThat(this.grupoRepository.count()).isEqualTo(1);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1);
		this.grupoService.delete(grupo);

		Assertions.assertThat(this.grupoRepository.count()).isEqualTo(0)
				.as("Deve ter deletado o grupo da base de dados");

		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(0)
				.as("Deve ter deletado o currículo dos pesquisadores do grupo deletado da base de dados");
	}

}
