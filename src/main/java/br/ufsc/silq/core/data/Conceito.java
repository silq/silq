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
	private TipoConceito tipoConceito;

	public Conceito(Qualis result, NivelSimilaridade similaridade, TipoConceito tipoConceito) {
		this.id = result.getId();
		this.tituloVeiculo = result.getTitulo();
		this.conceito = result.getEstrato();
		this.ano = result.getAno();
		this.similaridade = similaridade;
		this.tipoConceito = tipoConceito;

		if (result instanceof QualisEvento) {
			this.siglaVeiculo = ((QualisEvento) result).getSigla();
		}
	}

	public Float getSimilaridade() {
		return this.similaridade.getValue();
	}

	@Override
	public int compareTo(Conceito o) {
		return -this.getSimilaridade().compareTo(o.getSimilaridade());
	}

	public boolean isFeedback() {
		return TipoConceito.FEEDBACK.equals(this.getTipoConceito());
	}
}
