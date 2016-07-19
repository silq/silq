package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.AvaliacaoCollectionResult;
import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;
import br.ufsc.silq.test.asserts.CacheAssert;

public class AvaliacaoServiceTest extends WebContextTest {

	@Inject
	AvaliacaoService avaliacaoService;

	@Inject
	CurriculumLattesService curriculumService;

	@Inject
	GrupoService grupoService;

	private AvaliarForm avaliarForm;

	@Before
	public void setup() {
		this.avaliarForm = new AvaliarForm();
		this.avaliarForm.setArea("Ciência da Computação");
	}

	@Test
	public void testAvaliar() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.GUNTZEL_ZIP_UPLOAD);
		AvaliacaoResult result = this.avaliacaoService.avaliar(lattes, this.avaliarForm);

		Assertions.assertThat(result.getForm()).isEqualTo(this.avaliarForm);
		Assertions.assertThat(result.getArtigos()).isNotEmpty();
		Assertions.assertThat(result.getTrabalhos()).isNotEmpty();
		Assertions.assertThat(result.getDadosGerais().getNome()).isEqualTo("José Luís Almada Güntzel");
		Assertions.assertThat(result.getStats().getTotalizador()).isNotEmpty();
		Assertions.assertThat(result.getArtigos().first().hasConceito()).isTrue();
		Assertions.assertThat(result.getTrabalhos().first().hasConceito()).isTrue();
	}

	@Test
	public void testAvaliarCache() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		CacheAssert.assertThat(() -> {
			return this.avaliacaoService.avaliar(lattes, this.avaliarForm);
		}).isCached();
	}

	@Test
	public void testAvaliarColecction() throws SilqException {
		this.loginUser();
		GrupoForm grupoForm = new GrupoForm();
		grupoForm.setNomeGrupo("Grupo de testes #1");
		grupoForm.setNomeInstituicao("UFSC");
		grupoForm.setNomeArea(this.avaliarForm.getArea());
		Grupo grupo = this.grupoService.create(grupoForm);

		CurriculumLattes lattes1 = this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.MARCIO_ZIP_UPLOAD);
		CurriculumLattes lattes2 = this.grupoService.addPesquisadorFromUpload(grupo, Fixtures.CHRISTIANE_XML_UPLOAD);

		AvaliacaoCollectionResult result = this.avaliacaoService.avaliarCollection(grupo.getPesquisadores(), this.avaliarForm);
		Assertions.assertThat(result.getForm()).isEqualTo(this.avaliarForm);
		Assertions.assertThat(result.getStats()).isNotNull();

		AvaliacaoResult result1 = this.avaliacaoService.avaliar(lattes1, this.avaliarForm);
		AvaliacaoResult result2 = this.avaliacaoService.avaliar(lattes2, this.avaliarForm);
		Assertions.assertThat(result.getStats().getArtigos()).hasSize(result1.getArtigos().size() + result2.getArtigos().size());
		Assertions.assertThat(result.getStats().getTrabalhos()).hasSize(result1.getTrabalhos().size() + result2.getTrabalhos().size());
	}
}
