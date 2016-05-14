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

import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.service.AvaliacaoService;
import br.ufsc.silq.core.service.CurriculumLattesService;
import br.ufsc.silq.core.service.GrupoService;
import br.ufsc.silq.web.rest.exception.HttpNotFound;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class GrupoResource {

	@Inject
	private GrupoService grupoService;

	@Inject
	private AvaliacaoService avaliacaoService;

	@Inject
	private CurriculumLattesService curriculumService;

	/**
	 * Pesquisa por um Grupo com o ID especificado e que o usuário atual tenha
	 * permissão de acesso. Retorna 404 caso não encontre
	 *
	 * @param id
	 *            Id do grupo a ser pesquisado
	 * @return
	 */
	protected Grupo findGrupoBydIdWithPermissionOr404(Long id) {
		return this.grupoService.findOneWithPermission(id).orElseThrow(() -> new HttpNotFound("Grupo não encontrado"));
	}

	/**
	 * POST /grupos -> Cria um novo grupo associado ao usuário atual.
	 *
	 * @throws SilqException
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.POST)
	public ResponseEntity<?> createGrupo(@RequestBody @Valid GrupoForm grupo) {
		log.debug("REST request to save Grupo : {}", grupo);
		Grupo result = this.grupoService.create(grupo);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	/**
	 * PUT /grupos -> Atualiza dados de um grupo
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.PUT)
	public ResponseEntity<Grupo> updateGrupo(@RequestBody @Valid GrupoForm grupoForm) {
		log.debug("REST request to update Grupo : {}", grupoForm);
		this.findGrupoBydIdWithPermissionOr404(grupoForm.getId());
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
		return new ResponseEntity<>(this.findGrupoBydIdWithPermissionOr404(id), HttpStatus.OK);
	}

	/**
	 * DELETE /grupos/:id -> Deleta o Grupo com id especificado, caso o usuário
	 * logado tenha permissão
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGrupo(@PathVariable Long id) {
		log.debug("REST request to delete Grupo : {}", id);
		Grupo grupo = this.findGrupoBydIdWithPermissionOr404(id);
		this.grupoService.delete(grupo);
		return ResponseEntity.noContent().build();
	}

	/**
	 * POST /grupos/:id/addPesquisador -> Adiciona o currículo de um pesquisador ao
	 * grupo, através do envio de seu currículo Lattes em XML.
	 *
	 * @throws IOException
	 * @throws SilqException
	 * @throws IllegalStateException
	 */
	@RequestMapping(value = "/grupos/{id}/addPesquisador", method = RequestMethod.POST)
	public ResponseEntity<CurriculumLattes> addPesquisador(@PathVariable Long id, @RequestParam("file") MultipartFile upload)
			throws IllegalStateException, SilqException, IOException {
		log.debug("REST request to add Pesquisador to Grupo : {}, {}", id, upload);
		Grupo grupo = this.findGrupoBydIdWithPermissionOr404(id);
		CurriculumLattes lattes = this.grupoService.addPesquisadorFromUpload(grupo, upload);
		return new ResponseEntity<>(lattes, HttpStatus.OK);
	}

	/**
	 * DELETE /grupos/:grupoId/removePesquisador/:curriculumId -> Remove o currículo de pesquisador de um grupo.
	 */
	@RequestMapping(value = "/grupos/{grupoId}/removePesquisador/{curriculumId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePesquisador(@PathVariable Long grupoId, @PathVariable Long curriculumId) {
		log.debug("REST request to remove Pesquisador: {}, {}", grupoId, curriculumId);
		Grupo grupo = this.findGrupoBydIdWithPermissionOr404(grupoId);
		this.grupoService.removePesquisador(grupo, curriculumId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * GET /grupos/{grupoId}/avaliar/{curriculumId} -> Avalia o currículo do pesquisador de um grupo.
	 *
	 * @throws SilqLattesException
	 */
	@RequestMapping(value = "/grupos/{grupoId}/avaliar/{curriculumId}", method = RequestMethod.GET)
	public ResponseEntity<AvaliacaoResult> avaliarPesquisador(@PathVariable Long grupoId, @PathVariable Long curriculumId)
			throws SilqLattesException {
		log.debug("Avaliar Pesquisador: {}, {}", grupoId, curriculumId);

		Grupo grupo = this.findGrupoBydIdWithPermissionOr404(grupoId);
		CurriculumLattes lattes = this.curriculumService.findOneWithPermission(curriculumId)
				.orElseThrow(() -> new HttpNotFound("Pesquisador não encontrado"));

		// TODO (bonetti) passar do cliente!
		AvaliarForm avaliarForm = new AvaliarForm();
		avaliarForm.setArea(grupo.getNomeArea());

		AvaliacaoResult result = this.avaliacaoService.avaliar(lattes.getXml(), avaliarForm);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
