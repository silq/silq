package br.ufsc.silq.web.rest;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import br.ufsc.silq.core.business.service.UsuarioService;
import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.ParseResult;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class SimilarityResource {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private LattesParser lattesParser;

	/**
	 * POST /api/compare/my -> Compara o currículo do usuário atualmente logado
	 */
	@RequestMapping(value = "/compare/my", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@Valid @RequestBody AvaliarForm avaliarForm) {
		Document curriculum = this.usuarioService.getUserCurriculum();
		ParseResult result = this.lattesParser.parseCurriculaAvaliacao(curriculum, avaliarForm, AvaliacaoType.AMBOS);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
