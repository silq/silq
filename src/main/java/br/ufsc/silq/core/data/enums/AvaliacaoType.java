package br.ufsc.silq.core.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AvaliacaoType {

	ARTIGO("artigo"), 
	TRABALHO("trabalho"), 
	AMBOS("ambos");

	private String name;

	public static AvaliacaoType getAvaliacaoByName(String name) {
		for (AvaliacaoType type : AvaliacaoType.values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}

		return null;
	}

	/**
	 * Checa se o tipo de avaliação inclui o tipo parâmetro.
	 *
	 * @param type
	 *            Tipo a ser comparado.
	 * @return Verdadeiro caso o parâmetro a ser comparado seja igual ao objeto
	 *         comparado ou caso este seja AMBOS.
	 */
	public boolean includes(AvaliacaoType type) {
		return this.equals(type) || this.equals(AMBOS);
	}
}
