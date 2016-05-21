package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.silq.Fixtures;
import br.ufsc.silq.WebContextTest;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.usuario.RegisterForm;
import br.ufsc.silq.core.forms.usuario.UsuarioUpdateForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.CurriculumLattesRepository;
import br.ufsc.silq.core.persistence.repository.UsuarioRepository;

public class UsuarioServiceTest extends WebContextTest {

	@Inject
	UsuarioService usuarioService;

	@Inject
	UsuarioRepository usuarioRepository;

	@Inject
	CurriculumLattesRepository curriculumRepository;

	private RegisterForm registerForm;

	@Before
	public void setup() {
		this.registerForm = new RegisterForm("Test User", "12345", "user@test.com", "F");
	}

	@Test
	public void testRegister() {
		Assertions.assertThat(this.usuarioRepository.count()).isEqualTo(0);
		Usuario usuario = this.usuarioService.registerUsuario(this.registerForm);

		Assertions.assertThat(this.usuarioRepository.count()).isEqualTo(1)
				.as("Deve ter criado um usuário na base de dados");

		Assertions.assertThat(usuario.getNome()).isEqualTo(this.registerForm.getNome());
		Assertions.assertThat(usuario.getEmail()).isEqualTo(this.registerForm.getEmail());
		Assertions.assertThat(usuario.getCurriculum()).isNull();
		Assertions.assertThat(usuario.getSenha()).isNotEqualTo(this.registerForm.getSenha())
				.as("Senha deve ter sido criptada");
		Assertions.assertThat(usuario.getGrupos()).isEmpty();
	}

	@Test
	public void testGetUsuarioLogado() {
		Assertions.assertThatThrownBy(() -> this.usuarioService.getUsuarioLogado()).hasMessageContaining("User not found");
		this.loginUser(this.registerForm);
		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();
		Assertions.assertThat(usuarioLogado.getEmail()).isEqualTo(this.registerForm.getEmail());
	}

	@Test
	public void testUpdateUsuario() {
		this.loginUser(this.registerForm);
		Usuario usuarioAtualizado = this.usuarioService.updateUsuario(new UsuarioUpdateForm("New name", "M"));
		Assertions.assertThat(usuarioAtualizado.getNome()).isEqualTo("New name");
	}

	@Test
	public void testAlterarSenha() {
		Usuario usuario = this.loginUser(this.registerForm);
		String senhaAntiga = usuario.getSenha();
		Usuario usuarioComSenhaAlterada = this.usuarioService.alterarSenha("nova senha 123");
		Assertions.assertThat(usuarioComSenhaAlterada.getSenha()).isNotNull();
		Assertions.assertThat(usuarioComSenhaAlterada.getSenha()).isNotEqualTo(senhaAntiga);
	}

	@Test
	public void testRequestPasswordReset() {
		Usuario usuario = this.usuarioService.registerUsuario(this.registerForm);
		Assertions.assertThat(usuario.getResetKey()).isNull();
		this.usuarioService.requestPasswordReset(usuario.getEmail());
		Assertions.assertThat(usuario.getResetKey()).isNotNull();
	}

	@Test
	public void testCompletePasswordReset() {
		Usuario usuario = this.usuarioService.registerUsuario(this.registerForm);
		this.usuarioService.requestPasswordReset(usuario.getEmail());
		String senhaAntiga = usuario.getSenha();

		this.usuarioService.completePasswordReset("nova senha 123", usuario.getResetKey());
		Assertions.assertThat(usuario.getSenha()).isNotEmpty();
		Assertions.assertThat(usuario.getSenha()).isNotEqualTo(senhaAntiga);
		Assertions.assertThat(usuario.getResetKey()).isNull();
	}

	@Test
	public void testSaveCurriculumUsuario() throws SilqException {
		Usuario usuario = this.usuarioService.registerUsuario(this.registerForm);
		Assertions.assertThat(usuario.getCurriculum()).isNull();
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(0);

		CurriculumLattes lattes = this.usuarioService.saveCurriculumUsuario(usuario, Fixtures.RONALDO_ZIP_UPLOAD);
		Assertions.assertThat(usuario.getCurriculum()).isEqualTo(lattes);
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1);
	}

	@Test
	public void testRemoveCurriculumUsuario() throws SilqException {
		Usuario usuario = this.usuarioService.registerUsuario(this.registerForm);
		this.usuarioService.saveCurriculumUsuario(usuario, Fixtures.RONALDO_XML_UPLOAD);
		Assertions.assertThat(usuario.getCurriculum()).isNotNull();
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(1);

		this.usuarioService.removeCurriculumUsuario(usuario);
		Assertions.assertThat(usuario.getCurriculum()).isNull();
		Assertions.assertThat(this.curriculumRepository.count()).isEqualTo(0);
	}

	@Test
	public void testGetCurriculumUsuarioLogado() throws SilqException {
		this.loginUser(this.registerForm);
		CurriculumLattes lattes = this.usuarioService.saveCurriculumUsuarioLogado(Fixtures.RONALDO_XML_UPLOAD);
		Assertions.assertThat(this.usuarioService.getCurriculumUsuarioLogado()).isEqualTo(lattes);
	}
}
