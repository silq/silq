package br.ufsc.silq.core.service;

import java.io.StringReader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.mysema.query.jpa.impl.JPAQuery;

import br.ufsc.silq.core.entities.QUsuario;
import br.ufsc.silq.core.entities.Usuario;
import br.ufsc.silq.core.utils.SilqStringUtils;

@Service
public class UsuarioService {

	@PersistenceContext
	private EntityManager em;

	public Usuario getUsuarioAtual() {
		// String email = Controller.session("connected");
		String email = ""; // TODO (bonetti)

		if (SilqStringUtils.isBlank(email)) {
			return null;
		}

		Usuario usuario = this.getUsuarioByEmail(email);

		return usuario;
	}

	public boolean hasUsuarioLogado() {
		// String email = Controller.session("connected");
		String email = ""; // TODO (bonetti)

		if (SilqStringUtils.isBlank(email)) {
			return false;
		}

		return true;
	}

	public Usuario getUsuarioByEmail(String email) {
		QUsuario qUsuario = QUsuario.usuario;

		JPAQuery query = new JPAQuery(this.em);
		query.from(qUsuario).where(qUsuario.email.eq(email));
		List<Usuario> list = query.list(qUsuario);

		this.em.close();

		return list.get(0);
	}

	public Boolean isNameAlreadyRegistered(String nomeUsuario) {
		QUsuario qUsuario = QUsuario.usuario;

		JPAQuery query = new JPAQuery(this.em);
		List<Long> usuarioList = query.from(qUsuario).where(qUsuario.nome.eq(nomeUsuario)).list(qUsuario.id);

		this.em.close();

		return !usuarioList.isEmpty();
	}

	public Boolean isEmailAlreadyRegistered(String email) {
		QUsuario qUsuario = QUsuario.usuario;

		JPAQuery query = new JPAQuery(this.em);
		List<Long> usuarioList = query.from(qUsuario).where(qUsuario.email.eq(email)).list(qUsuario.id);

		this.em.close();

		return !usuarioList.isEmpty();
	}

	public boolean hasCurriculum() {
		DadoGeralService dadoGeralService = new DadoGeralService();
		return dadoGeralService.getDadoGeral() != null;
	}

	public Document getUserCurriculum() {
		DadoGeralService dadoGeralService = new DadoGeralService();
		byte[] curriculoStringByteArray = dadoGeralService.getDadoGeral().getCurriculoXml();
		String curriculoString = new String(curriculoStringByteArray);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(curriculoString)));
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getUserNameByEmail() {
		QUsuario qUsuario = QUsuario.usuario;

		// String email = Controller.session("connected");
		String email = ""; // TODO (bonetti)

		JPAQuery query = new JPAQuery(this.em);
		query.from(qUsuario).where(qUsuario.email.eq(email));

		Usuario usuario = query.singleResult(qUsuario);

		this.em.close();

		return usuario.getNome();
	}

	public void saveUsuario(Usuario usuario) {
		EntityTransaction transaction = this.em.getTransaction();

		transaction.begin();
		if (usuario.getId() != null) {
			this.em.merge(usuario);
		} else {
			this.em.persist(usuario);
		}
		transaction.commit();
		this.em.close();
	}

}