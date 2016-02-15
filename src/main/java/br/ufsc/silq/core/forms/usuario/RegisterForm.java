package br.ufsc.silq.core.forms.usuario;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterForm {

	@NotBlank
	private String nome;

	@NotBlank
	private String senha;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String sexo;
}