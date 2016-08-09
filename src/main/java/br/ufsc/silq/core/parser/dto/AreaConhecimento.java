package br.ufsc.silq.core.parser.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AreaConhecimento implements Serializable {
	private static final long serialVersionUID = -4862606476367568773L;

	private String nomeArea;
	private String nomeGrandeArea;
	private String nomeEspecialidade;
	private String nomeSubAreaConhecimento;

	@Override
	public String toString() {
		return this.getNomeArea() + " - " + this.getNomeGrandeArea();
	}

	public String getNomeGrandeArea() {
		return GrandeAreaConhecimento.valueOf(this.nomeGrandeArea) != null
				? GrandeAreaConhecimento.valueOf(this.nomeGrandeArea).getDesc() : this.nomeGrandeArea;
	}

	@Getter
	@AllArgsConstructor
	public enum GrandeAreaConhecimento {
		CIENCIAS_EXATAS_E_DA_TERRA("Ciências Exatas e da Terra"),
		CIENCIAS_BIOLOGICAS("Ciências Biológicas"),
		ENGENHARIAS("Engenharias"),
		CIENCIAS_DA_SAUDE("Ciências da Saúde"),
		CIENCIAS_AGRARIAS("Ciências Agrárias"),
		CIENCIAS_SOCIAIS_APLICADAS("Ciências Sociais Aplicadas"),
		CIENCIAS_HUMANAS("Ciências Humanas"),
		LINGUISTICA_LETRAS_E_ARTES("Linguística, Letras e Artes");

		private String desc;
	}

}
