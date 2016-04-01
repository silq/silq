package br.ufsc.silq.core.exception;

public class SilqUploadException extends SilqException {
	private static final long serialVersionUID = 605392058128899205L;

	public SilqUploadException(String msg) {
		super(msg);
	}

	public SilqUploadException() {
		super("Currículo Lattes inválido");
	}

}
