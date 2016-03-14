package br.ufsc.silq.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import br.ufsc.silq.core.business.service.UsuarioService;
import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.web.cache.CurriculumCache;
import br.ufsc.silq.web.cache.CurriculumCache.Curriculum;
import br.ufsc.silq.web.rest.form.AvaliacaoLivreForm;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class AvaliarResource {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private LattesParser lattesParser;

	@Inject
	private CurriculumCache curriculumCache;

	/**
	 * POST /api/avaliar/atual -> Compara o currículo do usuário atual
	 */
	@RequestMapping(value = "/avaliar/atual", method = RequestMethod.POST)
	public ResponseEntity<?> avaliarAtual(@Valid @RequestBody AvaliarForm avaliarForm) {
		Document curriculum = this.usuarioService.getUserCurriculum();
		ParseResult result = this.lattesParser.parseCurricula(curriculum, avaliarForm, AvaliacaoType.AMBOS);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * POST /api/avaliar/upload -> envia um currículo e salva-o em cache para
	 * ser posteriormente avaliado.
	 *
	 * @param file
	 *            Currículo Lattes em XML
	 * @param cacheId
	 *            ID do cache a ser utilizado para salvar o currículo. Pedidos
	 *            de avaliação (/api/avaliar) deverão informar o cacheId para
	 *            utilizar os currículos salvos no cache especificado.
	 */
	@RequestMapping(value = "/avaliar/upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("cacheId") String cacheId)
			throws IOException, SilqErrorException {
		log.debug("Received file upload {}", file.getOriginalFilename());
		this.curriculumCache.insert(cacheId, file);
		return new ResponseEntity<>(this.curriculumCache.get(cacheId), HttpStatus.OK);
	}

	/**
	 * POST /api/avaliar/ -> avalia os currículos anteriormente enviados via
	 * 'api/avaliar/upload' (e salvos na cache) de acordo com as opções de
	 * avaliação informadas. O formulário de configuração deve conter o cacheId
	 * dos currículos a serem utilizados na avaliação.
	 */
	@RequestMapping(value = "/avaliar/", method = RequestMethod.POST)
	public ResponseEntity<List<ParseResult>> avaliar(@Valid @RequestBody AvaliacaoLivreForm avaliacaoForm) {
		List<ParseResult> results = new ArrayList<>();

		for (Curriculum curriculum : this.curriculumCache.get(avaliacaoForm.getCacheId())) {
			ParseResult result = this.lattesParser.parseCurricula(curriculum.getFile(), avaliacaoForm,
					AvaliacaoType.AMBOS);
			results.add(result);
		}

		// TODO: tem certeza que é bom limpar o cache aqui?
		this.curriculumCache.clear(avaliacaoForm.getCacheId());
		return new ResponseEntity<>(results, HttpStatus.OK);
	}

}
