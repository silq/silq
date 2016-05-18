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
	 * @return
	 */
	protected Usuario loginUser() {
		RegisterForm registerForm = new RegisterForm();
		registerForm.setNome("Bruce Wayne");
		registerForm.setEmail("batman@batman.com");
		registerForm.setSenha("j0k3r");
		Usuario user = this.usuarioService.registerUsuario(registerForm);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(registerForm.getEmail(), registerForm.getSenha());
		Authentication authentication = this.authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return user;
	}

}
