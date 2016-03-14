package br.ufsc.silq.web.rest.form;

import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.web.cache.CurriculumCache;
import lombok.Getter;
import lombok.Setter;

public class AvaliacaoLivreForm extends AvaliarForm {

	/**
	 * Utilizado para a especificação do cache de avaliação.
	 * 
	 * @see {@link CurriculumCache}
	 */
	@NotBlank
	@Getter
	@Setter
	private String cacheId;

}
