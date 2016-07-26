package br.ufsc.silq.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import lombok.Data;
import net.sf.ehcache.pool.sizeof.annotations.IgnoreSizeOf;

/**
 * Resultado da avaliação de um currículo Lattes, realizado pelo serviço {@link AvaliacaoResult}.
 */
@Data
@IgnoreSizeOf
public class AvaliacaoResult {

	/**
	 * Form de configuração utilizado na avaliação
	 */
	private final AvaliarForm form;

	/**
	 * Dados gerais extraídos do currículo avaliado
	 */
	private final DadosGeraisResult dadosGerais;

	/**
	 * Lista de artigos avaliados, contendo os respectivos {@link Conceito}s.
	 */
	private List<Artigo> artigos = new ArrayList<>();

	/**
	 * Lista de trabalhos avaliados, contendo os respectivos {@link Conceito}s.
	 */
	private List<Trabalho> trabalhos = new ArrayList<>();

	/**
	 * Ordena os artigos e trabalhos deste resultado de avaliação.
	 */
	public void sort() {
		Collections.sort(this.artigos);
		Collections.sort(this.trabalhos);
	}

	public AvaliacaoStats getStats() {
		return new AvaliacaoStats(this.artigos, this.trabalhos);
	}

	@Override
	public String toString() {
		return "AvaliacaoResult(dadosGerais=" + this.dadosGerais.toString() + ","
				+ "form=" + this.form.toString() + ","
				+ "#artigos=" + this.artigos.size() + ","
				+ "#trabalhos=" + this.trabalhos.size() + ")";
	}
}
