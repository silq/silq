package br.ufsc.silq.core.forms.usuario;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.service.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Formulário de alteração de senha.
 *
 * @see UsuarioService#alterarSenha
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlterarSenhaForm {

	@NotBlank
	@Size(min = 5, max = 50)
	private String senhaAtual;

	@Size(min = 5, max = 50)
	private String novaSenha;
}
