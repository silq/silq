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

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/curriculum")
@Slf4j
public class CurriculumResource {

	@Inject
	private UsuarioService usuarioService;

	/**
	 * POST /api/curriculum -> envia um currículo Lattes, processa-o e salva-o na base de dados,
	 * associando ao usuário atual. Retorna o {@link CurriculumLattes} resultante.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException, SilqException {
		log.debug("Received file upload {}", file.getOriginalFilename());
		CurriculumLattes lattes = this.usuarioService.saveCurriculumUsuarioLogado(file);
		return new ResponseEntity<>(lattes, HttpStatus.OK);
	}

	/**
	 * GET /api/curriculum -> retorna o {@link CurriculumLattes} do usuário atual.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<CurriculumLattes> getDadoGeral() {
		return new ResponseEntity<>(this.usuarioService.getCurriculumUsuarioLogado(), HttpStatus.OK);
	}

	/**
	 * DELETE /api/curriculum -> remove o {@link CurriculumLattes} associado ao usuário atual.
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> removeDadoGeral() {
		this.usuarioService.removeCurriculumUsuarioLogado();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
