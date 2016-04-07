package br.ufsc.silq.core.exception;

import lombok.Getter;

public class SilqLattesException extends SilqException {
	private static final long serialVersionUID = 605392058128899205L;

	/**
	 * Exception que originou esta
	 */
	@Getter
	private Exception rootException;

	public SilqLattesException(Exception rootException, String msg) {
		super(msg);
		this.rootException = rootException;
	}

	public SilqLattesException(Exception originalException) {
		this(originalException, "Currículo Lattes inválido");
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		this.rootException.printStackTrace();
	}

}
