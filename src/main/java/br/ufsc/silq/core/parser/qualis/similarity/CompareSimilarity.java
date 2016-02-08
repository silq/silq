package br.ufsc.silq.core.parser.qualis.similarity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;

import br.ufsc.silq.core.SilqConfig;
import br.ufsc.silq.core.business.entities.QQualisGeral;
import br.ufsc.silq.core.business.entities.QualisGeral;
import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.utils.SilqStringUtils;
import br.ufsc.silq.core.utils.combo.ComboValueHelper;

@Service
public class CompareSimilarity {

	@PersistenceContext
	private EntityManager em;

	public void compare(ParseResult parseResult, AvaliarForm form, AvaliacaoType tipoAvaliacao) {
		List<Artigo> artigos = parseResult.getArtigos();
		List<Trabalho> trabalhos = parseResult.getTrabalhos();
		String conhecimento = form.getArea();
		String similarity = form.getNivelSimilaridade();
		parseResult.setNivelSimilaridade(ComboValueHelper.getNivelSimilaridadeTexto(form.getNivelSimilaridade()));

		if (tipoAvaliacao.equals(AvaliacaoType.TRABALHO) || tipoAvaliacao.equals(AvaliacaoType.AMBOS)) {
			if (conhecimento.equalsIgnoreCase("Ciência da Computação")) {
				this.compareTrabalhos(similarity, this.em, trabalhos);
			} else {
				parseResult.setHasConceitosTrabalhos(false);
			}
		}

		if (tipoAvaliacao.equals(AvaliacaoType.ARTIGO) || tipoAvaliacao.equals(AvaliacaoType.AMBOS)) {
			this.compareArtigos(similarity, this.em, artigos, conhecimento);
		}

		this.em.close();
	}

	private void compareArtigos(String similarity, EntityManager em, List<Artigo> artigos, String conhecimento) {
		for (Artigo artigo : artigos) {
			String issn = artigo.getIssn();
			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			QQualisGeral qQualisGeral = QQualisGeral.qualisGeral;
			JPAQuery queryJPA = new JPAQuery(em);
			queryJPA.from(qQualisGeral)
					.where(qQualisGeral.issn.eq(issn).and(qQualisGeral.areaAvaliacao.eq(conhecimento.toUpperCase())));

			QualisGeral singleResult = queryJPA.singleResult(qQualisGeral);
			if (singleResult != null) {
				conceito = new Conceito();
				conceito.setConceito(singleResult.getEstrato());
				conceito.setNomeEvento(singleResult.getTitulo());
				conceito.setSimilaridade("1.0");
				conceitos.add(conceito);
			} else if (SilqStringUtils.isBlank(issn)) {
				String titulo;
				try {
					titulo = artigo.getTituloPeriodico();
					titulo = SilqStringUtils.normalizeString(titulo);

					Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/silq",
							"postgres", "mingaudeaveia");
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
					// TODO Lançar exceção;
					System.out.println(e.getMessage() + "\n" + artigo);
				}
			}
			artigo.setConceitos(conceitos);
		}
	}

	private void compareTrabalhos(String similarity, EntityManager em, List<Trabalho> trabalhos) {
		for (Trabalho trabalho : trabalhos) {
			String titulo = trabalho.getNomeEvento();
			titulo = SilqStringUtils.normalizeString(titulo);

			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			try {
				Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/silq", "postgres",
						"mingaudeaveia");
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
				// vários;
				System.out.println(e.getMessage() + "\n" + e.getMessage());
			}
		}
	}

}
