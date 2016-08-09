package br.ufsc.silq.core.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackPeriodicoForm {

	@NotNull
	private Long periodicoId;

	@NotBlank
	private String query;

}
