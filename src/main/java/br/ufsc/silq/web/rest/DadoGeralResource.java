package br.ufsc.silq.web.rest;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.service.DadoGeralService;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class DadoGeralResource {

	@Inject
	private DadoGeralService dadoGeralService;

	/**
	 * POST /api/dado-geral -> envia um currículo, processa-o e retorna o dado
	 * geral, salvando o DadoGeral na base de dados e associando ao usuário
	 * logado.
	 */
	@RequestMapping(value = "/dado-geral", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException, SilqErrorException {
		log.debug("Received file upload {}", file.getOriginalFilename());
		DadoGeral dadoGeral = this.dadoGeralService.saveFromUpload(file);
		return new ResponseEntity<>(dadoGeral, HttpStatus.OK);
	}

	/**
	 * GET /api/dado-geral -> retorna os dados gerais do usuário logado
	 */
	@RequestMapping(value = "/dado-geral", method = RequestMethod.GET)
	public ResponseEntity<DadoGeral> getDadoGeral() {
		return new ResponseEntity<>(this.dadoGeralService.getDadoGeral(), HttpStatus.OK);
	}

	/**
	 * DELETE /api/dado-geral -> remove os dados gerais do usuário logado
	 */
	@RequestMapping(value = "/dado-geral", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeDadoGeral() {
		this.dadoGeralService.removeCurriculum();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
