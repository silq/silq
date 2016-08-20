package br.ufsc.silq.core.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QualisSearchForm {

	private String query;

	private String area;

	public QualisSearchForm(String query) {
		this.query = query;
	}
}
