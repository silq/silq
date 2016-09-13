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
}
