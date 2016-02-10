package br.ufsc.silq.web.rest.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * DTO for transfering error message with a list of field errors.
 */
@Data
public class ErrorDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String message;
	private final String description;
	private List<FieldErrorDTO> fieldErrors;

	ErrorDTO(String message) {
		this(message, null);
	}

	ErrorDTO(String message, String description) {
		this.message = message;
		this.description = description;
	}

	ErrorDTO(String message, String description, List<FieldErrorDTO> fieldErrors) {
		this.message = message;
		this.description = description;
		this.fieldErrors = fieldErrors;
	}

	public void add(String objectName, String field, String message) {
		if (this.fieldErrors == null) {
			this.fieldErrors = new ArrayList<>();
		}
		this.fieldErrors.add(new FieldErrorDTO(objectName, field, message));
	}

}
