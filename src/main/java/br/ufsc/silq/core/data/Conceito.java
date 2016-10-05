package br.ufsc.silq.core.data;

import br.ufsc.silq.core.persistence.entities.Qualis;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
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

	public Conceito(Qualis result, NivelSimilaridade similaridade) {
		this.id = result.getId();
		this.tituloVeiculo = result.getTitulo();
		this.conceito = result.getEstrato();
		this.ano = result.getAno();
		this.similaridade = similaridade;

		if (result instanceof QualisEvento) {
			this.siglaVeiculo = ((QualisEvento) result).getSigla();
		}
	}

	public Float getSimilaridade() {
		return this.similaridade.getValue();
	}

	@Override
	public int compareTo(Conceito o) {
		// if (this.isFeedback()) {
		// return -1;
		// } else if (o.isFeedback()) {
		// return 1;
		// }

		return -this.getSimilaridade().compareTo(o.getSimilaridade());
	}
}
