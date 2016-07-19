package br.ufsc.silq.core.data;

import java.security.InvalidParameterException;

import lombok.Data;

@Data
public class Periodo {

	private Integer inicio;

	private Integer fim;

	public Periodo() {
	}

	public Periodo(Integer inicio, Integer fim) {
		if (inicio > fim) {
			throw new InvalidParameterException("Início do período deve ser anterior ao fim.");
		}

		this.inicio = inicio;
		this.fim = fim;
	}

	public String getText() {
		if (this.getInicio() == null && this.getFim() == null) {
			return "Todos";
		}

		if (this.getInicio() != null && this.getFim() == null) {
			return "A partir de " + this.getInicio();
		}

		if (this.getInicio() == null && this.getFim() != null) {
			return "Até " + this.getFim();
		}

		return this.getInicio() + " - " + this.getFim();
	}

	@Override
	public String toString() {
		return this.getText();
	}

	/**
	 * Checa se o período setado inclui o ano passado como parâmetro.
	 *
	 * @param ano Ano a ser checado.
	 * @return Verdadeiro caso o período inclua o ano.
	 */
	public boolean inclui(int ano) {
		if (this.getInicio() != null && ano < this.getInicio()) {
			return false;
		}

		if (this.getFim() != null && ano > this.getFim()) {
			return false;
		}

		return true;
	}
}
