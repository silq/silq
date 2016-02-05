package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PesquisadorResult implements Serializable {

	private static final long serialVersionUID = -7762349339590570720L;

	private String nome;
	private Long idCurriculo;
	private Date ultimaAtualizacao;

	public PesquisadorResult() {
		this.nome = "-";
	}

}
