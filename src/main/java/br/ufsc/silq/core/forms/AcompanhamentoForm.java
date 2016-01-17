package br.ufsc.silq.core.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcompanhamentoForm {

	public String periodo;

	public String ano;
	
	public String nivelSimilaridade;
	
	public String tipoGrafico;

}
