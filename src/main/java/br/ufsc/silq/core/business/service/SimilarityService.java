package br.ufsc.silq.core.business.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import br.ufsc.silq.core.SilqConfig;
import br.ufsc.silq.core.business.entities.QualisPeriodico;
import br.ufsc.silq.core.business.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.utils.SilqStringUtils;
import br.ufsc.silq.core.utils.combo.ComboValueHelper;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SimilarityService {

	@Inject
	private DataSource dataSource;

	@Inject
	private QualisPeriodicoRepository qualisPeriodicoRepository;

	public void compare(ParseResult parseResult, AvaliarForm form) {
		List<Artigo> artigos = parseResult.getArtigos();
		List<Trabalho> trabalhos = parseResult.getTrabalhos();
		String conhecimento = form.getArea();
		String similarity = form.getNivelSimilaridade();
		parseResult.setNivelSimilaridade(ComboValueHelper.getNivelSimilaridadeTexto(form.getNivelSimilaridade()));

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			this.compareArtigos(similarity, artigos, conhecimento);
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			this.compareTrabalhos(similarity, trabalhos, conhecimento);
		}
	}

	private void compareArtigos(String similarity, List<Artigo> artigos, String conhecimento) {
		artigos.parallelStream().forEach((artigo) -> {
			String issn = artigo.getIssn();
			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			Optional<QualisPeriodico> singleResult = this.qualisPeriodicoRepository.findOneByIssnAndAreaAvaliacao(issn,
					conhecimento.toUpperCase());

			if (singleResult.isPresent()) {
				conceito = new Conceito();
				conceito.setConceito(singleResult.get().getEstrato());
				conceito.setNomeEvento(singleResult.get().getTitulo());
				conceito.setSimilaridade("1.0");
				conceitos.add(conceito);
			} else if (SilqStringUtils.isBlank(issn)) {
				String titulo;
				try {
					titulo = artigo.getTituloVeiculo();
					titulo = SilqStringUtils.normalizeString(titulo);

					Connection connection = this.dataSource.getConnection();

					Statement st = connection.createStatement();
					st.executeQuery("SELECT set_limit(" + similarity + "::real)");
					ResultSet rsSimilarity = st.executeQuery("SELECT NO_ESTRATO, NO_TITULO, SIMILARITY(NO_TITULO, \'"
							+ titulo + "\') AS SML FROM TB_QUALIS_PERIODICO WHERE NO_TITULO % \'" + titulo
							+ "\' AND NO_AREA_AVALIACAO LIKE \'%" + conhecimento.toUpperCase()
							+ "%\' ORDER BY SML DESC LIMIT " + SilqConfig.MAX_PARSE_RESULTS);

					while (rsSimilarity.next()) {
						conceito = new Conceito();
						conceito.setConceito(rsSimilarity.getString("NO_ESTRATO"));
						conceito.setNomeEvento(rsSimilarity.getString("NO_TITULO"));
						conceito.setSimilaridade(rsSimilarity.getFloat("SML") + "");
						conceitos.add(conceito);
					}

					rsSimilarity.close();
					st.close();
					connection.close();
				} catch (Exception e) {
					// TODO Lançar exceção ?
					log.error(e.getMessage() + "\nArtigo: " + artigo);
				}
			}
			artigo.setConceitos(conceitos);
		});
	}

	private void compareTrabalhos(String similarity, List<Trabalho> trabalhos, String conhecimento) {
		trabalhos.parallelStream().forEach(trabalho -> {
			String titulo = trabalho.getTituloVeiculo();
			titulo = SilqStringUtils.normalizeString(titulo);

			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			try {
				Connection connection = this.dataSource.getConnection();
				Statement st = connection.createStatement();
				st.executeQuery("SELECT set_limit(" + similarity + "::real)");
				ResultSet rs = st.executeQuery("SELECT NO_ESTRATO, NO_TITULO, SIMILARITY(NO_TITULO, \'" + titulo
						+ "\') AS SML FROM TB_QUALIS_EVENTO WHERE NO_TITULO % \'" + titulo
						+ "\' AND NO_AREA_AVALIACAO LIKE \'%" + conhecimento.toUpperCase()
						+ "\' ORDER BY SML DESC LIMIT " + SilqConfig.MAX_PARSE_RESULTS);

				while (rs.next()) {
					conceito = new Conceito();
					conceito.setConceito(rs.getString("NO_ESTRATO"));
					conceito.setNomeEvento(rs.getString("NO_TITULO"));
					conceito.setSimilaridade(rs.getFloat("SML") + "");
					conceitos.add(conceito);
				}
				trabalho.setConceitos(conceitos);

				rs.close();
				st.close();
				connection.close();
			} catch (Exception e) {
				// TODO Lançar exceções ou montar uma estrutura pq podem ser
				// vários
				log.error(e.getMessage() + "\nTrabalho: " + trabalho);
			}
		});
	}

}
