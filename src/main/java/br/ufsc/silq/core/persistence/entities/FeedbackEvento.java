package br.ufsc.silq.core.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(FeedbackEvento.DISCRIMINATOR)
@Getter
@Setter
public class FeedbackEvento extends Feedback {
	public static final String DISCRIMINATOR = "E";

	@ManyToOne
	@JoinColumn(name = "co_evento")
	private QualisEvento evento;

}
