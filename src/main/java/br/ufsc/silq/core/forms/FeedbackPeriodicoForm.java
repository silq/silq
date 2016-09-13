package br.ufsc.silq.core.forms;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackPeriodicoForm {

	/**
	 * ID do periódico dado como feedback.
	 */
	private Long periodicoId;

	/**
	 * Query do feedback.
	 * Representa o título do periódico onde foi publicado o artigo do pesquisador.
	 */
	@NotBlank
	private String query;

	/**
	 * Ano do artigo do pesquisador.
	 */
	private Integer ano;
}
