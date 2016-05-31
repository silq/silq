package br.ufsc.silq.core.data;

import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.service.AvaliacaoService;
import lombok.Data;

/**
 * Resultado da avaliação de uma coleção de currículos Lattes, realizado por {@link AvaliacaoService#avaliarCollection}.
 */
@Data
public class AvaliacaoCollectionResult {

	/**
	 * Form de configuração utilizado na avaliação
	 */
	private final AvaliarForm form;

	/**
	 * Estatísticas desta avaliação;
	 */
	private final AvaliacaoStats stats;
}
