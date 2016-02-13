package br.ufsc.silq.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.service.DadoGeralService;
import br.ufsc.silq.core.business.service.GrupoService;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.web.rest.errors.SilqRESTException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class GrupoResource {

	@Inject
	private GrupoService grupoService;

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
			throw new SilqRESTException("Você deve ter um currículo cadastrado para criar grupos");
		}

		Grupo result = this.grupoService.create(grupo);
		return new ResponseEntity<Grupo>(result, HttpStatus.CREATED);
	}

	/**
	 * PUT /grupos -> Atualiza dados de um grupo
	 */
	@RequestMapping(value = "/grupos", method = RequestMethod.PUT)
	public ResponseEntity<Grupo> updateGrupo(@RequestBody @Valid Grupo updatedGrupo) {
		log.debug("REST request to update Grupo : {}", updatedGrupo);

		Grupo grupo = this.grupoService.findOne(updatedGrupo.getId());

		DadoGeral dadoGeral = this.dadoGeralService.getDadoGeral();
		if (grupo.getCoordenador().getId() != dadoGeral.getId()) {
			throw new AuthorizationServiceException("Você não pode editar este grupo");
		}

		grupo.setCoordenador(dadoGeral);
		Grupo result = this.grupoService.save(grupo);
		return new ResponseEntity<Grupo>(result, HttpStatus.OK);
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
	 * GET /grupos/:id -> Otém dados do Grupo com id especificado
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.GET)
	public ResponseEntity<Grupo> getGrupo(@PathVariable Long id) {
		log.debug("REST request to get Grupo : {}", id);

		return this.grupoService.findOneWithPermission(id).map(result -> {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /grupos/:id -> Deleta o Grupo com id especificado, caso o usuário
	 * logado tenha permissão
	 */
	@RequestMapping(value = "/grupos/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGrupo(@PathVariable Long id) {
		log.debug("REST request to delete Grupo : {}", id);

		return this.grupoService.findOneWithPermission(id).map(result -> {
			this.grupoService.delete(result);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
