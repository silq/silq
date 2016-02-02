package br.ufsc.silq.core.service;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.ufsc.silq.core.dto.parser.struct.DadosGeraisResult;
import br.ufsc.silq.core.entities.DadoGeral;
import br.ufsc.silq.core.entities.Usuario;
import br.ufsc.silq.core.exceptions.SilqErrorException;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.repository.DadoGeralRepository;
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

	/**
	 * Salva os dados gerais do usuário atualmente logado a partir do arquivo
	 * XML de seu currículo Lattes. Remove dados gerais anteriores associados a
	 * este usuário.
	 *
	 * @param curriculumFile
	 *            Currículo Lattes do usuário. Deve ser em formato XML válido.
	 * @return
	 * @throws SilqErrorException
	 */
	public DadoGeral saveFromFile(File curriculumFile) throws SilqErrorException {
		DadosGeraisResult result = LattesParser.parseCurriculaDadosGerais(curriculumFile);
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

		return this.save(dadoGeral);
	}

	/**
	 * Retorna o dado geral do usuário atual
	 *
	 * @return
	 */
	public DadoGeral getDadoGeral() {
		return this.findByUsuario(this.usuarioService.getUsuarioLogado());
	}

	/**
	 * Remove o currículo do usuário atual, deletando seu Dado Geral da base de
	 * dados
	 */
	public void removeCurriculum() {
		this.delete(this.getDadoGeral());
	}

}
