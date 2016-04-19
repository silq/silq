package br.ufsc.silq.core.parser.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = { "dadosGerais" })
public class ParseResult {

	private DadosGeraisResult dadosGerais;
	private List<Trabalho> trabalhos = new ArrayList<>();
	private List<Artigo> artigos = new ArrayList<>();

	public void order() {
		Collections.sort(this.getArtigos());
		Collections.sort(this.getTrabalhos());
	}
}
