package br.ufsc.silq.core.forms;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualisSearchForm {

	private String query = "";

	private String area;

	public QualisSearchForm(String query) {
		this.query = query;
	}

	public boolean hasQuery() {
		return StringUtils.isNotBlank(this.query);
	}

	public boolean hasArea() {
		return StringUtils.isNotBlank(this.area);
	}
}
