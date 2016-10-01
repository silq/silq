package br.ufsc.silq.core.data.measure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasureEntry {
	private Boolean match;
	private Double reciprocralRank;
}
