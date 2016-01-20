package br.ufsc.silq.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

	private PasswordEncoder passwordEncoder;

	private UserDetailsService userDetailsService;

	public AuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		String login = token.getName();
		UserDetails user = this.userDetailsService.loadUserByUsername(login);
		if (user == null) {
			throw new UsernameNotFoundException("Usuário não existe");
		}
		String password = user.getPassword();
		String tokenPassword = (String) token.getCredentials();
		if (!this.passwordEncoder.matches(tokenPassword, password)) {
			throw new BadCredentialsException("E-mail ou senha inválidos");
		}
		return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
