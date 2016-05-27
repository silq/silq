package br.ufsc.silq.core.forms.usuario;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Utilizado para atualização das informações do usuário
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateForm {

	@NotBlank
	@Size(min = 5, max = 100)
	private String nome;

}
