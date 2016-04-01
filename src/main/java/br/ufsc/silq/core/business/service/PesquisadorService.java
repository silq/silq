package br.ufsc.silq.core.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.entities.Pesquisador;
import br.ufsc.silq.core.business.repository.PesquisadorRepository;
import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqUploadException;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.PesquisadorResult;

@Service
public class PesquisadorService {

	@Inject
	private PesquisadorRepository pesquisadorRepository;

	@Inject
	private LattesParser lattesParser;

	@Inject
	private DocumentManager documentManager;

	/**
	 * Salva a entidade {@link Pesquisador}.
	 *
	 * @param pesquisador
	 */
	public void save(Pesquisador pesquisador) {
		this.pesquisadorRepository.save(pesquisador);
	}

	/**
	 * Cria uma nova entidade {@link Pesquisador} a partir do upload de um
	 * currículo Lattes em XML.
	 *
	 * @param upload
	 *            Currículo XML do pesquisador.
	 * @return
	 * @throws SilqUploadException
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws SilqException
	 */
	public Pesquisador parseUploadPesquisador(MultipartFile upload) throws SilqUploadException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(upload);
		PesquisadorResult result = this.lattesParser.parseCurriculumPesquisador(document);

		Pesquisador pesquisador = new Pesquisador();
		String curriculum = this.documentManager.documentToString(document);
		pesquisador.setCurriculoXml(curriculum.getBytes());
		pesquisador.setNome(result.getNome());
		pesquisador.setIdCurriculo(result.getIdCurriculo());
		pesquisador.setDataAtualizacaoCurriculo(result.getUltimaAtualizacao());
		pesquisador.setDataAtualizacaoUsuario(new Date());
		return pesquisador;
	}

	/**
	 * Cria um novo {@link Pesquisador} a partir do upload de seu currículo
	 * Lattes em XML e o adiciona ao grupo.
	 *
	 * @param grupo
	 *            Grupo ao qual o Pesquisador criado será adicionado.
	 * @param upload
	 *            Upload do currículo Lattes.
	 * @return A nova entidade {@link Pesquisador} criada.
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws SilqException
	 */
	public Pesquisador addToGroupFromUpload(Grupo grupo, MultipartFile upload)
			throws IllegalStateException, IOException, SilqException {
		Pesquisador pesquisador = this.parseUploadPesquisador(upload);
		// TODO (bonetti): area de atuação?
		pesquisador.setGrupo(grupo);
		this.pesquisadorRepository.save(pesquisador);
		return pesquisador;
	}

	/**
	 * Remove o Pesquisador.
	 *
	 * @param pesquisadorId
	 *            ID do pesquisador a ser removido.
	 */
	public void remove(Long pesquisadorId) {
		this.pesquisadorRepository.delete(pesquisadorId);
	}

	/**
	 * Retorna o {@link Pesquisador} com ID informado.
	 *
	 * @param pesquisadorId
	 * @return
	 */
	public Optional<Pesquisador> findOneById(Long pesquisadorId) {
		return this.pesquisadorRepository.findOneById(pesquisadorId);
	}

	/**
	 * Retorna a entidade {@link Pesquisador} que esteja no grupo informado.
	 *
	 * @param idPesquisador
	 *            Id do {@link Pesquisador}.
	 * @param idGrupo
	 *            Id do {@link Grupo}.
	 * @return
	 */
	public Optional<Pesquisador> findOneByIdAndGrupoId(Long idPesquisador, Long idGrupo) {
		return this.pesquisadorRepository.findOneByIdAndGrupoId(idPesquisador, idGrupo);
	}
}
