package br.ufsc.silq.core.utils.parser;

import java.util.ArrayList;

public class TextSeparation {

	/**
	 * @param text
	 * @return
	 */
	public static ArrayList<String[]> toArrayListPeriodicos(String[] text) {
		ArrayList<String[]> qualisItems = new ArrayList<String[]>();
		String[] qualisDetail, status, area, estrato;
		String titulo, issn;
		for (int i = 0; i < text.length; i++) {
			qualisDetail = new String[5];
			status = detectarStatus(text[i].split(" "));
			area = detectarArea(status[1].split(" "));
			estrato = detectarEstrato(area[1].split(" "));
			titulo = detectarTitulo(estrato[1])[1];
			issn = detectarTitulo(estrato[1])[0];

			qualisDetail[4] = status[0];
			qualisDetail[3] = area[0];
			qualisDetail[2] = estrato[0];
			qualisDetail[1] = titulo;
			qualisDetail[0] = issn;

			qualisItems.add(qualisDetail);
		}
		return qualisItems;
	}

	public static ArrayList<String[]> toArrayListConferencia(String[] text) {
		ArrayList<String[]> qualisItems = new ArrayList<String[]>();
		String[] qualisDetail, estrato, indiceH;
		String nome, sigla;
		for (int i = 0; i < text.length; i++) {
			qualisDetail = new String[4];
			estrato = detectarEstrato(text[i].split(" "));
			indiceH = detectarEstrato(estrato[1].split(" "));
			nome = detectarNome(indiceH[1])[0];
			sigla = detectarNome(indiceH[1])[1];

			qualisDetail[0] = sigla;
			qualisDetail[1] = nome;
			qualisDetail[2] = indiceH[0];
			qualisDetail[3] = estrato[0];
			qualisItems.add(qualisDetail);

		}
		return qualisItems;
	}

	private static String[] detectarStatus(String[] texto) {
		String[] status = new String[2];
		status[0] = texto[texto.length - 1];
		status[1] = "";

		int index = texto.length - 2;
		while (index >= 0) {
			status[1] = texto[index] + " " + status[1];
			index--;
		}

		return status;
	}

	private static String[] detectarArea(String[] texto) {
		int index = texto.length - 1;
		String[] area = new String[2];
		area[0] = "";
		area[1] = "";

		while (!texto[index].matches("(A\\d|B\\d|C)")) {
			index--;
			if (index == 0) {
				break;
			}
		}

		for (int i = texto.length - 1; i > index; i--) {
			area[0] = texto[i] + " " + area[0];
		}

		for (int i = 0; i <= index; i++) {
			area[1] += texto[i] + " ";
		}
		area[0].trim();
		area[1].trim();

		return area;
	}

	private static String[] detectarEstrato(String[] texto) {
		String[] estrato = new String[2];
		estrato[0] = texto[texto.length - 1];
		estrato[1] = "";
		int index = texto.length - 2;

		while (index >= 0) {
			estrato[1] = texto[index] + " " + estrato[1];
			index--;
		}

		return estrato;
	}

	private static String[] detectarTitulo(String texto) {
		String[] temp = texto.split(" ");
		String[] titulo = new String[2];
		titulo[1] = "";

		if (temp[0].matches("[0-9]*{4}-[0-9]*X?{4}")) {
			titulo[0] = temp[0];
			for (int index = 1; index <= temp.length - 1; index++) {
				titulo[1] += temp[index] + " ";
			}
		} else {
			titulo[0] = "XXXX-XXXX";
			titulo[1] = texto;
		}

		return titulo;
	}

	private static String[] detectarNome(String texto) {
		texto = texto.trim();
		String[] nome = new String[2];
		String[] temp = texto.split(" ");

		nome[1] = temp[0];
		nome[0] = "";

		for (int i = 1; i < temp.length; i++) {
			nome[0] += temp[i] + " ";
		}

		return nome;
	}

}
