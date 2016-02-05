package br.ufsc.silq.core.graphs.stringjsonhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufsc.silq.core.graphs.dto.PesquisadorEstratoAnoDto;

public class ColumnChartJsonStructureBuilder {

	public static String createStructure(PesquisadorEstratoAnoDto pesquisadorDto) {
		String jsonString = "{ \"cols\": [\n" + getColumn(pesquisadorDto) + "],";
		jsonString += "\n\"rows\":[\n" + getRow(pesquisadorDto) + "]}";

		return jsonString;
	}

	private static String getColumn(PesquisadorEstratoAnoDto pesquisadorDto) {
		String jsonColumns = "{\"id\": \"ano\", \"label\": \"Triênio\", \"pattern\": \"\", \"type\": \"string\"}";

		List<String> conceitos = pesquisadorDto.getConceitos();
		for (int index = 0; index < conceitos.size(); index++) {
			String conceito = conceitos.get(index);
			jsonColumns += ",\n\t{\"id\":\"" + index + "\",\"label\":\"" + conceito
					+ "\",\"pattern\":\"\",\"type\":\"number\"}";
		}

		return jsonColumns;
	}

	private static String getRow(PesquisadorEstratoAnoDto pesquisadorDto) {
		String json = "";

		List<String> anos = pesquisadorDto.getAnos();
		for (int index = 0; index < anos.size(); index++) {
			if (index != 0) {
				json += ",\n";
			}

			json += "{\"c\":[\n";
			String ano = anos.get(index);
			Map<Integer, ConcurrentMap<String, AtomicInteger>> mapConceitoQuantidade = pesquisadorDto
					.getMapConceitoQuantidade();
			ConcurrentMap<String, AtomicInteger> conceitoQuantidadeMap = mapConceitoQuantidade
					.get(Integer.parseInt(ano));
			Set<String> conceitos = conceitoQuantidadeMap.keySet();
			List<String> conceitosList = new ArrayList<>();

			String conceito = null;
			Iterator<String> iterator = conceitos.iterator();
			while (iterator.hasNext()) {
				conceito = iterator.next();
				conceitosList.add(conceito);
			}

			Collections.sort(conceitosList);

			json += "\t{\"v\":\"" + ano + "\",\"f\":null}";
			for (String conceitoString : conceitosList) {
				json += ",\n\t{\"v\":\"" + conceitoQuantidadeMap.get(conceitoString) + "\",\"f\":null}";
			}
			json += "]}";
		}

		return json;
	}
}
