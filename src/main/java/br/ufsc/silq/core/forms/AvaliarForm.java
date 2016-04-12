package br.ufsc.silq.core.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.enums.AvaliacaoType;
import lombok.Data;

@Data
public class AvaliarForm {

	@NotBlank
	private String area;

	@NotBlank
	private String nivelSimilaridade = "0.4";

	@NotBlank
	private String anoPublicacaoDe = "1985";

	@NotBlank
	private String anoPublicacaoAte = "10000";

	@NotNull
	private AvaliacaoType tipoAvaliacao = AvaliacaoType.AMBOS;

	public void setAnoPublicacaoDe(String anoPublicacaoDe) {
		if (anoPublicacaoDe != null && !anoPublicacaoDe.isEmpty()) {
			this.anoPublicacaoDe = anoPublicacaoDe;
		}
	}

	public void setAnoPublicacaoAte(String anoPublicacaoAte) {
		if (anoPublicacaoAte != null && !anoPublicacaoAte.isEmpty()) {
			this.anoPublicacaoAte = anoPublicacaoAte;
		}
	}
}
