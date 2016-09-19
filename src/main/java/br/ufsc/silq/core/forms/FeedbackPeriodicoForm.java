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
	 * Se for nulo, indica um feedback negativo, ou seja, que não existe periódico cadastrado no sistema
	 * que seja um match para este trabalho.
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

	/**
	 * Checa se o feedback é negativo, ou seja, não existe registro Qualis que seja um match para esta query.
	 *
	 * @return Verdadeiro se for um feedback negativo.
	 */
	public boolean isNegativo() {
		return this.periodicoId == null;
	}
}
