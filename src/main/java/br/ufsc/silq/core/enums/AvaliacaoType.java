package br.ufsc.silq.core.enums;

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
}
