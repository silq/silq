package br.ufsc.silq.core.business.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import br.ufsc.silq.core.SilqConfig;
import br.ufsc.silq.core.business.entities.QualisGeral;
import br.ufsc.silq.core.business.repository.QualisGeralRepository;
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

	@PersistenceContext
	private EntityManager em;

	@Inject
	private DataSource dataSource;

	@Inject
	private QualisGeralRepository qualisGeralRepository;

	public void compare(ParseResult parseResult, AvaliarForm form) {
		List<Artigo> artigos = parseResult.getArtigos();
		List<Trabalho> trabalhos = parseResult.getTrabalhos();
		String conhecimento = form.getArea();
		String similarity = form.getNivelSimilaridade();
		parseResult.setNivelSimilaridade(ComboValueHelper.getNivelSimilaridadeTexto(form.getNivelSimilaridade()));

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			if (conhecimento.equalsIgnoreCase("Ciência da Computação")) {
				// TODO (bonetti): somente avaliações de trabalhos de CCO são
				// pesquisados atualmente... Permitir outras áreas!
				this.compareTrabalhos(similarity, trabalhos);
			} else {
				parseResult.setHasConceitosTrabalhos(false);
			}
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			this.compareArtigos(similarity, artigos, conhecimento);
		}

		this.em.close();
	}

	private void compareArtigos(String similarity, List<Artigo> artigos, String conhecimento) {
		for (Artigo artigo : artigos) {
			String issn = artigo.getIssn();
			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			Optional<QualisGeral> singleResult = this.qualisGeralRepository.findOneByIssnAndAreaAvaliacao(issn,
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
					titulo = artigo.getTituloPeriodico();
					titulo = SilqStringUtils.normalizeString(titulo);

					Connection connection = this.dataSource.getConnection();

					Statement st = connection.createStatement();
					st.executeQuery("SELECT set_limit(" + similarity + "::real)");
					ResultSet rsSimilarity = st.executeQuery("SELECT NO_ESTRATO, NO_TITULO, SIMILARITY(NO_TITULO, \'"
							+ titulo + "\') AS SML FROM TB_QUALIS_GERAL WHERE NO_TITULO % \'" + titulo
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
		}
	}

	private void compareTrabalhos(String similarity, List<Trabalho> trabalhos) {
		for (Trabalho trabalho : trabalhos) {
			String titulo = trabalho.getNomeEvento();
			titulo = SilqStringUtils.normalizeString(titulo);

			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			try {
				Connection connection = this.dataSource.getConnection();
				Statement st = connection.createStatement();
				st.executeQuery("SELECT set_limit(" + similarity + "::real)");
				ResultSet rs = st.executeQuery("SELECT NO_ESTRATO, NO_TITULO, SIMILARITY(NO_TITULO, \'" + titulo
						+ "\') AS SML FROM TB_QUALIS_CCO WHERE NO_TITULO % \'" + titulo + "\' ORDER BY SML DESC LIMIT "
						+ SilqConfig.MAX_PARSE_RESULTS);

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
		}
	}

}
