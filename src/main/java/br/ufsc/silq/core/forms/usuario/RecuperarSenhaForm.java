package br.ufsc.silq.core.forms.usuario;

import lombok.Data;

/**
 * Usado pela página de recuperação de senha (reset/finish)
 *
 */
@Data
public class RecuperarSenhaForm {

	private String key;
	private String novaSenha;
}
