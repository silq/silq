package br.ufsc.silq.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception que retorna uma resposta HTTP NOT FOUND (404) quando lançada
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class HttpNotFound extends RuntimeException {
	private static final long serialVersionUID = 7523585643570217014L;

	public HttpNotFound(String messageKey) {
		super(messageKey);
	}

}
