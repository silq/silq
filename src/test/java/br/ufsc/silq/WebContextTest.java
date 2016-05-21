package br.ufsc.silq;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.forms.usuario.RegisterForm;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.service.UsuarioService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles(resolver = TestProfilesResolver.class)
@Transactional
public abstract class WebContextTest {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private AuthenticationManager authenticationManager;

	/**
	 * Cria um novo usuário na base de dados e o loga no sistema, para testes.
	 *
	 * @return A entidade {@link Usuario} criada.
	 */
	protected Usuario loginUser() {
		return this.loginUser(new RegisterForm("Bruce Wayne", "j0k3r", "batman@batman.com", "M"));
	}

	/**
	 * Cria um novo usuário na base de dados e o loga no sistema, para testes.
	 *
	 * @param registerForm Formulário de registro utilizado para criar o usuário que será logado.
	 * @return A entidade {@link Usuario} criada.
	 */
	protected Usuario loginUser(RegisterForm registerForm) {
		Usuario user = this.usuarioService.registerUsuario(registerForm);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(registerForm.getEmail(), registerForm.getSenha());
		Authentication authentication = this.authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return user;
	}

}
