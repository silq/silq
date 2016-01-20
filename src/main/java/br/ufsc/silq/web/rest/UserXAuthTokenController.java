package br.ufsc.silq.web.rest;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.ufsc.silq.security.xauth.Token;
import br.ufsc.silq.security.xauth.TokenProvider;

/**
 * Controlador de autenticação de usuários usuando um Token stateless
 */
@RestController
@RequestMapping("/api")
public class UserXAuthTokenController {

	@Inject
	private TokenProvider tokenProvider;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@Timed
	public Token authorize(@RequestParam String login, @RequestParam String password) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
		Authentication authentication = this.authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails details = this.userDetailsService.loadUserByUsername(login);
		return this.tokenProvider.createToken(details);
	}
}
