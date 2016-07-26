package br.ufsc.silq.web.rest;

import javax.inject.Inject;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.persistence.repository.search.QualisEventoSearchRepository;
import br.ufsc.silq.core.persistence.repository.search.QualisPeriodicoSearchRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/qualis")
@Slf4j
public class QualisResource {

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
	 * GET /api/qualis/periodicos -> Obtém uma lista (filtrada) com os registros Qualis de periódicos do sistema
	 */
	@RequestMapping(value = "/periodicos", method = RequestMethod.GET)
	public ResponseEntity<Page<QualisPeriodico>> getPeriodicos(@RequestParam(value = "query", defaultValue = "") String query, Pageable pageable) {
		log.debug("REST request to get Periodicos: {}", pageable);

		Page<QualisPeriodico> periodicos;
		if (query.isEmpty()) {
			periodicos = this.periodicoSearchRepository.findAll(pageable);
		} else {
			periodicos = this.periodicoSearchRepository.search(this.createQuery(query), pageable);
		}
		return new ResponseEntity<>(periodicos, HttpStatus.OK);
	}

	/**
	 * GET /api/qualis/eventos -> Obtém uma lista (filtrada) com os registros Qualis de eventos do sistema
	 */
	@RequestMapping(value = "/eventos", method = RequestMethod.GET)
	public ResponseEntity<Page<QualisEvento>> getEventos(@RequestParam(value = "query", defaultValue = "") String query, Pageable pageable) {
		log.debug("REST request to get Eventos: {}", pageable);

		Page<QualisEvento> eventos;

		if (query.isEmpty()) {
			eventos = this.eventoSearchRepository.findAll(pageable);
		} else {
			eventos = this.eventoSearchRepository.search(this.createQuery(query), pageable);
		}

		return new ResponseEntity<>(eventos, HttpStatus.OK);
	}

	private SimpleQueryStringBuilder createQuery(String query) {
		SimpleQueryStringBuilder queryBuilder = QueryBuilders.simpleQueryStringQuery(query);
		queryBuilder.defaultOperator(Operator.AND);
		return queryBuilder;
	}

	/**
	 * GET /api/qualis/eventos/recreate
	 */
	@RequestMapping(value = "/recreate", method = RequestMethod.GET)
	public ResponseEntity<String> recreate() {
		log.debug("REST request to recreate Qualis index");

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

		return new ResponseEntity<>("OK!", HttpStatus.OK);
	}
}
