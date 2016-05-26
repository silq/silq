package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.security.xauth.Token;
import br.ufsc.silq.security.xauth.TokenProvider;

/**
 * Serviço de autenticação dos usuários do sistema.
 */
@Service
@Transactional(readOnly = true)
public class AuthService {

	@Inject
	private TokenProvider tokenProvider;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private UserDetailsService userDetailsService;

	/**
	 * Tenta autenticar um usuário através de seu e-mail e senha.
	 *
	 * @param email E-mail do usuário.
	 * @param senha Senha (não codificada) do usuário.
	 * @return O objeto de autenticação criado incluindo as credenciais do usuário.
	 * @throws BadCredentialsException Caso o e-mail ou senha estejam incorretos.
	 */
	public Authentication authenticate(String email, String senha) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, senha);
		return this.authenticationManager.authenticate(token);
	}

	/**
	 * Tenta autenticar um usuário através de seu e-mail e senha.
	 * Caso autenticado, loga o usuário no sistema.
	 *
	 * @param email E-mail do usuário.
	 * @param senha Senha (não codificada) do usuário.
	 * @return O Token de autenticação criado.
	 */
	public Token authenticateAndLogin(String email, String senha) {
		Authentication authentication = this.authenticate(email, senha);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails details = this.userDetailsService.loadUserByUsername(email);
		return this.tokenProvider.createToken(details);
	}
}
