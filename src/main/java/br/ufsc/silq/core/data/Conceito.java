package br.ufsc.silq.core.data;

import lombok.Data;

@Data
public class Conceito implements Comparable<Conceito> {

	private final Long id;
	private final String tituloVeiculo;
	private final String conceito;
	private final NivelSimilaridade similaridade;
	private final Integer ano;

	/**
	 * Flag usada para indicar se este conceito foi manualmente escolhihdo pelo usuário.
	 * Em caso positivo, então este conceito será escolhido independente da similaridade dos demais.
	 */
	private boolean flagged = false;

	public Float getSimilaridade() {
		return this.similaridade.getValue();
	}

	@Override
	public int compareTo(Conceito o) {
		if (this.isFlagged()) {
			return -1;
		} else if (o.isFlagged()) {
			return 1;
		}

		return -this.getSimilaridade().compareTo(o.getSimilaridade());
	}
}
