package br.ufsc.silq.core.exception;

public class SilqError extends RuntimeException {
	private static final long serialVersionUID = -1208524256782432825L;

	public SilqError(String msg) {
		super(msg);
	}

	public SilqError(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SilqError(Throwable cause) {
		super(cause);
	}
}
