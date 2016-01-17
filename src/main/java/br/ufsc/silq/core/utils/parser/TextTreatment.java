package br.ufsc.silq.core.utils.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTreatment {

	public static boolean qualisType;

	/**
	 * Trata o texto coletado do PDF
	 * 
	 * @param text
	 * @return
	 */
	public static String[] treatText(String text) {
		String patternMeses = ".*(Janeiro|Fevereiro|Março|Abril|Maio|Junho|Julho|Agosto|Setembro|Outubro|Novembro|Dezembro|January|February|March|April|May|June|July|August|September|October|November|December).*";
		String patternQualis = ".*(Qualis|QUALIS|Estrato|ESTRATO).*";
		String patternPaginas = "\\d(\\d)*/\\d(\\d)";

		String treat = text;

		treat = treat.replaceAll("-­‐", "-");
		treat = treat.replaceAll(" ", " ");
		treat = treat.replaceAll("\t", "");

		if (treat.contains("Índice-H")) {
			treat = tratadorConferencia(treat);
			qualisType = false;
		} else {
			treat = tratadorPeriodicos(treat);
			qualisType = true;
		}
		treat = removePadroes(treat, patternQualis);
		treat = removePadroes(treat, patternMeses);
		treat = removePadroes(treat, patternPaginas);

		treat = treat.replaceAll("\r\n", " ");
		treat = treat.replaceAll("\n", " ");
		treat = treat.replaceAll("  ", "");
		treat = treat.trim();
		String[] resultado = stringToArray(treat, qualisType);

		return resultado;
	}

	public static String tratadorPeriodicos(String treat) {
		treat = treat.replaceAll("ETURISMO", "E TURISMO");
		treat = treat.replaceAll("RELAÇÕESINTERNACIONAIS", "RELAÇÕES INTERNACIONAIS");
		treat = treat.replaceAll("/DEMOGRAFIA", "/ DEMOGRAFIA");
		treat = treat.replaceAll("PROBABILIDADE E", "PROBABILIDADE E ESTATÍSTICA");
		int sub = treat.indexOf("STATUS");
		treat = treat.substring(sub);

		return treat;
	}

	private static String tratadorConferencia(String treat) {
		int s = treat.indexOf("Sigla");
		treat = treat.substring(s);
		treat = treat.replaceAll("\r", " ");
		return treat;
	}

	/**
	 * Separa o texto em tokens
	 * 
	 * @param text
	 * @param qualisType
	 * @return
	 */
	private static String[] stringToArray(String text, boolean qualisType) {
		String regexConferencia = "A1|A2|B1|B2|B3|B4|B5";
		String regexPeriodico = text.substring(text.lastIndexOf(" ") + 1);
		String[] tokens;

		if (qualisType == false) {
			tokens = text.trim().split("(?<=" + regexConferencia + ")");
		} else {
			tokens = text.trim().split("(?<=" + regexPeriodico + " )");
		}

		return tokens;
	}

	/**
	 * Remove alguns tipos de repetições desnecessárias do texto.
	 * 
	 * @param text
	 * @param ptrn
	 * @return
	 */
	private static String removePadroes(String text, String ptrn) {

		Pattern pattern = Pattern.compile(ptrn);
		Matcher matcher = pattern.matcher(text);
		StringBuffer stringBuffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(stringBuffer, "");
		}
		matcher.appendTail(stringBuffer);

		text = stringBuffer.toString();

		return text;
	}

}
