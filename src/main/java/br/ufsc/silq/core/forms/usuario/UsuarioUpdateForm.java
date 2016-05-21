package br.ufsc.silq.core.forms.usuario;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Utilizado para atualização das informações do usuário
 */
@Data
@AllArgsConstructor
public class UsuarioUpdateForm {

	@NotBlank
	@Size(min = 5, max = 100)
	private String nome;

	// TODO (bonetti): remover sexo?
	private String sexo;
}
