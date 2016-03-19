package br.ufsc.silq.web.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.ufsc.silq.core.business.service.MailService;
import br.ufsc.silq.core.business.service.UsuarioService;
import br.ufsc.silq.core.commondto.UsuarioDTO;
import br.ufsc.silq.core.forms.usuario.RecuperarSenhaForm;
import br.ufsc.silq.core.forms.usuario.RegisterForm;
import br.ufsc.silq.core.forms.usuario.UsuarioUpdateForm;
import lombok.extern.slf4j.Slf4j;

/**
 * Gerencia o usuário atualmente logado
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class AccountResource {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private MailService mailService;

	/**
	 * POST /register -> Cadastra um novo usuário, caso o e-mail já não esteja
	 * em uso
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	public ResponseEntity<?> registerAccount(@Valid @RequestBody RegisterForm usuario) {
		return this.usuarioService.findOneByEmail(usuario.getEmail())
				.map(user -> new ResponseEntity<>("E-mail já encontra-se cadastrado", HttpStatus.BAD_REQUEST))
				.orElseGet(() -> {
					this.usuarioService.registerUsuario(usuario);
					return new ResponseEntity<>(HttpStatus.CREATED);
				});
	}

	/**
	 * GET /activate -> activate the registered user.
	 */
	// @RequestMapping(value = "/activate", method = RequestMethod.GET, produces
	// = MediaType.APPLICATION_JSON_VALUE)
	// @Timed
	// public ResponseEntity<String> activateAccount(@RequestParam(value =
	// "key") String key) {
	// return Optional.ofNullable(this.userService.activateRegistration(key))
	// .map(user -> new ResponseEntity<String>(HttpStatus.OK))
	// .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	// }

	/**
	 * GET /authenticate -> check if the user is authenticated, and return its
	 * login.
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	/**
	 * GET /account -> retorna o usuário atualmente logado
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<UsuarioDTO> getAccount() {
		return Optional.ofNullable(this.usuarioService.getUsuarioLogado())
				.map(usuario -> new ResponseEntity<>(new UsuarioDTO(usuario), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * POST /account -> atualiza as informações do usuário atual
	 */
	@RequestMapping(value = "/account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> saveAccount(@Valid @RequestBody UsuarioUpdateForm info) {
		this.usuarioService.updateUsuario(info);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * POST /change_password -> altera a senha do usuário atual
	 */
	@RequestMapping(value = "/account/change_password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> changePassword(@RequestBody String password) {
		if (!this.checkPasswordLength(password)) {
			return new ResponseEntity<>("Senha inválida", HttpStatus.BAD_REQUEST);
		}
		this.usuarioService.alterarSenha(password);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/account/reset_password/init", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
		return this.usuarioService.requestPasswordReset(mail).map(usuario -> {
			this.mailService.sendPasswordResetMail(usuario, this.getBaseUrl(request));
			return new ResponseEntity<>("E-mail enviado", HttpStatus.OK);
		}).orElse(new ResponseEntity<>("E-mail não registrado", HttpStatus.BAD_REQUEST));
	}

	@RequestMapping(value = "/account/reset_password/finish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> finishPasswordReset(@RequestBody RecuperarSenhaForm chaveESenha) {
		if (!this.checkPasswordLength(chaveESenha.getNovaSenha())) {
			return new ResponseEntity<>("Senha inválida", HttpStatus.BAD_REQUEST);
		}
		return this.usuarioService.completePasswordReset(chaveESenha.getNovaSenha(), chaveESenha.getKey())
				.map(user -> new ResponseEntity<String>(HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	private String getBaseUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	private boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) && password.length() >= 5 && password.length() <= 50;
	}
}
