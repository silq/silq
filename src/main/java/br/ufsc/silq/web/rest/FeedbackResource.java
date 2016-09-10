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
@RequestMapping("/api/feedback")
@Slf4j
public class FeedbackResource {

	@Inject
	private FeedbackService feedbackService;

	@RequestMapping(value = "/evento/", method = RequestMethod.POST)
	public ResponseEntity<FeedbackEvento> sugerirMatchingEvento(@RequestBody @Valid FeedbackEventoForm form) {
		if (form.getEventoId() == null) {
			this.feedbackService.deleteFeedbackEvento(form);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		FeedbackEvento feedback = this.feedbackService.sugerirMatchingEvento(form);
		return new ResponseEntity<>(feedback, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/periodico/", method = RequestMethod.POST)
	public ResponseEntity<FeedbackPeriodico> sugerirMatchingPeriodico(@RequestBody @Valid FeedbackPeriodicoForm form) {
		if (form.getPeriodicoId() == null) {
			this.feedbackService.deleteFeedbackPeriodico(form);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		FeedbackPeriodico feedback = this.feedbackService.sugerirMatchingPeriodico(form);
		return new ResponseEntity<>(feedback, HttpStatus.CREATED);
	}
}
