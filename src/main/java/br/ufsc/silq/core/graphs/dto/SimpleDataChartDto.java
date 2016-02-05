package br.ufsc.silq.core.graphs.dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SimpleDataChartDto {

	private ArrayList<String> columns;
	private ArrayList<Integer> rows;

}
