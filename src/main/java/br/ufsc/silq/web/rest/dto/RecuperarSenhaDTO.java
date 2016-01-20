package br.ufsc.silq.web.rest.dto;

import lombok.Data;

/**
 * Usado pela página de recuperação de senha (reset/finish)
 *
 */
@Data
public class RecuperarSenhaDTO {

	private String key;
	private String novaSenha;
}
