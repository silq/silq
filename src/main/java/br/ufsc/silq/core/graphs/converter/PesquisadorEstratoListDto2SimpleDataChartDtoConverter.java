package br.ufsc.silq.core.graphs.converter;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufsc.silq.core.dto.graphsdto.PesquisadorEstratoAnoDto;
import br.ufsc.silq.core.dto.graphsdto.SimpleDataChartDto;

public class PesquisadorEstratoListDto2SimpleDataChartDtoConverter {

	public SimpleDataChartDto toPizzaChartDto(PesquisadorEstratoAnoDto estratoList) {
		SimpleDataChartDto barChartDto = new SimpleDataChartDto();

		ArrayList<String> columns = new ArrayList<>();
		ArrayList<Integer> rows = new ArrayList<>();

		Map<Integer, ConcurrentMap<String, AtomicInteger>> map = estratoList.getMapConceitoQuantidade();

		for (Integer key : map.keySet()) {
			ConcurrentMap<String, AtomicInteger> concurrentMap = map.get(key);
			columns.add(key + "");

			for (String key2 : concurrentMap.keySet()) {
				AtomicInteger atomicInteger = concurrentMap.get(key2);
				rows.add(atomicInteger.get());
			}
		}

		barChartDto.setColumns(columns);
		barChartDto.setRows(rows);

		return barChartDto;
	}
}
