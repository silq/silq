package br.ufsc.silq.core.forms;

import org.hibernate.validator.constraints.NotBlank;

public class SolrSearchQuery {

	@NotBlank(message = "Campo obrigatório")
	public String query;

}
