package br.ufsc.silq.core.exception;

public class SilqLattesException extends SilqException {
	private static final long serialVersionUID = 605392058128899205L;

	public SilqLattesException(Throwable cause) {
		super("Currículo Lattes inválido", cause);
	}

	public SilqLattesException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
