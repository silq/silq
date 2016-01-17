package br.ufsc.silq.core.forms;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class AvaliarForm {

	@NotBlank(message = "Campo obrigatório")
	public String area;

	public String anoPublicacaoDe;

	public String anoPublicacaoAte;

	public String nivelSimilaridade;

	public AvaliarForm(String area, String anoPublicacaoDe, String anoPublicacaoAte) {
		this.area = area;
		this.anoPublicacaoDe = anoPublicacaoDe;
		this.anoPublicacaoAte = anoPublicacaoAte;
	}

	public AvaliarForm() {
	}
}
