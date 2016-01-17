package br.ufsc.silq.core.utils;

public class SilqStringUtils {

	private static final String QUOTES_REGEX = "[\'\"]";
	private static final String ORDINAL_REGEX = "(\\d+)((rd)|(st)|(nd)|(th)\\b)";
	private static final String ROMAN_REGEX = "M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})\\s";

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !SilqStringUtils.isBlank(str);
	}

	public static String setHifenIfVazio(String str) {
		return SilqStringUtils.isBlank(str) ? "-" : str;
	}

	public static String normalizeString(String string) {
		return SilqStringUtils.removeInvalidCharacters(string);
		// TODO Remover anos, siglas e texto entre parênteses também pode
		// reduzir a similaridade final;
	}

	private static String removeInvalidCharacters(String string) {
		string = string.replaceAll(SilqStringUtils.QUOTES_REGEX, "").trim();
		string = string.replaceAll(SilqStringUtils.ORDINAL_REGEX, "").trim();
		string = string.replaceAll(SilqStringUtils.ROMAN_REGEX, " ").trim();

		return string;
	}
}
