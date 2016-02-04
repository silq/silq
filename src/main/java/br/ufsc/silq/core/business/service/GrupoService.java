package br.ufsc.silq.core.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.entities.QDadoGeral;
import br.ufsc.silq.core.business.entities.QGrupo;
import br.ufsc.silq.core.dto.commondto.GroupDto;
import br.ufsc.silq.core.exceptions.SilqEntityNotFoundException;
import br.ufsc.silq.core.exceptions.SilqForbiddenActionException;
import br.ufsc.silq.core.graphs.converter.GrupoEntity2DtoConverter;

@Service
public class GrupoService {

	@PersistenceContext
	private EntityManager em;

	public void saveGrupo(Grupo grupo) {
		EntityTransaction transaction = this.em.getTransaction();

		transaction.begin();
		if (grupo.getId() != null) {
			this.em.merge(grupo);
		} else {
			this.em.persist(grupo);
		}
		transaction.commit();
		this.em.close();
	}

	public void deleteGrupo(Long id) throws SilqForbiddenActionException {
		try {
			Grupo group = this.loadGroup(id);

			this.em.getTransaction().begin();
			this.em.remove(this.em.contains(group) ? group : this.em.merge(group));
			this.em.getTransaction().commit();
		} catch (SilqEntityNotFoundException e) {
			throw new SilqForbiddenActionException("ID de grupo é inexistente: " + id);
		} finally {
			this.em.close();
		}
	}

	public Grupo loadGroup(Long id) throws SilqEntityNotFoundException {
		this.em.getTransaction().begin();

		QGrupo qGrupo = QGrupo.grupo;
		QDadoGeral qDadoGeral = QDadoGeral.dadoGeral;

		DadoGeralService dadoGeralService = new DadoGeralService();
		DadoGeral usuarioAtual = dadoGeralService.getDadoGeral();

		JPAQuery query = new JPAQuery(this.em);
		// @formatter:off
		query.from(qGrupo).innerJoin(qGrupo.coordenador, qDadoGeral)
				.where(qDadoGeral.usuario.id.eq(usuarioAtual.getUsuario().getId()).and(qGrupo.id.eq(id)));
		// @formatter:on

		Grupo group = query.singleResult(qGrupo);
		this.em.getTransaction().commit();

		if (group == null) {
			throw new SilqEntityNotFoundException("Entidade Grupo inexistente. ID: " + id);
		}

		this.em.close();

		return group;
	}

	public List<GroupDto> getGrupos() {
		List<Grupo> userGroups = this.getUserGroups(this.em);
		this.em.close();

		List<GroupDto> gruposDtoList = GrupoEntity2DtoConverter.convertGrupos(userGroups);

		return gruposDtoList;
	}

	private List<Grupo> getUserGroups(EntityManager em) {
		List<Grupo> groupList = new ArrayList<Grupo>();
		em.getTransaction().begin();

		QGrupo qGrupo = QGrupo.grupo;
		QDadoGeral qDadoGeral = QDadoGeral.dadoGeral;

		DadoGeralService dadoGeralService = new DadoGeralService();
		DadoGeral usuarioAtual = dadoGeralService.getDadoGeral();

		if (usuarioAtual == null) {
			return groupList;
		}

		JPAQuery query = new JPAQuery(em);
		// @formatter:off
		query.from(qGrupo).join(qGrupo.coordenador, qDadoGeral)
				.where(qDadoGeral.usuario.id.eq(usuarioAtual.getUsuario().getId()));
		groupList = query.list(qGrupo);
		// @formatter:on

		em.getTransaction().commit();

		return groupList;
	}

}
