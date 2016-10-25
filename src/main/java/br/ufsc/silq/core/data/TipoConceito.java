package br.ufsc.silq.core.data;

/**
 * Representa o tipo de conceito atribuído a um objeto {@link Conceituado}, ou seja, por que
 * o algoritmo de avaliação do SILQ escolheu o objeto como um matching para a query submetida.
 */
public enum TipoConceito {

	/**
	 * O conceito foi atribuído via comparação de ISSN entre a query e o registro Qualis.
	 */
	ISSN,

	/**
	 * O conceito foi atribuído por causa da semelhança textual entre a query e o registro Qualis.
	 */
	SIMILARIDADE_TEXTUAL,

	/**
	 * O conceito foi atribuído por causa de um feedback de usuário.
	 */
	FEEDBACK;
}
