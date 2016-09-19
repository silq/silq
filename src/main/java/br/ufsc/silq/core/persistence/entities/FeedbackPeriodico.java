package br.ufsc.silq.core.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(FeedbackPeriodico.DISCRIMINATOR)
@Getter
@Setter
public class FeedbackPeriodico extends Feedback {
	public static final String DISCRIMINATOR = "P";

	@ManyToOne
	@JoinColumn(name = "co_periodico")
	private QualisPeriodico periodico;

	/**
	 * Checa se o feedback é negativo.
	 * Um feedback negativo indica que não existe registro qualis no sistema que seja um match válido para esta query.
	 *
	 * @return Veridadeiro se for um feedback negativo.
	 */
	public boolean isNegativo() {
		return this.periodico == null;
	}
}
