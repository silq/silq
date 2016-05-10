package br.ufsc.silq.core.service;

import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.DadosGeraisResult;
import br.ufsc.silq.core.persistence.entities.DadoGeral;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.persistence.repository.DadoGeralRepository;

@Service
@Transactional
public class DadoGeralService {

	@Inject
	private DadoGeralRepository dadoGeralRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private LattesParser lattesParser;

	@Inject
	private DocumentManager uploadManager;

	/**
	 * Salva os dados gerais do usuário logado a partir do arquivo XML de seu
	 * currículo Lattes. Remove dados gerais anteriores associados a este
	 * usuário.
	 *
	 * @param curriculumXml
	 *            Currículo Lattes do usuário em formato XML.
	 * @return
	 * @throws SilqException
	 */
	protected DadoGeral saveFromDocument(Document curriculumXml) throws SilqException {
		DadosGeraisResult result = this.lattesParser.extractDadosGerais(curriculumXml);
		DadoGeral dadoGeral = new DadoGeral();

		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();

		String curriculum = this.uploadManager.documentToString(curriculumXml);
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

		this.dadoGeralRepository.deleteByUsuario(usuarioLogado);

		return this.dadoGeralRepository.save(dadoGeral);
	}

	/**
	 * Salva os dados gerais do usuário logado a partir do arquivo XML de seu
	 * currículo Lattes. Remove dados gerais anteriores associados a este
	 * usuário.
	 *
	 * @param uploadedFile
	 *            Upload do arquivo contendo o currículo Lattes.
	 * @return
	 * @throws SilqException
	 */
	public DadoGeral saveFromUpload(MultipartFile uploadedFile) throws SilqException {
		Document document = this.uploadManager.extractXmlDocumentFromUpload(uploadedFile);
		DadoGeral dadoGeral = this.saveFromDocument(document);
		return dadoGeral;
	}

	/**
	 * Retorna o dado geral do usuário logado.
	 *
	 * @return O {@link DadoGeral} do usuário logado.
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
