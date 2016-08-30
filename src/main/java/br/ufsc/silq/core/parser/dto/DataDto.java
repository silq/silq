package br.ufsc.silq.core.parser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataDto {

	private Integer ano;
	private Integer mes;
	private Integer dia;
	private Integer hora;
	private Integer minuto;
	private Integer segundo;
}
