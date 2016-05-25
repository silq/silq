package br.ufsc.silq.core.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.forms.usuario.RegisterForm;
import br.ufsc.silq.core.forms.usuario.UsuarioUpdateForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.UsuarioRepository;
import br.ufsc.silq.core.service.util.RandomUtil;
import br.ufsc.silq.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UsuarioService {

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private CurriculumLattesService curriculumService;

	/**
	 * Cria uma nova entidade {@link Usuario} a partir de um formulário de registro, mas não a salva no banco.
	 *
	 * @param form Formulário de registro contendo as informações do novo usuário.
	 * @return Uma nova entidade {@link Usuario} não salva.
	 */
	protected Usuario newUser(@Valid RegisterForm form) {
		String senhaCifrada = this.passwordEncoder.encode(form.getSenha());
		Usuario usuario = new Usuario();
		usuario.setNome(form.getNome());
		usuario.setEmail(form.getEmail());
		usuario.setSenha(senhaCifrada);
		return usuario;
	}

	/**
	 * Registra um novo usuário, salvando-o na base de dados e tornando-o ativo.
	 *
	 * @param form Formulário de registro.
	 * @return A nova entidade Usuario criada.
	 */
	public Usuario register(@Valid RegisterForm form) {
		Usuario usuario = this.newUser(form);
		usuario.setAtivo(true);

		Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
		log.debug("Usuário registrado {}", usuarioSalvo);
		return usuarioSalvo;
	}

	/**
	 * Requisita um pedido de registro a um novo usuário.
	 *
	 * @param form Formulário de registro do novo usuário.
	 * @return A entidade {@link Usuario} criada, mas ainda não ativa.
	 */
	public Usuario registerRequest(@Valid RegisterForm form) {
		Usuario usuario = this.newUser(form);
		usuario.setAtivo(false);
		usuario.setRegisterKey(RandomUtil.generateRegisterKey());

		Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
		log.debug("Requisição de registro {}", usuarioSalvo);
		return usuarioSalvo;
	}

	/**
	 * Finaliza o processo de registro de um usuário que iniciou com {@link #registerRequest(RegisterForm)}, tornando-o ativo.
	 *
	 * @param registerKey A chave de registro criada por {@link #registerRequest(RegisterForm)}.
	 * @return
	 */
	public Optional<Usuario> registerFinish(String registerKey) {
		return this.usuarioRepository.findOneByRegisterKey(registerKey).map((usuario) -> {
			usuario.setAtivo(true);
			usuario.setRegisterKey(null);
			return usuario;
		});
	}

	/**
	 * Retorna o usuário que possua o e-mail especificado.
	 *
	 * @param email E-mail do usuário.
	 * @return A entidade {@link Usuario} que possue o e-mail especificado.
	 */
	public Optional<Usuario> findOneByEmail(String email) {
		return this.usuarioRepository.findOneByEmail(email);
	}

	/**
	 * Retorna o usuário atualmente logado. Joga uma exceção caso não exista.
	 *
	 * @return A entidade {@link Usuario} do usuário logado.
	 */
	public Usuario getUsuarioLogado() {
		Usuario usuario = this.usuarioRepository.findOneByEmailAndAtivoTrue(SecurityUtils.getCurrentUser().getUsername())
				.orElseThrow(() -> new IllegalStateException("User not found!"));
		usuario.getAutoridades().size(); // força o carregamento das autoridades
		return usuario;
	}

	/**
	 * Atualiza as informações do usuário logado.
	 *
	 * @param info Novas informações do usuário.
	 * @return A entidade {@link Usuario} atualizada.
	 */
	public Usuario updateUsuario(@Valid UsuarioUpdateForm info) {
		Usuario usuario = this.getUsuarioLogado();
		usuario.setNome(info.getNome());
		return this.usuarioRepository.save(usuario);
	}

	/**
	 * Altera a senha do usuário logado.
	 *
	 * @param novaSenha Nova senha definida. Será codificada e salva no banco.
	 * @return A entidade {@link Usuario} com a senha alterada.
	 */
	public Usuario alterarSenha(String novaSenha) {
		Usuario usuario = this.getUsuarioLogado();
		String senhaCifrada = this.passwordEncoder.encode(novaSenha);
		usuario.setSenha(senhaCifrada);
		return this.usuarioRepository.save(usuario);
	}

	/**
	 * Requisita uma alteração de senha para um usuário, criando uma chave de alteração de senha e a salvando no banco
	 * para uso posterior pelo {@link #completePasswordReset(String, String)}
	 *
	 * @param email E-mail do usuário para o qual será enviado um link para alteração de senha.
	 * @return A entidade {@link Usuario} dona do e-mail.
	 */
	public Optional<Usuario> requestPasswordReset(String email) {
		return this.usuarioRepository.findOneByEmailAndAtivoTrue(email).map(usuario -> {
			usuario.setResetKey(RandomUtil.generateResetKey());
			// usuario.setResetDate(ZonedDateTime.now());
			this.usuarioRepository.save(usuario);
			return usuario;
		});
	}

	/**
	 * Finaliza o processo de alteração de senha de um usuário dado um {@code resetKey} válido criado pelo
	 * método {@link #requestPasswordReset(String)}
	 *
	 * @param novaSenha Nova senha do usuário.
	 * @param resetKey A chave de alteração de senha.
	 * @return A entidade {@link Usuario}} que teve a senha modificada.
	 */
	public Optional<Usuario> completePasswordReset(String novaSenha, String resetKey) {
		log.debug("Reset user password for reset key {}", resetKey);

		return this.usuarioRepository.findOneByResetKey(resetKey).map(usuario -> {
			usuario.setSenha(this.passwordEncoder.encode(novaSenha));
			usuario.setResetKey(null);
			this.usuarioRepository.save(usuario);
			return usuario;
		});
	}

	/**
	 * Retorna o currículo do usuário logado.
	 *
	 * @return O {@link CurriculumLattes} do usuário logado.
	 */
	public CurriculumLattes getCurriculumUsuarioLogado() {
		return this.getUsuarioLogado().getCurriculum();
	}

	/**
	 * Remove o currículo do usuário, deletando seu {@link CurriculumLattes} da base de dados caso
	 * ele não seja mais referenciado.
	 *
	 * @param usuario Entidade da qual deve ser removido o currículo.
	 */
	public void removeCurriculumUsuario(Usuario usuario) {
		usuario.setCurriculum(null);
		this.usuarioRepository.save(usuario);
	}

	/**
	 * Remove o currículo do usuário logado, deletando seu {@link CurriculumLattes} da base de dados caso
	 * ele não seja mais referenciado.
	 */
	public void removeCurriculumUsuarioLogado() {
		this.removeCurriculumUsuario(this.getUsuarioLogado());
	}

	/**
	 * Salva (ou altera) o currículo de um usuário a partir do arquivo XML de seu currículo Lattes.
	 *
	 * @param uploadedFile Upload do arquivo contendo o currículo Lattes.
	 * @return {@link CurriculumLattes} criado a partir do parsing do Lattes.
	 * @throws SilqException
	 */
	public CurriculumLattes saveCurriculumUsuario(Usuario usuario, MultipartFile uploadedFile) throws SilqException {
		CurriculumLattes lattes = this.curriculumService.saveFromUpload(uploadedFile);
		usuario.setCurriculum(lattes);
		this.usuarioRepository.save(usuario);
		return lattes;
	}

	/**
	 * Salva o currículo do usuário logado a partir do arquivo XML de seu
	 * currículo Lattes. Remove currículo anterior associados a este usuário.
	 *
	 * @param uploadedFile Upload do arquivo contendo o currículo Lattes.
	 * @return {@link CurriculumLattes} criado a partir do parsing do Lattes.
	 * @throws SilqException
	 */
	public CurriculumLattes saveCurriculumUsuarioLogado(MultipartFile uploadedFile) throws SilqException {
		return this.saveCurriculumUsuario(this.getUsuarioLogado(), uploadedFile);
	}
}
