package br.ufsc.silq.core.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.Periodo;
import br.ufsc.silq.core.enums.AvaliacaoType;
import lombok.Data;

@Data
public class AvaliarForm {

	@NotBlank
	private String area;

	@NotNull
	private NivelSimilaridade nivelSimilaridade = NivelSimilaridade.NORMAL;

	@NotNull
	private Periodo periodoAvaliacao = new Periodo();

	@NotNull
	private AvaliacaoType tipoAvaliacao = AvaliacaoType.AMBOS;
}
