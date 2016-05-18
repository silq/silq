package br.ufsc.silq.core.forms.usuario;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {

	@NotBlank
	private String nome;

	@NotBlank
	private String senha;

	@NotBlank
	@Email
	private String email;

	// TODO (bonetti): remover sexo?
	private String sexo;
}
