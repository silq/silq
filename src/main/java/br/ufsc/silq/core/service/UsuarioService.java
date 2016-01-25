package br.ufsc.silq.core.service;

import java.io.StringReader;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import br.ufsc.silq.core.entities.Usuario;
import br.ufsc.silq.core.repository.UsuarioRepository;
import br.ufsc.silq.core.service.util.RandomUtil;
import br.ufsc.silq.security.SecurityUtils;
import br.ufsc.silq.web.rest.dto.UsuarioUpdateDTO;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UsuarioService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Delegate
	@Inject
	private UsuarioRepository usuarioRepository;

	/**
	 * Registra um novo usuário, salvando-o na base de dados e cifrando a senha
	 * da entidade parâmetro
	 *
	 * @param usuario
	 * @return
	 */
	public Usuario registerUsuario(@Valid Usuario usuario) {
		String senhaCifrada = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCifrada);
		Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
		log.debug("Usuário registrado {}", usuarioSalvo);
		return usuarioSalvo;
	}

	/**
	 * Retorna o usuário atualmente logado. Joga uma exceção caso não exista
	 *
	 * @return
	 */
	public Usuario getUsuarioLogado() {
		Usuario usuario = this.usuarioRepository.findOneByEmail(SecurityUtils.getCurrentUser().getUsername()).get();
		usuario.getAutoridades().size(); // força o carregamento das autoridades
		return usuario;
	}

	/**
	 * Checa se existe um usuário logado
	 *
	 * @return
	 */
	public boolean hasUsuarioLogado() {
		return SecurityUtils.isAuthenticated();
	}

	public void updateUsuario(UsuarioUpdateDTO info) {
		Usuario usuario = this.getUsuarioLogado();
		usuario.setNome(info.getNome());
		usuario.setSexo(info.getSexo());
		this.usuarioRepository.save(usuario);
	}

	public void alterarSenha(String novaSenha) {
		Usuario usuario = this.getUsuarioLogado();
		String senhaCifrada = this.passwordEncoder.encode(novaSenha);
		usuario.setSenha(senhaCifrada);
		this.usuarioRepository.save(usuario);
	}

	public Optional<Usuario> requestPasswordReset(String mail) {
		return this.usuarioRepository.findOneByEmail(mail).map(usuario -> {
			usuario.setResetKey(RandomUtil.generateResetKey());
			// usuario.setResetDate(ZonedDateTime.now());
			this.usuarioRepository.save(usuario);
			return usuario;
		});
	}

	public Optional<Usuario> completePasswordReset(String novaSenha, String key) {
		log.debug("Reset user password for reset key {}", key);

		return this.usuarioRepository.findOneByResetKey(key).filter(usuario -> {
			// TODO (bonetti): expirar key após 24 horas ??
			// ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
			// return usuario.getResetDate().isAfter(oneDayAgo);
			return true;
		}).map(usuario -> {
			usuario.setSenha(this.passwordEncoder.encode(novaSenha));
			usuario.setResetKey(null);
			// usuario.setResetDate(null);
			this.usuarioRepository.save(usuario);
			return usuario;
		});
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

}