package br.ufsc.silq.web.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.entities.Pesquisador;
import br.ufsc.silq.core.business.service.DadoGeralService;
import br.ufsc.silq.core.business.service.GrupoService;
import br.ufsc.silq.core.business.service.PesquisadorService;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.web.rest.exception.HttpBadRequest;
import br.ufsc.silq.web.rest.exception.HttpNotFound;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class GrupoResource {

	@Inject
	private GrupoService grupoService;

	@Inject
	private PesquisadorService pesquisadorService;

	@Inject
	private DadoGeralService dadoGeralService;

	/**
	 * POST /grupos -> Cria um novo grupo associado ao usuário atual
	 *
	 * @throws SilqErrorException
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.POST)
	public ResponseEntity<?> createGrupo(@RequestBody @Valid GrupoForm grupo) {
		log.debug("REST request to save Grupo : {}", grupo);

		DadoGeral dadoGeral = this.dadoGeralService.getDadoGeral();
		if (dadoGeral == null) {
			throw new HttpBadRequest("Você deve ter um currículo cadastrado para criar grupos");
		}

		Grupo result = this.grupoService.create(grupo);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	/**
	 * PUT /grupos -> Atualiza dados de um grupo
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.PUT)
	public ResponseEntity<Grupo> updateGrupo(@RequestBody @Valid GrupoForm grupoForm) {
		log.debug("REST request to update Grupo : {}", grupoForm);
		this.findOneWithPermissionOr404(grupoForm.getId());
		Grupo result = this.grupoService.update(grupoForm);
		return ResponseEntity.ok(result);
	}

	/**
	 * GET /grupos -> Retorna os grupos do usuário atual
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.GET)
	public List<Grupo> getAllGrupos() {
		log.debug("REST request to get all Grupos");
		return this.grupoService.findAllWithPermission();
	}

	/**
	 * GET /grupos/:id -> Obtém dados do Grupo com id especificado
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.GET)
	public ResponseEntity<Grupo> getGrupo(@PathVariable Long id) {
		log.debug("REST request to get Grupo : {}", id);
		return new ResponseEntity<>(this.findOneWithPermissionOr404(id), HttpStatus.OK);
	}

	/**
	 * DELETE /grupos/:id -> Deleta o Grupo com id especificado, caso o usuário
	 * logado tenha permissão
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGrupo(@PathVariable Long id) {
		log.debug("REST request to delete Grupo : {}", id);
		Grupo grupo = this.findOneWithPermissionOr404(id);
		this.grupoService.delete(grupo);
		return ResponseEntity.noContent().build();
	}

	/**
	 * POST /grupos/:id/addPesquisador -> Adiciona um @{link Pesquisador} ao
	 * grupo, através do envio de seu currículo Lattes em XML.
	 *
	 * @throws IOException
	 * @throws SilqErrorException
	 * @throws IllegalStateException
	 */
	@RequestMapping(value = "/grupos/{id}/addPesquisador", method = RequestMethod.POST)
	public ResponseEntity<Pesquisador> addPesquisador(@PathVariable Long id, @RequestParam("file") MultipartFile upload)
			throws IllegalStateException, SilqErrorException, IOException {
		log.debug("REST request to add Pesquisador to Grupo : {}, {}", id, upload);

		Grupo grupo = this.findOneWithPermissionOr404(id);
		Pesquisador pesquisador = this.pesquisadorService.addToGroupFromUpload(grupo, upload);

		return new ResponseEntity<>(pesquisador, HttpStatus.OK);
	}

	/**
	 * Pesquisa por um Grupo com o ID especificado e que o usuário atual tenha
	 * permissão de acesso. Retorna 404 caso não encontre
	 *
	 * @param id
	 *            Id do grupo a ser pesquisado
	 * @return
	 */
	protected Grupo findOneWithPermissionOr404(Long id) {
		return this.grupoService.findOneWithPermission(id).orElseThrow(() -> new HttpNotFound("Grupo não encontrado"));
	}
}
