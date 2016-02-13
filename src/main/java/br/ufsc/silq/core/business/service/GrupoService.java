package br.ufsc.silq.core.business.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.repository.GrupoRepository;
import br.ufsc.silq.core.forms.GrupoForm;
import lombok.experimental.Delegate;

@Service
public class GrupoService {

	@Inject
	@Delegate
	private GrupoRepository grupoRepository;

	@Inject
	private DadoGeralService dadoGeralService;

	/**
	 * Cria um novo Grupo e associa-o ao usuário atualmente logado
	 *
	 * @param form
	 * @return
	 */
	public Grupo create(@Valid GrupoForm form) {
		Grupo entity = new Grupo();
		entity.setNomeGrupo(form.getNomeGrupo());
		entity.setNomeInstituicao(form.getNomeInstituicao());
		entity.setNomeArea(form.getNomeArea());
		entity.setCoordenador(this.getCoordenadorLogado());
		this.save(entity);
		return entity;
	}

	/**
	 * Retorna todos os Grupos do usuário atualmente logado
	 *
	 * @return
	 */
	public List<Grupo> findAllWithPermission() {
		return this.findAllByCoordenador(this.getCoordenadorLogado());
	}

	/**
	 * Pesquisa por um grupo com ID especificado pertencente ao usuário
	 * atualmente logado
	 *
	 * @param id
	 * @return
	 */
	public Optional<Grupo> findOneWithPermission(Long id) {
		return this.findOneByIdAndCoordenador(id, this.getCoordenadorLogado());
	}

	/**
	 * Retorna a entidade que representa o usuário atualmente logado
	 *
	 * @return
	 */
	protected DadoGeral getCoordenadorLogado() {
		return this.dadoGeralService.getDadoGeral();
	}

}
