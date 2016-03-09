package br.ufsc.silq.core.business.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.entities.Usuario;
import br.ufsc.silq.core.business.repository.DadoGeralRepository;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.utils.files.FileManager;
import lombok.experimental.Delegate;

@Service
@Transactional
public class DadoGeralService {

	@Delegate
	@Inject
	private DadoGeralRepository dadoGeralRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private LattesParser lattesParser;

	/**
	 * Salva os dados gerais do usuário logado a partir do arquivo XML de seu
	 * currículo Lattes. Remove dados gerais anteriores associados a este
	 * usuário.
	 *
	 * @param curriculumFile
	 *            Currículo Lattes do usuário. Deve ser em formato XML válido.
	 * @return
	 * @throws SilqErrorException
	 */
	protected DadoGeral saveFromFile(File curriculumFile) throws SilqErrorException {
		DadosGeraisResult result = this.lattesParser.parseCurriculaDadosGerais(curriculumFile);
		DadoGeral dadoGeral = new DadoGeral();

		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();

		String curriculum = FileManager.extractCurriculum(curriculumFile);
		dadoGeral.setCurriculoXml(curriculum.getBytes());
		dadoGeral.setUsuario(usuarioLogado);
		dadoGeral.setAreaConhecimento(result.getAreaGrandeAreaConhecimento().getNomeArea());
		dadoGeral.setSubAreaConhecimento(result.getNomeSubAreaConhecimento());
		dadoGeral.setDataAtualizacaoCurriculo(result.getUltimaAtualizacao());
		dadoGeral.setDataAtualizacaoUsuario(new Date());
		dadoGeral.setEspecialidade(result.getNomeEspecialidade());
		dadoGeral.setGrandeAreaConhecimento(result.getAreaGrandeAreaConhecimento().getNomeGrandeArea());
		dadoGeral.setIdCurriculo(result.getIdCurriculo());
		dadoGeral.setNome(result.getNome());

		this.deleteByUsuario(usuarioLogado);

		return this.dadoGeralRepository.save(dadoGeral);
	}

	/**
	 * Salva os dados gerais do usuário logado a partir do arquivo XML de seu
	 * currículo Lattes. Remove dados gerais anteriores associados a este
	 * usuário.
	 *
	 * @param uploadedFile
	 *            Upload do arquivo contendo o Lattes
	 * @return
	 * @throws IOException
	 * @throws SilqErrorException
	 */
	public DadoGeral saveFromUpload(MultipartFile uploadedFile) throws IOException, SilqErrorException {
		// TODO(bonetti): é mesmo necessário criar um File ??
		String newName = UUID.randomUUID().toString();
		File tempFile = File.createTempFile(newName, null);
		uploadedFile.transferTo(tempFile);
		DadoGeral dadoGeral = this.saveFromFile(tempFile);
		tempFile.delete();
		return dadoGeral;
	}

	/**
	 * Retorna o dado geral do usuário logado.
	 *
	 * @return
	 */
	public DadoGeral getDadoGeral() {
		return this.dadoGeralRepository.findByUsuario(this.usuarioService.getUsuarioLogado());
	}

	/**
	 * Remove o currículo do usuário logado, deletando seu DadoGeral da base de
	 * dados.
	 */
	public void removeCurriculum() {
		this.dadoGeralRepository.delete(this.getDadoGeral());
	}

}
