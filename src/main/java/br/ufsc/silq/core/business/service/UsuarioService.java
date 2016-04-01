package br.ufsc.silq.core.business.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufsc.silq.core.business.entities.Usuario;
import br.ufsc.silq.core.business.repository.UsuarioRepository;
import br.ufsc.silq.core.business.service.util.RandomUtil;
import br.ufsc.silq.core.forms.usuario.RegisterForm;
import br.ufsc.silq.core.forms.usuario.UsuarioUpdateForm;
import br.ufsc.silq.security.SecurityUtils;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UsuarioService {

	@Inject
	private PasswordEncoder passwordEncoder;

	@Delegate
	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private DadoGeralService dadoGeralService;

	/**
	 * Registra um novo usuário, salvando-o na base de dados e cifrando a senha
	 * do formulário parâmetro
	 *
	 * @param usuario
	 *            Formulário de registro
	 * @return A nova entidade Usuario criada
	 */
	public Usuario registerUsuario(@Valid RegisterForm form) {
		String senhaCifrada = this.passwordEncoder.encode(form.getSenha());

		Usuario usuario = new Usuario();
		usuario.setNome(form.getNome());
		usuario.setEmail(form.getEmail());
		usuario.setSexo(form.getSexo());
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

	public void updateUsuario(@Valid UsuarioUpdateForm info) {
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

		return this.usuarioRepository.findOneByResetKey(key).map(usuario -> {
			usuario.setSenha(this.passwordEncoder.encode(novaSenha));
			usuario.setResetKey(null);
			this.usuarioRepository.save(usuario);
			return usuario;
		});
	}
}