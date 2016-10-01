package br.ufsc.silq.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.data.measure.MeasureResult;
import br.ufsc.silq.core.service.MeasurementService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/measurement")
@Slf4j
public class MeasurementResource {

	@Inject
	private MeasurementService measurementService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MeasureResult>> measure(
			@RequestParam(defaultValue = "0.1") float initialThreshold,
			@RequestParam(defaultValue = "1") float finalThreshold,
			@RequestParam(defaultValue = "0.1") float step,
			@RequestParam(defaultValue = "10000") int limit) {
		return new ResponseEntity<>(this.measurementService.measure(initialThreshold, finalThreshold, step, limit), HttpStatus.OK);
	}
}
