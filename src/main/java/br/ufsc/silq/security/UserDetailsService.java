package br.ufsc.silq.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.entities.Usuario;
import br.ufsc.silq.core.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Autentica um usuário do banco de dados
 */
@Component("userDetailsService")
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Inject
	private UsuarioRepository usuarioRepository;

	/**
	 * Retorna detalhes do usuário necessários para autenticação usando seu
	 * e-mail como informação de login
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) {
		log.debug("Authenticating {}", login);

		String lowercaseLogin = login.toLowerCase();
		Optional<Usuario> userFromDatabase = this.usuarioRepository.findOneByEmail(lowercaseLogin);

		return userFromDatabase.map(user -> {
			// if (!user.getActivated()) {
			// throw new UserNotActivatedException("User " + lowercaseLogin + "
			// was not activated");
			// }

			// List<GrantedAuthority> grantedAuthorities =
			// user.getAuthorities().stream()
			// .map(authority -> new
			// SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());

			// TODO (bonetti): fazer relacionamento das Autoridades com Usuario
			List<GrantedAuthority> grantedAuthorities = Arrays.asList(
					new SimpleGrantedAuthority(AuthoritiesConstants.USER),
					new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));

			return new org.springframework.security.core.userdetails.User(lowercaseLogin, user.getSenha(),
					grantedAuthorities);
		}).orElseThrow(
				() -> new UsernameNotFoundException("Usuário " + lowercaseLogin + " não encontrado na base de dados"));
	}
}
