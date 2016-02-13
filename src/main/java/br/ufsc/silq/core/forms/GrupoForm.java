package br.ufsc.silq.core.forms;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class GrupoForm {

	@NotBlank
	private String nomeGrupo;

	@NotBlank
	private String nomeInstituicao;

	@NotBlank
	private String nomeArea;

}
