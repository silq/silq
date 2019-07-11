package br.ufsc.silq.web.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.ufsc.silq.core.service.MailService;
import br.ufsc.silq.core.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
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
    private UsuarioService usuarioService;

	@Inject
    private MailService mailService;

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
        this.grupoService.findOneWithPermission(grupoForm.getId()).orElseThrow(() -> new HttpNotFound("Somente o criador deste grupo tem permissão para editar este grupo"));
		Grupo result = this.grupoService.update(grupoForm);
		return ResponseEntity.ok(result);
	}

	/**
	 * GET /grupos -> Retorna os grupos do usuário atual
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.GET)
	public List<Grupo> getAllGrupos() {
		log.debug("REST request to get all Grupos");
        List<Grupo> allWithPermission = this.grupoService.findAllWithPermission();
        List<Grupo> allWithEspectadorPermission = this.grupoService.findAllWithEspectadorPermission();
        allWithPermission.addAll(allWithEspectadorPermission);
        return allWithPermission;
	}

	/**
	 * GET /grupos/:id -> Obtém dados do Grupo com id especificado
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.GET)
	public ResponseEntity<Grupo> getGrupo(@PathVariable Long id) {
		log.debug("REST request to get Grupo : {}", id);
        Grupo grupo = this.grupoService.findOneWithEspectadorPermission(id).orElse(null);
        if(grupo == null){
            grupo  = this.grupoService.findOneWithPermission(id).orElseThrow(() -> new HttpNotFound("Grupo não encontrado"));
        }
        return new ResponseEntity<>(grupo, HttpStatus.OK);
	}

	/**
	 * DELETE /grupos/:id -> Deleta o Grupo com id especificado, caso o usuário
	 * logado tenha permissão
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGrupo(@PathVariable Long id) {
		log.debug("REST request to delete Grupo : {}", id);
        Grupo grupo = this.grupoService.findOneWithPermission(id).orElseThrow(() -> new HttpNotFound("Somente o criador deste grupo tem permissão para deletar este grupo"));
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
        Grupo grupo = this.grupoService.findOneWithPermission(id).orElseThrow(() -> new HttpNotFound("Somente o criador deste grupo tem permissão para editar este grupo"));
		CurriculumLattes lattes = this.grupoService.addPesquisadorFromUpload(grupo, upload);
		return new ResponseEntity<>(lattes, HttpStatus.OK);
	}

	/**
	 * DELETE /grupos/:grupoId/removePesquisador/:curriculumId -> Remove o currículo de pesquisador de um grupo.
	 */
	@RequestMapping(value = "/grupos/{grupoId}/removePesquisador/{curriculumId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePesquisador(@PathVariable Long grupoId, @PathVariable Long curriculumId) {
		log.debug("REST request to remove Pesquisador: {}, {}", grupoId, curriculumId);
        Grupo grupo = this.grupoService.findOneWithPermission(grupoId).orElseThrow(() -> new HttpNotFound("Somente o criador deste grupo tem permissão para editar este grupo"));
		this.grupoService.removePesquisador(grupo, curriculumId);
		return ResponseEntity.noContent().build();
	}

    @RequestMapping(value = "/grupos/{id}/share", method = RequestMethod.POST)
    public ResponseEntity<?> shareGrupo(@PathVariable Long id, @RequestBody String email, HttpServletRequest request) {
        log.debug("REST request to add Email to Grupo : {}, {}", id, email);
        return this.usuarioService.findOneByEmail(email)
            .map(user -> {
                Grupo grupo = this.grupoService.findOneWithPermission(id).orElseThrow(() -> new HttpNotFound("Somente o criador deste grupo tem permissão para compartilhar este grupo"));
                this.grupoService.addUsuarioToGrupo(grupo, user);
                this.mailService.sendShareGroupMail(user, grupo, this.getBaseUrl(request));
                return new ResponseEntity<>(HttpStatus.OK);
            })
            .orElseThrow(() -> new HttpNotFound("Nenhum usuário foi encontrado com o email especificado"));
    }

    @RequestMapping(value = "/grupos/{grupoId}/removeEspectador/{espectadorId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeEspectador(@PathVariable Long grupoId, @PathVariable Long espectadorId) {
        log.debug("REST request to remove Espectador: {}, {}", grupoId, espectadorId);
        Grupo grupo = this.grupoService.findOneWithPermission(grupoId).orElseThrow(() -> new HttpNotFound("Somente o criador deste grupo tem permissão para editar este grupo"));
        this.grupoService.removeEspectador(grupo, espectadorId);
        return ResponseEntity.noContent().build();
    }


    private String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
    }
}
