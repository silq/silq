package br.ufsc.silq.web.rest;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.forms.FeedbackEventoForm;
import br.ufsc.silq.core.forms.FeedbackPeriodicoForm;
import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.FeedbackPeriodico;
import br.ufsc.silq.core.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class FeedbackResource {

	@Inject
	private FeedbackService feedbackService;

	@RequestMapping(value = "/feedback/evento/", method = RequestMethod.POST)
	public ResponseEntity<FeedbackEvento> sugerirMatchingEvento(@Valid @RequestBody FeedbackEventoForm form) {
		FeedbackEvento feedback = this.feedbackService.sugerirMatchingEvento(form);
		return new ResponseEntity<>(feedback, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/feedback/periodico/", method = RequestMethod.POST)
	public ResponseEntity<FeedbackPeriodico> sugerirMatchingPeriodico(@Valid @RequestBody FeedbackPeriodicoForm form) {
		FeedbackPeriodico feedback = this.feedbackService.sugerirMatchingPeriodico(form);
		return new ResponseEntity<>(feedback, HttpStatus.CREATED);
	}

}
