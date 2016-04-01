package br.ufsc.silq.core.exception;

public class SilqLattesException extends SilqException {
	private static final long serialVersionUID = 605392058128899205L;

	public SilqLattesException(String msg) {
		super(msg);
	}

	public SilqLattesException() {
		super("Currículo Lattes inválido");
	}

}
