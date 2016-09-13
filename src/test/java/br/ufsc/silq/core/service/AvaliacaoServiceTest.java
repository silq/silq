package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.core.data.AvaliacaoCollectionResult;
import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.Conceituado;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.forms.FeedbackPeriodicoForm;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;

public class AvaliacaoServiceTest extends WebContextTest {

	@Inject
	AvaliacaoService avaliacaoService;

	@Inject
	CurriculumLattesService curriculumService;

	@Inject
	GrupoService grupoService;

	@Inject
	FeedbackService feedbackService;

	@Inject
	LattesParser lattesParser;

	@Inject
	QualisEventoRepository eventoRepo;

	@Inject
	QualisPeriodicoRepository periodicoRepo;

	private AvaliarForm avaliarForm;

	private Usuario usuarioLogado;

	@Before
	public void setup() {
		this.usuarioLogado = this.loginUser();
		this.avaliarForm = new AvaliarForm();
		this.avaliarForm.setArea("Ciência da Computação");
		this.avaliarForm.setMaxConceitos(4);
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
		Assertions.assertThat(result.getArtigos().get(0).hasConceito()).isTrue();
		Assertions.assertThat(result.getTrabalhos().get(0).hasConceito()).isTrue();
	}

	@Test
	public void testAvaliarArtigo() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.MAURICIO_ZIP_UPLOAD);
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		Artigo artigo = parseResult.getArtigos().stream().findAny().get();

		Conceituado<Artigo> artigoAvaliado = this.avaliacaoService.avaliarArtigo(artigo, this.avaliarForm, null);

		// Artigo avaliado deve ser uma cópia do parâmetro
		Assertions.assertThat(artigoAvaliado).isNotSameAs(artigo);
		Assertions.assertThat(artigoAvaliado.getConceitos().size()).isLessThanOrEqualTo(this.avaliarForm.getMaxConceitos());
	}

	@Test
	public void testAvaliarArtigoComFeedback() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.MAURICIO_ZIP_UPLOAD);
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		Artigo artigo = parseResult.getArtigos().stream().findAny().get();

		// Dá o feedback
		QualisPeriodico periodico = this.periodicoRepo.findOne(1L);
		this.feedbackService.sugerirMatchingPeriodico(new FeedbackPeriodicoForm(periodico.getId(), artigo.getTituloVeiculo(), periodico.getAno()));

		Conceituado<Artigo> artigoAvaliado = this.avaliacaoService.avaliarArtigo(artigo, this.avaliarForm, this.usuarioLogado);

		// Artigo avaliado deve ter o conceito do feedback dado
		Assertions.assertThat(artigoAvaliado.hasConceito()).isTrue();
		Assertions.assertThat(artigoAvaliado.getConceitos().size()).isLessThanOrEqualTo(this.avaliarForm.getMaxConceitos());

		Conceito conceito = artigoAvaliado.getConceitoMaisSimilar();
		Assertions.assertThat(conceito.getTituloVeiculo()).isEqualTo(periodico.getTitulo());
		Assertions.assertThat(conceito.getAno()).isEqualTo(periodico.getAno());
		Assertions.assertThat(conceito.getConceito()).isEqualTo(periodico.getEstrato());
		Assertions.assertThat(conceito.getSimilaridade()).isNotNegative();
	}

	@Test
	public void testAvaliarTrabalho() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.MAURICIO_ZIP_UPLOAD);
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		Trabalho trabalho = parseResult.getTrabalhos().stream().findAny().get();

		Conceituado<Trabalho> trabalhoAvaliado = this.avaliacaoService.avaliarTrabalho(trabalho, this.avaliarForm, null);

		// Trabalho avaliado deve ser uma cópia do parâmetro
		Assertions.assertThat(trabalhoAvaliado).isNotSameAs(trabalho);
		Assertions.assertThat(trabalhoAvaliado.getConceitos().size()).isLessThanOrEqualTo(this.avaliarForm.getMaxConceitos());
	}

	@Test
	public void testAvaliarTrabalhoComFeedback() throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(Fixtures.MAURICIO_ZIP_UPLOAD);
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		Trabalho trabalho = parseResult.getTrabalhos().stream().findAny().get();

		// Dá o Feedback
		QualisEvento evento = this.eventoRepo.findOne(1L);
		this.feedbackService.sugerirMatchingEvento(new FeedbackEventoForm(evento.getId(), trabalho.getTituloVeiculo(), evento.getAno()));

		Conceituado<Trabalho> trabalhoAvaliado = this.avaliacaoService.avaliarTrabalho(trabalho, this.avaliarForm, this.usuarioLogado);

		// Trabalho avaliado deve ter o conceito do feedback dado
		Assertions.assertThat(trabalhoAvaliado.hasConceito()).isTrue();
		Assertions.assertThat(trabalhoAvaliado.getConceitos().size()).isLessThanOrEqualTo(this.avaliarForm.getMaxConceitos());

		Conceito conceito = trabalhoAvaliado.getConceitoMaisSimilar();
		Assertions.assertThat(conceito.getTituloVeiculo()).isEqualTo(evento.getTitulo());
		Assertions.assertThat(conceito.getAno()).isEqualTo(evento.getAno());
		Assertions.assertThat(conceito.getConceito()).isEqualTo(evento.getEstrato());
		Assertions.assertThat(conceito.getSimilaridade()).isNotNegative();
	}

	@Test
	public void testAvaliarCollection() throws SilqException {
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
