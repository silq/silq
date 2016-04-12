package br.ufsc.silq.core.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.enums.AvaliacaoType;
import lombok.Data;

@Data
public class AvaliarForm {

	@NotBlank
	public String area;

	@NotBlank
	public String nivelSimilaridade;

	public String anoPublicacaoDe;

	public String anoPublicacaoAte;

	@NotNull
	public AvaliacaoType tipoAvaliacao = AvaliacaoType.AMBOS;

	public String getAnoPublicacaoAte() {
		// Retorna o mesmo valor de 'anoPublicacaoDe' caso valor inexistente
		return this.anoPublicacaoAte != null ? this.anoPublicacaoAte : this.anoPublicacaoDe;
	}
}
