package br.ufsc.silq.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception que retorna uma resposta HTTP BAD REQUEST (400) quando lançada
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HttpBadRequest extends RuntimeException {
	private static final long serialVersionUID = -3653887064073716393L;

	public HttpBadRequest(String messageKey) {
		super(messageKey);
	}

}