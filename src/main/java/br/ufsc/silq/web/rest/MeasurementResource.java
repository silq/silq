package br.ufsc.silq.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.data.MeasurementResult;
import br.ufsc.silq.core.service.MeasurementService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/measurement")
@Slf4j
public class MeasurementResource {

	@Inject
	private MeasurementService measurementService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MeasurementResult>> measure() {
		return new ResponseEntity<>(this.measurementService.measure(0.1f, 1f, 0.05f), HttpStatus.OK);
	}
}
