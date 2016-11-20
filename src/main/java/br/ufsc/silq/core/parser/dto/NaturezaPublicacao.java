package br.ufsc.silq.core.parser.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum NaturezaPublicacao {

	COMPLETO("Completo"),
	RESUMO("Resumo"),
	RESUMO_EXPANDIDO("Resumo expandido"),
	RELATORIO_TECNICO("Relatório técnico"),
	OUTROS("Outros"); // Utilizado para naturezas que por ventura não tenham sido mapeadas neste enum

	private final String desc;

	@Override
	@JsonValue
	public String toString() {
		return this.desc;
	}

	public static NaturezaPublicacao parse(String desc) {
		try {
			return valueOf(desc);
		} catch (Exception e) {
			log.warn("Natureza de publicação não mapeada: {}", desc);
			return OUTROS;
		}
	}
}
