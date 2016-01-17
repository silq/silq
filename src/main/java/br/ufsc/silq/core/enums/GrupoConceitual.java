package br.ufsc.silq.core.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrupoConceitual {

	ESTRITO("Estrito (A1, A2, B1)"),
	OUTRO_B2("Outro (B2, B3)"),
	OUTRO_B4("Outro (B4, B5, C)"),
	OUTRO_ATE_B2("Outro (A1, A2, B1, B2, B3)"),
	TODOS("Todos");

	private String descricao;

	public static boolean pertenceAoGrupo(String conceito, String conceitoFiltro) {
		if (conceito == null) {
			return false;
		}
		if (conceitoFiltro.equals(ESTRITO.getDescricao())) {
			if (conceito.equals("A1") || conceito.equals("A2") || conceito.equals("B1")) {
				return true;
			}
		}
		if (conceitoFiltro.equals(OUTRO_B2.getDescricao())) {
			if (conceito.equals("B2") || conceito.equals("B3")) {
				return true;
			}
		}
		if (conceitoFiltro.equals(OUTRO_B4.getDescricao())) {
			if (conceito.equals("B4") || conceito.equals("B5") || conceito.equals("C")) {
				return true;
			}
		}
		if (conceitoFiltro.equals(OUTRO_ATE_B2.getDescricao())) {
			if (conceito.equals("A1") || conceito.equals("A2") || conceito.equals("B1") || conceito.equals("B2") || conceito.equals("B3")) {
				return true;
			}
		}
		if (conceitoFiltro.equals(TODOS.getDescricao())) {
			return true;
		}
		return false;
	}

	public static List<String> getConceitosFromGrupo(String conceitoFiltro) {
		List<String> conceitos = new ArrayList<>();

		if (conceitoFiltro == null) {
			return conceitos;
		}

		if (conceitoFiltro.equals(ESTRITO.getDescricao())) {
			conceitos.addAll(Arrays.asList("A1", "A2", "B1"));
		}

		if (conceitoFiltro.equals(OUTRO_B2.getDescricao())) {
			conceitos.addAll(Arrays.asList("B2", "B3"));
		}

		if (conceitoFiltro.equals(OUTRO_B4.getDescricao())) {
			conceitos.addAll(Arrays.asList("B4", "B5", "C"));
		}

		if (conceitoFiltro.equals(OUTRO_ATE_B2.getDescricao())) {
			conceitos.addAll(Arrays.asList("A1", "A2", "B1", "B2", "B3"));
		}

		if (conceitoFiltro.equals(TODOS.getDescricao())) {
			conceitos.addAll(Arrays.asList("A1", "A2", "B1", "B2", "B3", "B4", "B5", "C"));
		}

		return conceitos;
	}
}
