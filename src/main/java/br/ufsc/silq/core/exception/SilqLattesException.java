package br.ufsc.silq.core.exception;

public class SilqLattesException extends SilqException {
	private static final long serialVersionUID = 605392058128899205L;

	/**
	 * Exception que originou esta
	 */
	private Exception originalException;

	public SilqLattesException(Exception originalException, String msg) {
		super(msg);
		this.originalException = originalException;
	}

	public SilqLattesException(Exception originalException) {
		this(originalException, "Currículo Lattes inválido");
	}

}
