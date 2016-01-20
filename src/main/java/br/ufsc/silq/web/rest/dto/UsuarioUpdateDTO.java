package br.ufsc.silq.web.rest.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * Utilizado para atualização das informações do usuário
 */
@Data
public class UsuarioUpdateDTO {

	@NotBlank(message = "Campo obrigatório")
	@Size(min = 5, max = 100)
	private String nome;

	@NotBlank(message = "Campo obrigatório")
	private String sexo;
}
