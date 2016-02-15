package br.ufsc.silq.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception que retorna uma resposta HTTP INTERNAL SERVER ERROR (500) quando
 * lançada
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class HttpInternalServerError extends RuntimeException {
	private static final long serialVersionUID = -3653887064073716393L;

	public HttpInternalServerError(String messageKey) {
		super(messageKey);
	}

}
