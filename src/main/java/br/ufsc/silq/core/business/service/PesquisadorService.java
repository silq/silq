package br.ufsc.silq.core.business.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.entities.Pesquisador;
import br.ufsc.silq.core.business.repository.PesquisadorRepository;
import br.ufsc.silq.core.business.service.util.UploadManager;
import br.ufsc.silq.core.exceptions.SilqEntityNotFoundException;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.exceptions.SilqForbiddenActionException;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.PesquisadorResult;
import br.ufsc.silq.core.utils.files.FileManager;

@Service
public class PesquisadorService {

	@Inject
	private PesquisadorRepository pesquisadorRepository;

	@Inject
	private GrupoService grupoService;

	@Inject
	private LattesParser lattesParser;

	@PersistenceContext
	private EntityManager em;

	public void save(Pesquisador pesquisador) {
		this.pesquisadorRepository.save(pesquisador);
	}

	public void deletePesquisador(Long idGrupo, Long idPesquisador) throws SilqForbiddenActionException {
		try {
			Grupo grupo = this.grupoService.findOne(idGrupo);

			Boolean encontrou = false;
			for (Pesquisador pesquisador : grupo.getPesquisadores()) {
				if (pesquisador.getId().equals(idPesquisador)) {
					this.em.getTransaction().begin();
					grupo.getPesquisadores().remove(pesquisador);
					this.em.remove(this.em.contains(pesquisador) ? pesquisador : this.em.merge(pesquisador));
					this.em.merge(grupo);
					this.em.getTransaction().commit();

					encontrou = true;
					break;
				}
			}

			if (!encontrou) {
				throw new SilqEntityNotFoundException("ID de pesquisador é inexistente: " + idPesquisador);
			}
		} catch (SilqEntityNotFoundException e) {
			throw new SilqForbiddenActionException(e.getMessage());
		} finally {
			this.em.close();
		}
	}

	public byte[] loadPesquisadorCurriculum(Long idGrupo, Long idPesquisador) throws SilqForbiddenActionException {
		try {
			Grupo grupo = this.grupoService.findOne(idGrupo);

			byte[] curriculoXml = null;
			Boolean encontrou = false;
			for (Pesquisador pesquisador : grupo.getPesquisadores()) {
				if (pesquisador.getId().equals(idPesquisador)) {
					this.em.getTransaction().begin();
					curriculoXml = pesquisador.getCurriculoXml();
					this.em.getTransaction().commit();
					encontrou = true;
					break;
				}
			}

			if (!encontrou) {
				throw new SilqEntityNotFoundException("ID de pesquisador é inexistente: " + idPesquisador);
			}
			return curriculoXml;
		} catch (SilqEntityNotFoundException e) {
			throw new SilqForbiddenActionException(e.getMessage());
		} finally {
			this.em.close();
		}
	}

	public Optional<Pesquisador> loadPesquisador(Long idPesquisador, Long idGrupo) {
		return this.pesquisadorRepository.findOneByIdAndGrupoId(idPesquisador, idGrupo);
	}

	public boolean existsPesquisadorCurriculo(Long idCurriculoPesquisador, Long idGrupo) {
		return this.pesquisadorRepository.findOneByIdCurriculoAndGrupoId(idCurriculoPesquisador, idGrupo).isPresent();
	}

	public void verifyPesquisador(Long idPesquisador, Long idGrupo, boolean isUpdate, Long idCurriculoPesquisador)
			throws SilqErrorException {
		if (idPesquisador == null) {
			boolean existsPesquisadorCurriculo = this.existsPesquisadorCurriculo(idCurriculoPesquisador, idGrupo);
			if (existsPesquisadorCurriculo) {
				throw new SilqErrorException(
						"Pequisador com o ID " + idCurriculoPesquisador + " já é membro desse grupo.");
			}
		} else if (isUpdate) {
			Pesquisador loadPesquisador = this.loadPesquisador(idPesquisador, idGrupo).get();
			if (loadPesquisador != null && !loadPesquisador.getIdCurriculo().equals(idCurriculoPesquisador)) {
				throw new SilqErrorException(
						"Tentando atualizar pesquisador com currículo de ID diferente: " + idCurriculoPesquisador);
			}
		}

		this.em.close();
	}

	/**
	 * Cria uma nova entidade {@link Pesquisador} a partir do upload de um
	 * currículo Lattes em XML
	 *
	 * @param upload
	 *            Currículo XML do pesquisador
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws SilqErrorException
	 */
	public Pesquisador parseUploadPesquisador(MultipartFile upload)
			throws IllegalStateException, IOException, SilqErrorException {
		File tempFile = UploadManager.createTempFileFromUpload(upload);
		PesquisadorResult result = this.lattesParser.parseCurriculaPesquisador(tempFile);

		Pesquisador pesquisador = new Pesquisador();
		String curriculum = FileManager.extractCurriculum(tempFile);
		pesquisador.setCurriculoXml(curriculum.getBytes());
		pesquisador.setNome(result.getNome());
		pesquisador.setIdCurriculo(result.getIdCurriculo());
		pesquisador.setDataAtualizacaoCurriculo(result.getUltimaAtualizacao());
		pesquisador.setDataAtualizacaoUsuario(new Date());

		tempFile.delete();
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
	 * @throws SilqErrorException
	 */
	public Pesquisador addToGroupFromUpload(Grupo grupo, MultipartFile upload)
			throws IllegalStateException, IOException, SilqErrorException {
		Pesquisador pesquisador = this.parseUploadPesquisador(upload);
		// TODO (bonetti): area de atuação?
		pesquisador.setGrupo(grupo);
		this.pesquisadorRepository.save(pesquisador);
		return pesquisador;
	}
}
