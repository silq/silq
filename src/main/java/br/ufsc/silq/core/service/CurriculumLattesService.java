package br.ufsc.silq.core.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.CurriculumLattesRepository;
import br.ufsc.silq.core.persistence.repository.GrupoRepository;
import br.ufsc.silq.core.persistence.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CurriculumLattesService {

	@Inject
	private CurriculumLattesRepository lattesRepository;

	@Inject
	private LattesParser lattesParser;

	@Inject
	private DocumentManager documentManager;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private GrupoService grupoService;

	@Inject
	private GrupoRepository grupoRepository;

	/**
	 * Cria uma nova entidade {@link CurriculumLattes} a partir de um documento Lattes em XML
	 *
	 * @param curriculumXml XML do currículo Lattes.
	 * @return A entidade {@link CurriculumLattes}.
	 * @throws SilqLattesException
	 */
	protected CurriculumLattes parseDocument(Document curriculumXml) throws SilqLattesException {
		DadosGeraisResult result = this.lattesParser.extractDadosGerais(curriculumXml);
		CurriculumLattes lattes = new CurriculumLattes();
		lattes.setXml(this.documentManager.documentToString(curriculumXml).getBytes());
		lattes.setAreaConhecimento(result.getAreaGrandeAreaConhecimento().getNomeArea());
		lattes.setSubAreaConhecimento(result.getNomeSubAreaConhecimento());
		lattes.setDataAtualizacaoCurriculo(result.getUltimaAtualizacao());
		lattes.setDataAtualizacaoUsuario(new Date());
		lattes.setEspecialidade(result.getNomeEspecialidade());
		lattes.setGrandeAreaConhecimento(result.getAreaGrandeAreaConhecimento().getNomeGrandeArea());
		lattes.setIdLattes(result.getIdCurriculo());
		lattes.setNome(result.getNome());
		return lattes;
	}

	/**
	 * Salva um currículo de partir de um documento XML Lattes.
	 * Se um currículo igual já existir na base de dados, retorna este registro para reutilizá-lo, caso
	 * contrário, cria um novo currículo na base de dados.
	 *
	 * @param curriculumXml Currículo Lattes do usuário em formato XML.
	 * @return {@link CurriculumLattes} criado a partir do parsing do Lattes.
	 * @throws SilqException
	 */
	protected CurriculumLattes saveFromDocument(Document curriculumXml) throws SilqException {
		DadosGeraisResult result = this.lattesParser.extractDadosGerais(curriculumXml);
		Optional<CurriculumLattes> lattes = this.lattesRepository.findOneByIdLattesAndDataAtualizacaoCurriculo(result.getIdCurriculo(),
				result.getUltimaAtualizacao());

		if (lattes.isPresent()) {
			log.debug("Utilizando currículo já existente da base de dados: {}", lattes.get());
			return lattes.get();
		} else {
			CurriculumLattes novoLattes = new CurriculumLattes();
			novoLattes.setXml(this.documentManager.documentToString(curriculumXml).getBytes());
			novoLattes.setAreaConhecimento(result.getAreaGrandeAreaConhecimento().getNomeArea());
			novoLattes.setSubAreaConhecimento(result.getNomeSubAreaConhecimento());
			novoLattes.setDataAtualizacaoCurriculo(result.getUltimaAtualizacao());
			novoLattes.setDataAtualizacaoUsuario(new Date());
			novoLattes.setEspecialidade(result.getNomeEspecialidade());
			novoLattes.setGrandeAreaConhecimento(result.getAreaGrandeAreaConhecimento().getNomeGrandeArea());
			novoLattes.setIdLattes(result.getIdCurriculo());
			novoLattes.setNome(result.getNome());
			log.debug("Criando novo currículo na base de dados: {}", novoLattes);
			return this.lattesRepository.save(novoLattes);
		}
	}

	/**
	 * Salva um currículo a partir do upload de um XML Lattes.
	 *
	 * @param uploadedFile Upload do arquivo contendo o currículo Lattes.
	 * @return {@link CurriculumLattes} criado a partir do parsing do Lattes.
	 * @throws SilqException
	 */
	public CurriculumLattes saveFromUpload(MultipartFile uploadedFile) throws SilqException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(uploadedFile);
		return this.saveFromDocument(document);
	}

	/**
	 * "Libera" o uso de um currículo. Chamado quando um usuário remove seu currículo ou um pesquisador é removido de um grupo,
	 * o que significa a exclusão do currículo. Como o currículo é compartilhado, ou seja, pode estar sendo usado por múltiplos
	 * usuários e grupos, verifica-se neste método se podemos realmente excluí-lo da base de dados.
	 *
	 * @param curriculum Currículo a ser liberado.
	 */
	public void releaseCurriculum(CurriculumLattes curriculum) {
		if (!this.isCurriculumEmUso(curriculum)) {
			log.debug("Removendo currículo em desuso: {}", curriculum);
			this.lattesRepository.delete(curriculum);
		}
	}

	/**
	 * Checa se o currículo está relacionado a outra entidade no sistema.
	 *
	 * @param curriculum
	 * @return
	 */
	public boolean isCurriculumEmUso(CurriculumLattes curriculum) {
		return !this.usuarioRepository.findAllByCurriculum(curriculum).isEmpty()
				|| !this.grupoRepository.findAllByPesquisadores(curriculum).isEmpty();
	}

	/**
	 * Retorna o currículo com ID especificado, somente caso o usuário logado tenha permissão para acessá-lo.
	 *
	 * @param curriculumId ID do curríulo lattes salvo na base de dados.
	 * @return
	 */
	public Optional<CurriculumLattes> findOneWithPermission(Long curriculumId) {
		CurriculumLattes lattes = this.lattesRepository.findOne(curriculumId);

		if (lattes == null) {
			// Currículo não encontrado
			return Optional.empty();
		}

		Usuario usuario = this.usuarioService.getUsuarioLogado();
		List<Grupo> grupos = this.grupoService.findAllWithPermission();
		if (!lattes.equals(usuario.getCurriculum())
				&& !grupos.stream().anyMatch((grupo) -> grupo.getPesquisadores().contains(lattes))) {
			// Não é currículo do usuário e nem pertence a um grupo dele: sem permissão
			return Optional.empty();
		}

		return Optional.of(lattes);
	}
}
