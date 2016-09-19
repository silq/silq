package br.ufsc.silq.core.forms;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEventoForm {

	/**
	 * ID do evento dado como feedback.
	 * Se for nulo, indica um feedback negativo, ou seja, que não existe evento cadastrado no sistema
	 * que seja um match para este trabalho.
	 */
	private Long eventoId;

	/**
	 * Query do feedback.
	 * Representa o título do evento de um trabalho do pesquisador.
	 */
	@NotBlank
	private String query;

	/**
	 * Ano do trabalho do pesquisador.
	 */
	private Integer ano;

	/**
	 * Checa se o feedback é negativo, ou seja, não existe registro Qualis que seja um match para esta query.
	 * 
	 * @return Verdadeiro se for um feedback negativo.
	 */
	public boolean isNegativo() {
		return this.eventoId == null;
	}
}
