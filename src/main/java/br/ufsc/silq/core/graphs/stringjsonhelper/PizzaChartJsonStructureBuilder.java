package br.ufsc.silq.core.graphs.stringjsonhelper;

import br.ufsc.silq.core.graphs.dto.SimpleDataChartDto;

public class PizzaChartJsonStructureBuilder {

	public static String getJson(SimpleDataChartDto simpleDataChartDto) {
		String jsonString = "{ \"cols\":[";
		jsonString += getColumn(simpleDataChartDto);
		jsonString += "],\"rows\":[";
		jsonString += getRow(simpleDataChartDto);
		jsonString += "]}";

		return jsonString;
	}

	private static String getColumn(SimpleDataChartDto pizzaChartDto) {
		String json = "";

		for (int index = 0; index < pizzaChartDto.getColumns().size(); index++) {
			if (index != 0) {
				json += ",\n";
			}
			String column = pizzaChartDto.getColumns().get(index);
			json += "{\"id\":\"" + index + "\",\"label\":\"" + column + "\",\"pattern\":\"\",\"type\":\"string\"}";
		}

		return json;
	}

	private static String getRow(SimpleDataChartDto pizzaChartDto) {
		String json = "";

		for (int index = 0; index < pizzaChartDto.getColumns().size(); index++) {
			if (index != 0) {
				json += ",\n";
			}

			String column = pizzaChartDto.getColumns().get(index);
			Integer quantidade = pizzaChartDto.getRows().get(index);

			json += "{\"c\":[{\"v\":\"" + column + "\",\"f\":null},{\"v\":" + quantidade + ",\"f\":null}]}";
		}

		return json;
	}
}
