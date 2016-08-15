package br.ufsc.silq.web.rest;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.service.QualisService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/qualis")
@Slf4j
public class QualisResource {

	@Inject
	private QualisService qualisService;

	/**
	 * GET /api/qualis/periodicos -> Obtém uma lista (filtrada) com os registros Qualis de periódicos do sistema
	 */
	@RequestMapping(value = "/periodicos", method = RequestMethod.GET)
	public ResponseEntity<Page<SimilarityResult<QualisPeriodico>>> getPeriodicos(
			@RequestParam(value = "query", defaultValue = "") String query, Pageable pageable) {
		log.debug("REST request to get Periodicos: {}", pageable);

		PageImpl<SimilarityResult<QualisPeriodico>> periodicos = this.qualisService.searchPeriodicos(query, pageable);
		return new ResponseEntity<>(periodicos, HttpStatus.OK);
	}

	/**
	 * GET /api/qualis/eventos -> Obtém uma lista (filtrada) com os registros Qualis de eventos do sistema
	 */
	@RequestMapping(value = "/eventos", method = RequestMethod.GET)
	public ResponseEntity<Page<SimilarityResult<QualisEvento>>> getEventos(
			@RequestParam(value = "query", defaultValue = "") String query, Pageable pageable) {
		log.debug("REST request to get Eventos: {}", pageable);

		PageImpl<SimilarityResult<QualisEvento>> eventos = this.qualisService.searchEventos(query, pageable);
		return new ResponseEntity<>(eventos, HttpStatus.OK);
	}
}
