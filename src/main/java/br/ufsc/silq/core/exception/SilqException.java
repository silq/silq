package br.ufsc.silq.core.exception;

public class SilqException extends Exception {

	private static final long serialVersionUID = -3464387742195614094L;

	public SilqException(String msg) {
		super(msg);
	}

	public SilqException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SilqException(Throwable cause) {
		super(cause);
	}
}
