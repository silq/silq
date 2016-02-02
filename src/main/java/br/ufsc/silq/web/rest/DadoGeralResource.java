package br.ufsc.silq.web.rest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.entities.DadoGeral;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.service.DadoGeralService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class DadoGeralResource {

	@Inject
	private DadoGeralService dadoGeralService;

	/**
	 * POST /api/upload -> envia um currículo, processa-o e retorna o dado geral
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException, SilqErrorException {
		// TODO: checar tipo de arquivo e tamanho
		log.debug("Received file {}", file.getOriginalFilename());

		String newName = UUID.randomUUID().toString() + file.getOriginalFilename();
		File tempFile = File.createTempFile(newName, null);
		file.transferTo(tempFile);
		DadoGeral dadoGeral = this.dadoGeralService.saveFromFile(tempFile);

		return new ResponseEntity<>(dadoGeral, HttpStatus.OK);
	}

	/**
	 * GET /dado-geral -> retorna os dados gerais do usuário atualmente logado
	 */
	@RequestMapping(value = "/dado-geral", method = RequestMethod.GET)
	public ResponseEntity<DadoGeral> getDadoGeral() {
		return new ResponseEntity<>(this.dadoGeralService.getDadoGeral(), HttpStatus.OK);
	}

	/**
	 * DELETE /dado-geral -> remove os dados gerais do usuário atualmente logado
	 */
	@RequestMapping(value = "/dado-geral", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeDadoGeral() {
		this.dadoGeralService.removeCurriculum();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
