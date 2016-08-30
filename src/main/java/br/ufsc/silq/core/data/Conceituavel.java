package br.ufsc.silq.core.data;

/**
 * Interface comum aos objetos que podem ser conceituados/avaliados (artigos e trabalhos, por exemplo).
 */
public interface Conceituavel {

	/**
	 * @return O ano da publicação.
	 */
	Integer getAno();

	/**
	 * @return O título do veículo onde o objeto (artigo/trabalho) foi publicado.
	 */
	String getTituloVeiculo();

}
