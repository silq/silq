package br.ufsc.silq.core.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.GrupoForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.GrupoRepository;

@Service
public class GrupoService {

	@Inject
	private GrupoRepository grupoRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private CurriculumLattesService curriculumService;

	/**
	 * Cria um novo Grupo e associa-o ao usuário atualmente logado.
	 *
	 * @param form {@link GrupoForm} Formulário com os dados do novo grupo.
	 * @return A entidade {@link Grupo} recém criada.
	 */
	public Grupo create(@Valid GrupoForm form) {
		Grupo entity = new Grupo();
		this.mapFormToEntity(form, entity);
		return this.grupoRepository.save(entity);
	}

	/**
	 * Edita um Grupo existente, caso o usuário atual tenha permissão de acesso.
	 *
	 * @param form {@link GrupoForm} Formulário com os novos dados do grupo.
	 * @return A entidade {@link Grupo} atualizada.
	 */
	public Grupo update(@Valid GrupoForm form) {
		Grupo entity = this.findOneWithPermission(form.getId())
				.orElseThrow(() -> new AuthorizationServiceException("Sem permissão para editar este grupo"));
		this.mapFormToEntity(form, entity);
		return this.grupoRepository.save(entity);
	}

	/**
	 * Extrai os dados do formulário para a entidade.
	 *
	 * @param form
	 * @param entity
	 */
	private void mapFormToEntity(GrupoForm form, Grupo entity) {
		entity.setNomeGrupo(form.getNomeGrupo());
		entity.setNomeInstituicao(form.getNomeInstituicao());
		entity.setNomeArea(form.getNomeArea());
		entity.setCoordenador(this.getCoordenadorLogado());
	}

	/**
	 * Retorna todos os Grupos cujo usuário atualmente logado é coordenador.
	 *
	 * @return Uma lista da entidade {@link Grupo}.
	 */
	public List<Grupo> findAllWithPermission() {
		return this.grupoRepository.findAllByCoordenador(this.getCoordenadorLogado());
	}

	/**
	 * Pesquisa por um grupo com ID especificado pertencente ao usuário
	 * atualmente logado.
	 *
	 * @param id
	 * @return
	 */
	public Optional<Grupo> findOneWithPermission(Long id) {
		return this.grupoRepository.findOneByIdAndCoordenador(id, this.getCoordenadorLogado());
	}

	/**
	 * Retorna a entidade que representa o usuário atualmente logado.
	 *
	 * @return
	 */
	protected Usuario getCoordenadorLogado() {
		return this.usuarioService.getUsuarioLogado();
	}

	/**
	 * Deleta o grupo da base de dados.
	 *
	 * @param grupo
	 */
	public void delete(Grupo grupo) {
		this.grupoRepository.delete(grupo);
	}

	/**
	 * Adiciona um novo pesquisador a um grupo a partir do upload de seu currículo
	 * Lattes em XML.
	 *
	 * @param grupo
	 *            Grupo ao qual o Pesquisador criado será adicionado.
	 * @param upload
	 *            Upload do currículo Lattes.
	 * @return A entidade {@link CurriculumLattes} associada.
	 * @throws SilqException
	 */
	public CurriculumLattes addPesquisadorFromUpload(Grupo grupo, MultipartFile upload) throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(upload);
		grupo.getPesquisadores().add(lattes);
		this.grupoRepository.save(grupo);
		return lattes;
	}

	/**
	 * Remove um pesquisador do grupo.
	 *
	 * @param grupo Grupo do pesquisador.
	 * @param curriculoId ID do currículo {@link CurriculumLattes} a ser removido do grupo.
	 */
	public void removePesquisador(Grupo grupo, Long curriculoId) {
		CurriculumLattes lattes = this.curriculumService.findOneWithPermission(curriculoId).get();
		grupo.getPesquisadores().remove(lattes);
		this.grupoRepository.save(grupo);
	}

}
