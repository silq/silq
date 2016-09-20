package br.ufsc.silq.core.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Conceito implements Comparable<Conceito> {

	private final Long id;
	private final String tituloVeiculo;
	private final String conceito;
	private final NivelSimilaridade similaridade;
	private final Integer ano;
	private String siglaVeiculo;

	/**
	 * Flag usada para indicar se este conceito foi manualmente escolhido pelo usuário.
	 * Em caso positivo, então este conceito será escolhido independente da similaridade dos demais.
	 */
	private boolean feedback = false;

	public Float getSimilaridade() {
		return this.similaridade.getValue();
	}

	@Override
	public int compareTo(Conceito o) {
		if (this.isFeedback()) {
			return -1;
		} else if (o.isFeedback()) {
			return 1;
		}

		return -this.getSimilaridade().compareTo(o.getSimilaridade());
	}
}
