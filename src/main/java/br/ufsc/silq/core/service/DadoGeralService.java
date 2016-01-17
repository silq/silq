package br.ufsc.silq.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import br.ufsc.silq.core.entities.DadoGeral;
import br.ufsc.silq.core.entities.QDadoGeral;
import br.ufsc.silq.core.entities.QUsuario;
import br.ufsc.silq.core.entities.Usuario;

@Service
public class DadoGeralService {

	@PersistenceContext
	private EntityManager em;

	public DadoGeral getDadoGeral() {
		// String email = Controller.session("connected");
		String email = ""; // TODO (bonetti)

		List<DadoGeral> listDadoGeral = new ArrayList<>();
		if (email == null) {
			return null;
		}

		QDadoGeral qDadoGeral = QDadoGeral.dadoGeral;
		QUsuario qUsuario = QUsuario.usuario;

		JPAQuery query = new JPAQuery(this.em);
		query.from(qDadoGeral).innerJoin(qDadoGeral.usuario, qUsuario).on(qUsuario.email.eq(email));

		this.em.getTransaction().begin();
		listDadoGeral = query.list(qDadoGeral);
		this.em.getTransaction().commit();
		this.em.close();

		if (listDadoGeral.isEmpty()) {
			return null;
		}
		return listDadoGeral.get(0);
	}

	public void saveDadosGerais(DadoGeral dadoGeral) {
		EntityTransaction transaction = this.em.getTransaction();
		transaction.begin();

		QDadoGeral qDadoGeral = QDadoGeral.dadoGeral;
		QUsuario qUsuario = QUsuario.usuario;

		Usuario usuarioAtual = new UsuarioService().getUsuarioAtual();

		// @formatter:off
		JPAQuery query = new JPAQuery(this.em).from(qDadoGeral).innerJoin(qDadoGeral.usuario, qUsuario)
				.where(qDadoGeral.usuario.id.eq(usuarioAtual.getId()));
		// @formatter:on
		DadoGeral result = query.singleResult(qDadoGeral);

		if (result != null) {
			dadoGeral.setId(result.getId());
			dadoGeral.setGrupos(result.getGrupos());
			this.em.merge(dadoGeral);
		} else {
			Usuario usuario = new UsuarioService().getUsuarioAtual();
			dadoGeral.setUsuario(usuario);
			this.em.persist(dadoGeral);
		}

		transaction.commit();
		this.em.close();
	}

	public void removeCurriculum() {
		DadoGeral dadoGeral = this.getDadoGeral();

		EntityTransaction transaction = this.em.getTransaction();
		transaction.begin();

		this.em.remove(this.em.contains(dadoGeral) ? dadoGeral : this.em.merge(dadoGeral));

		transaction.commit();
		this.em.close();
	}

}
