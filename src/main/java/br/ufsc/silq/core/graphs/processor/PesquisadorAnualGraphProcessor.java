package br.ufsc.silq.core.graphs.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import br.ufsc.silq.core.business.entities.Grupo;
import br.ufsc.silq.core.business.entities.Pesquisador;
import br.ufsc.silq.core.business.service.GrupoService;
import br.ufsc.silq.core.business.service.PesquisadorService;
import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.enums.GrupoConceitual;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.graphs.dto.PesquisadorEstratoAnoDto;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;

@Component
public class PesquisadorAnualGraphProcessor {

	@Inject
	private GrupoService grupoService;

	@Inject
	private PesquisadorService pesquisadorService;

	@Inject
	private LattesParser lattesParser;

	public PesquisadorEstratoAnoDto getPesquisadorEstratoAnoDto(Long idGrupo, String nivelSimilaridade, String conceito, AvaliacaoType tipoAvaliacao, Integer ano)
			throws SilqLattesException {
		PesquisadorEstratoAnoDto pesquisadorEstratoAnoDto = new PesquisadorEstratoAnoDto();

		Grupo grupo = this.grupoService.findOne(idGrupo);

		for (Pesquisador pesquisador : grupo.getPesquisadores()) {
			Pesquisador entity = this.pesquisadorService.findOneByIdAndGrupoId(idGrupo, pesquisador.getId()).get();
			AvaliarForm avaliarForm = this.getAvaliarForm(pesquisador.getAreaAtuacao(), ano, nivelSimilaridade, tipoAvaliacao);
			ParseResult parseResult = this.lattesParser.parseCurriculum(entity.getCurriculoXml(), avaliarForm);
			this.processParseResult(pesquisadorEstratoAnoDto, ano, parseResult, conceito);
			this.processDto(pesquisadorEstratoAnoDto);
		}

		return pesquisadorEstratoAnoDto;
	}

	private AvaliarForm getAvaliarForm(String areaAtuacao, Integer ano, String nivelSimilaridade, AvaliacaoType tipoAvaliacao) {
		AvaliarForm avaliarForm = new AvaliarForm();

		avaliarForm.setArea("Ciência da Computação");
		avaliarForm.setNivelSimilaridade(nivelSimilaridade);
		avaliarForm.setAnoPublicacaoDe(ano);
		avaliarForm.setAnoPublicacaoAte(ano);
		avaliarForm.setTipoAvaliacao(tipoAvaliacao);

		return avaliarForm;
	}

	private void processParseResult(PesquisadorEstratoAnoDto pesquisadorEstratoAnoDto, Integer ano, ParseResult parseResult, String conceitoFiltro) {
		Map<Integer, ConcurrentMap<String, AtomicInteger>> mapAnoConceito = pesquisadorEstratoAnoDto.getMapConceitoQuantidade();

		ConcurrentMap<String, AtomicInteger> mapConceitoQuantidade = mapAnoConceito.get(ano);
		if (mapConceitoQuantidade == null) {
			mapAnoConceito.put(ano, new ConcurrentHashMap<String, AtomicInteger>());
			mapConceitoQuantidade = mapAnoConceito.get(ano);
		}

		this.putConceitosIntoMap(mapAnoConceito.get(ano), conceitoFiltro);

		List<Artigo> artigos = parseResult.getArtigosByAno(ano);
		List<Trabalho> trabalhos = parseResult.getTrabalhosByAno(ano);
		for (Artigo artigo : artigos) {
			List<Conceito> conceitos = artigo.getConceitos();
			if (!conceitos.isEmpty()) {
				String conceito = conceitos.get(0).getConceito();

				if (!GrupoConceitual.pertenceAoGrupo(conceito, conceitoFiltro)) {
					continue;
				}

				mapConceitoQuantidade.putIfAbsent(conceito, new AtomicInteger(0));
				mapConceitoQuantidade.get(conceito).incrementAndGet();
			}
		}

		for (Trabalho trabalho : trabalhos) {
			List<Conceito> conceitos = trabalho.getConceitos();
			if (!conceitos.isEmpty()) {
				String conceito = conceitos.get(0).getConceito();

				if (!GrupoConceitual.pertenceAoGrupo(conceito, conceitoFiltro)) {
					continue;
				}

				mapConceitoQuantidade.putIfAbsent(conceito, new AtomicInteger(0));
				mapConceitoQuantidade.get(conceito).incrementAndGet();
			}
		}
	}

	private void putConceitosIntoMap(ConcurrentMap<String, AtomicInteger> mapConceitoQuantidade, String conceitoFiltro) {
		List<String> conceitosFromGrupo = GrupoConceitual.getConceitosFromGrupo(conceitoFiltro);

		for (String conceito : conceitosFromGrupo) {
			mapConceitoQuantidade.putIfAbsent(conceito, new AtomicInteger(0));
		}
	}

	private void processDto(PesquisadorEstratoAnoDto pesquisadorEstratoAnoDto) {
		List<String> conceitos = new ArrayList<>();
		List<String> anos = new ArrayList<>();
		Map<Integer, ConcurrentMap<String, AtomicInteger>> mapAnosConceitos = pesquisadorEstratoAnoDto.getMapConceitoQuantidade();
		Set<Integer> keySet = mapAnosConceitos.keySet();

		for (Integer ano : keySet) {
			if (!anos.contains(ano)) {
				anos.add(ano.toString());
			}

			ConcurrentMap<String, AtomicInteger> mapConceitosQuantidade = mapAnosConceitos.get(ano);
			Set<String> keySet2 = mapConceitosQuantidade.keySet();

			for (String conceito : keySet2) {
				if (!conceitos.contains(conceito)) {
					conceitos.add(conceito);
				}
			}
		}

		Collections.sort(conceitos);
		Collections.sort(anos);

		pesquisadorEstratoAnoDto.setAnos(anos);
		pesquisadorEstratoAnoDto.setConceitos(conceitos);
	}

}
