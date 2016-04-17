package br.ufsc.silq.core.enums;

import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Periodo {

	// TODO Primeiro ano do Qualis?
	TODOS_TRIENIOS(1985, getCurrentYear()),
	TRIENIO_2007_2009(2007, 2009),
	TRIENIO_2010_2012(2010, 2012),
	TRIENIO_2013_2015(2013, 2015);

	private Integer primeiroAno;
	private Integer ultimoAno;

	private static Integer getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		return calendar.get(Calendar.YEAR);
	}

	public static Periodo getEnum(String descricao) {
		for (Periodo periodo : Periodo.values()) {
			if (periodo.name().equals(descricao)) {
				return periodo;
			}
		}
		return null;
	}
}
