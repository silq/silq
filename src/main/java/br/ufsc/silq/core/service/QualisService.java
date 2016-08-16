package br.ufsc.silq.core.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.service.SimilarityService.TipoAvaliacao;

@Service
@Transactional
public class QualisService {

	@Inject
	private SimilarityService similarityService;

	/**
	 * Pesquisa por peridócios na base Qualis do sistema que sejam similares à query.
	 *
	 * @param query Query a ser utilizada para busca.
	 * @param pageable Configuração de paginação.
	 * @return Uma página (sublista) dos resultados encontrados.
	 */
	public PageImpl<SimilarityResult<QualisPeriodico>> searchPeriodicos(String query, Pageable pageable) {
		List<Object[]> results = this.similarityService.search(TipoAvaliacao.PERIODICO, query, pageable);
		List<SimilarityResult<QualisPeriodico>> periodicos = results.stream()
				.map(this.similarityService::mapResultToPeriodico)
				.collect(Collectors.toList());
		return new PageImpl<>(periodicos, pageable,
				this.similarityService.searchCount(TipoAvaliacao.PERIODICO, query).intValue());
	}

	/**
	 * Pesquisa por eventos na base Qualis do sistema que sejam similares à query.
	 *
	 * @param query Query a ser utilizada para busca.
	 * @param pageable Configuração de paginação.
	 * @return Uma página (sublista) dos resultados encontrados.
	 */
	public PageImpl<SimilarityResult<QualisEvento>> searchEventos(String query, Pageable pageable) {
		List<Object[]> results = this.similarityService.search(TipoAvaliacao.EVENTO, query, pageable);
		List<SimilarityResult<QualisEvento>> eventos = results.stream()
				.map(this.similarityService::mapResultToEvento)
				.collect(Collectors.toList());
		return new PageImpl<>(eventos, pageable,
				this.similarityService.searchCount(TipoAvaliacao.EVENTO, query).intValue());
	}
}
