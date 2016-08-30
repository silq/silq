package br.ufsc.silq.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import br.ufsc.silq.core.parser.dto.DataDto;

public class SilqDataUtils {

	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public static Date formatDates(String dataDia, String dataHora) {
		DataDto dataDto = getDataFromStrings(dataDia, dataHora);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getDefault());
		calendar.set(dataDto.getAno(), dataDto.getMes(), dataDto.getDia(), dataDto.getHora(), dataDto.getMinuto(),
				dataDto.getSegundo());
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	private static DataDto getDataFromStrings(String dataDia, String dataHora) {
		DataDto dataDto = new DataDto();

		Integer dia = Integer.parseInt(dataDia.substring(0, 2));
		Integer mes = Integer.parseInt(dataDia.substring(2, 4)) - 1;
		Integer ano = Integer.parseInt(dataDia.substring(4, 8));
		Integer hora = Integer.parseInt(dataHora.substring(0, 2));
		Integer minuto = Integer.parseInt(dataHora.substring(2, 4));
		Integer segundo = Integer.parseInt(dataHora.substring(4, 6));

		dataDto.setDia(dia);
		dataDto.setMes(mes);
		dataDto.setAno(ano);
		dataDto.setHora(hora);
		dataDto.setMinuto(minuto);
		dataDto.setSegundo(segundo);

		return dataDto;
	}

	public static String getFormattedDate(Date data) {
		if (data == null) {
			return "";
		}
		return DATE_TIME_FORMAT.format(data);
	}
}
