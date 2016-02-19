package br.ufsc.silq.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.business.entities.Usuario;
import br.ufsc.silq.core.business.repository.UsuarioRepository;
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
			List<GrantedAuthority> grantedAuthorities = user.getAutoridades().stream()
					.map(autoridade -> new SimpleGrantedAuthority(autoridade.getNome())).collect(Collectors.toList());

			// Todo usuário logado possui a autoridade "ROLE_USER"
			grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

			return new org.springframework.security.core.userdetails.User(lowercaseLogin, user.getSenha(),
					grantedAuthorities);
		}).orElseThrow(
				() -> new UsernameNotFoundException("Usuário " + lowercaseLogin + " não encontrado na base de dados"));
	}
}
