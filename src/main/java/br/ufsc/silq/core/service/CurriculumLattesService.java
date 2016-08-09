package br.ufsc.silq.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import br.ufsc.silq.core.exception.SilqException;
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
		Optional<CurriculumLattes> lattes = Optional.empty();

		if (result.getIdCurriculo() == null || result.getIdCurriculo().isEmpty()) {
			// Caso o currículo não tenha ID (por ser um Lattes muito antigo), sempre adiciona um novo
			// pois não há como identificar um currículo para reuso
			log.debug("Currículo sem ID: {}", result);
		} else {
			lattes = this.lattesRepository.findOneByIdLattesAndDataAtualizacaoCurriculo(result.getIdCurriculo(),
					result.getUltimaAtualizacao());
		}

		if (lattes.isPresent()) {
			log.debug("Currículo já existente na base de dados: {}", lattes.get());
			return lattes.get();
		} else {
			CurriculumLattes novoLattes = new CurriculumLattes();
			novoLattes.setLattesXml(this.documentManager.documentToString(curriculumXml));
			novoLattes.setAreaConhecimento(result.getAreaConhecimento().getNomeArea());
			novoLattes.setSubAreaConhecimento(result.getAreaConhecimento().getNomeSubAreaConhecimento());
			novoLattes.setDataAtualizacaoCurriculo(result.getUltimaAtualizacao());
			novoLattes.setDataAtualizacaoUsuario(new Date());
			novoLattes.setEspecialidade(result.getAreaConhecimento().getNomeEspecialidade());
			novoLattes.setGrandeAreaConhecimento(result.getAreaConhecimento().getNomeGrandeArea());
			novoLattes.setIdLattes(result.getIdCurriculo());
			novoLattes.setNome(result.getNome());
			log.debug("Criando novo currículo na base de dados: {}", novoLattes);
			return this.lattesRepository.save(novoLattes);
		}
	}

	/**
	 * Salva um currículo a partir do upload de um XML Lattes.
	 * Só salva o currículo de fato caso ele ainda não exista na base dados. Caso contrário,
	 * o currículo já existente é utilizado e retornado.
	 *
	 * @param uploadedFile Upload do arquivo contendo o currículo Lattes.
	 * @return {@link CurriculumLattes} criado a partir do parsing do Lattes ou o currículo que já existia antes do upload.
	 * @throws SilqException
	 */
	public CurriculumLattes saveFromUpload(MultipartFile uploadedFile) throws SilqException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(uploadedFile);
		return this.saveFromDocument(document);
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
	 * Limpa currículos não utilizados.
	 * Remove todos os currículos da base de dados que não estejam relacionados com outra entidade, ou seja,
	 * que não sejam currículos pessoais de nenhum usuário e de nenhum pesquisador de grupo.
	 *
	 * @return O número de currículos excluídos.
	 */
	public long cleanCuriculosEmDesuso() {
		// TODO (bonetti): remover apenas currículos não utilizados no último dia!
		StopWatch watch = new StopWatch();
		watch.start();

		List<BigDecimal> ids = this.lattesRepository.findAllEmDesuso();
		long count = ids.size();

		ids.stream().forEach((id) -> this.lattesRepository.delete(id.longValue()));

		watch.stop();
		log.info("Removendo currículos em desuso da base de dados: {} currículos deletados em {}ms",
				count, watch.getTotalTimeMillis());
		return count;
	}

	/**
	 * Job de limpeza de currículos não utilizados.
	 * Executa o método {@link #cleanCuriculosEmDesuso()} todo dia às 02:01h.
	 * Métodos com @Scheduled devem ser void, por isso foi criado um outro método para o job.
	 */
	@Scheduled(cron = "0 1 2 * * ?")
	public void jobCleanCurriculosEmDesuso() {
		this.cleanCuriculosEmDesuso();
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
