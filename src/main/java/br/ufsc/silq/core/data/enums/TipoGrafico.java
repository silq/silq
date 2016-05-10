package br.ufsc.silq.core.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoGrafico {
	
	GERAL("geral"),
	ANUAL("anual_por_grupo");
	
	private String descricao;
}
