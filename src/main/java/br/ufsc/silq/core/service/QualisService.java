package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.persistence.repository.search.QualisEventoSearchRepository;
import br.ufsc.silq.core.persistence.repository.search.QualisPeriodicoSearchRepository;

@Service
@Transactional(readOnly = true)
public class QualisService {

	@Inject
	private ElasticsearchTemplate elasticsearchTemplate;

	@Inject
	private QualisPeriodicoRepository periodicoRepository;

	@Inject
	private QualisPeriodicoSearchRepository periodicoSearchRepository;

	@Inject
	private QualisEventoRepository eventoRepository;

	@Inject
	private QualisEventoSearchRepository eventoSearchRepository;

	/**
	 * Pesquisa por peridócios na base Qualis do sistema que sejam similares à query.
	 *
	 * @param query Query a ser utilizada para busca.
	 * @param pageable Configuração de paginação.
	 * @return Uma página (sublista) dos resultados encontrados.
	 */
	public Page<QualisPeriodico> searchPeriodicos(String query, Pageable pageable) {
		if (StringUtils.isNotBlank(query)) {
			return this.periodicoSearchRepository.findAll(pageable);
		} else {
			return this.periodicoSearchRepository.search(this.createQuery(query), pageable);
		}
	}

	/**
	 * Pesquisa por eventos na base Qualis do sistema que sejam similares à query.
	 *
	 * @param query Query a ser utilizada para busca.
	 * @param pageable Configuração de paginação.
	 * @return Uma página (sublista) dos resultados encontrados.
	 */
	public Page<QualisEvento> searchEventos(String query, Pageable pageable) {
		if (StringUtils.isNotBlank(query)) {
			return this.eventoSearchRepository.findAll(pageable);
		} else {
			return this.eventoSearchRepository.search(this.createQuery(query), pageable);
		}
	}

	private SimpleQueryStringBuilder createQuery(String query) {
		SimpleQueryStringBuilder queryBuilder = QueryBuilders.simpleQueryStringQuery(query);
		queryBuilder.defaultOperator(Operator.AND);
		return queryBuilder;
	}

	public void recreateIndex() {
		this.eventoSearchRepository.deleteAll();
		this.periodicoSearchRepository.deleteAll();

		this.elasticsearchTemplate.deleteIndex(QualisEvento.class);
		this.elasticsearchTemplate.createIndex(QualisEvento.class);
		this.elasticsearchTemplate.putMapping(QualisEvento.class);
		this.elasticsearchTemplate.refresh(QualisEvento.class);

		this.elasticsearchTemplate.deleteIndex(QualisPeriodico.class);
		this.elasticsearchTemplate.createIndex(QualisPeriodico.class);
		this.elasticsearchTemplate.putMapping(QualisPeriodico.class);
		this.elasticsearchTemplate.refresh(QualisPeriodico.class);

		this.eventoSearchRepository.save(this.eventoRepository.findAll());
		this.periodicoSearchRepository.save(this.periodicoRepository.findAll());
	}
}
