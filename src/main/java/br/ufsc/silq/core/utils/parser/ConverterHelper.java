package br.ufsc.silq.core.utils.parser;

public class ConverterHelper {

	public static Integer parseIntegerSafely(String text) {
		if (text == null || text.equals("")) {
			return null;
		}
		String filteredNumber = "";
		for (int i = 0; i < text.length(); i++) {
			char charAt = text.charAt(i);
			if (Character.isDigit(charAt)) {
				filteredNumber += charAt;
			}
		}

		if (filteredNumber.equals("")) {
			return null;
		}

		Integer ano = Integer.parseInt(filteredNumber);

		return ano;
	}

	public static String parsePercentagem(String sim) {
		String newValue = "";

		boolean dotFound = false;
		for (Character ch : sim.toCharArray()) {
			if (Character.isDigit(ch)) {
				newValue += ch;
			}
			if (!dotFound && (ch == '.' || ch == ',')) {
				newValue += ".";
				dotFound = true;
			}
		}

		double doubleValue = Double.parseDouble(newValue) * 100;
		return new Double(doubleValue).intValue() + "%";
	}
}
