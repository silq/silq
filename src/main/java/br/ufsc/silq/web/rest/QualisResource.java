package br.ufsc.silq.web.rest;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/qualis")
@Slf4j
public class QualisResource {

	@Inject
	private QualisPeriodicoRepository periodicoRepository;

	@Inject
	private QualisEventoRepository eventoRepository;

	/**
	 * GET /api/qualis/periodicos -> Obtém uma lista (filtrada) com os registros Qualis de periódicos do sistema
	 */
	@RequestMapping(value = "/periodicos", method = RequestMethod.GET)
	public ResponseEntity<?> getPeriodicos(Pageable pageable) {
		log.debug("REST request to get Periodicos: {}", pageable);
		Page<QualisPeriodico> periodicos = this.periodicoRepository.findAll(pageable);
		return new ResponseEntity<>(periodicos, HttpStatus.OK);
	}

	/**
	 * GET /api/qualis/eventos -> Obtém uma lista (filtrada) com os registros Qualis de eventos do sistema
	 */
	@RequestMapping(value = "/eventos", method = RequestMethod.GET)
	public ResponseEntity<?> getEventos(Pageable pageable) {
		log.debug("REST request to get Eventos: {}", pageable);
		Page<QualisEvento> eventos = this.eventoRepository.findAll(pageable);
		return new ResponseEntity<>(eventos, HttpStatus.OK);
	}
}
