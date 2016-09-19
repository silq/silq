package br.ufsc.silq.core.forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.data.AvaliacaoType;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.Periodo;
import lombok.Data;

@Data
public class AvaliarForm {

	/**
	 * Área a ser utilizada na avaliação.
	 * Se especificado, somente conceitos Qualis desta área serão levados em conta na avaliação.
	 */
	@NotBlank
	private String area;

	/**
	 * Threshold de similaridade textual.
	 * No caso de avaliação via similaridade textual, somente conceitos com similaridade textual acima deste
	 * valor serão retornados.
	 */
	@NotNull
	private NivelSimilaridade nivelSimilaridade = NivelSimilaridade.NORMAL;

	/**
	 * Período de avaliação considerado.
	 * Se especificado, só avalia e retorna artigos e trabalhos cujos anos de publicação/apresentação
	 * tenham ocorrido dentro do período.
	 */
	@NotNull
	private Periodo periodoAvaliacao = new Periodo();

	/**
	 * Tipo de avaliação.
	 * Espefica quais tipos de avaliação devem ocorrer.
	 *
	 * @see {@link AvaliacaoType}
	 */
	@NotNull
	private AvaliacaoType tipoAvaliacao = AvaliacaoType.AMBOS;

	/**
	 * Número máximo de conceitos a ser atribuído a um artigo/trabalho
	 */
	@Min(1)
	@Max(100)
	private int maxConceitos = 5;

	/**
	 * Se devem ser utilizados os feedbacks do usuário para a avaliação.
	 */
	private boolean usarFeedback = true;

	/**
	 * Se os artigos devem ser avaliados por similaridade (da mesma maneira que os eventos) além da avaliação por ISSN.
	 */
	private boolean avaliarArtigoPorSimilaridade = false;

	public boolean hasArea() {
		return StringUtils.isNotBlank(this.getArea());
	}
}
