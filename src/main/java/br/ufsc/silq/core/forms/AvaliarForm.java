package br.ufsc.silq.core.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.enums.AvaliacaoType;
import lombok.Data;

@Data
public class AvaliarForm {

	@NotBlank
	private String area;

	@NotNull
	private NivelSimilaridade nivelSimilaridade = NivelSimilaridade.NORMAL;

	@NotNull
	private Integer anoPublicacaoDe = 1980;

	@NotNull
	private Integer anoPublicacaoAte = 2016000;

	@NotNull
	private AvaliacaoType tipoAvaliacao = AvaliacaoType.AMBOS;

	public void setAnoPublicacaoDe(Integer anoPublicacaoDe) {
		if (anoPublicacaoDe != null) {
			this.anoPublicacaoDe = anoPublicacaoDe;
		}
	}

	public void setAnoPublicacaoAte(Integer anoPublicacaoAte) {
		if (anoPublicacaoAte != null) {
			this.anoPublicacaoAte = anoPublicacaoAte;
		}
	}

	/**
	 * Checa se o período setado no form (anoPublicacaoDe, anoPublicacaoAte) inclui o ano passado como parâmetro.
	 *
	 * @param ano
	 * @return
	 */
	public boolean periodoInclui(int ano) {
		if (this.getAnoPublicacaoDe() == null || this.getAnoPublicacaoAte() == null) {
			return true;
		}

		return ano >= this.getAnoPublicacaoDe() && ano <= this.getAnoPublicacaoAte();
	}
}
