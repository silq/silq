package br.ufsc.silq.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import br.ufsc.silq.core.business.entities.Usuario;
import br.ufsc.silq.security.AuthoritiesConstants;
import lombok.Data;

/**
 * Utilizado para retornar informações do usuário atualmente logado para o
 * cliente, via REST
 */
@Data
public class UsuarioDTO {
	private String email;

	private String nome;

	private String sexo;

	private Set<String> authorities;

	public UsuarioDTO(Usuario usuario) {
		this.email = usuario.getEmail();
		this.nome = usuario.getNome();
		this.sexo = usuario.getSexo();

		this.authorities = new HashSet<>();
		usuario.getAutoridades().stream().forEach(autoridade -> {
			this.authorities.add(autoridade.getNome());
		});

		// Todo usuário logado possui a autoridade "ROLE_USER"
		// Ao invés de salvar esta informação no banco, adicionamos na entidade
		// sempre que usarmos este atributo
		this.authorities.add(AuthoritiesConstants.USER);
	}
}
