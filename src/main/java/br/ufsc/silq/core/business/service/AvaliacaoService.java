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
import br.ufsc.silq.core.exception.SilqError;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.utils.SilqStringUtils;
import br.ufsc.silq.core.utils.combo.ComboValueHelper;

@Service
public class AvaliacaoService {

	@Inject
	private DataSource dataSource;

	@Inject
	private QualisPeriodicoRepository qualisPeriodicoRepository;

	public void avaliar(ParseResult parseResult, AvaliarForm form) {
		List<Artigo> artigos = parseResult.getArtigos();
		List<Trabalho> trabalhos = parseResult.getTrabalhos();
		parseResult.setNivelSimilaridade(ComboValueHelper.getNivelSimilaridadeTexto(form.getNivelSimilaridade()));

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			this.avaliarArtigos(artigos, form);
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			this.avaliarTrabalhos(trabalhos, form);
		}
	}

	private void avaliarArtigos(List<Artigo> artigos, AvaliarForm avaliarForm) {
		artigos.parallelStream().forEach((artigo) -> {
			if (!avaliarForm.periodoInclui(artigo.getAno())) {
				// Não realiza avaliação do artigo caso não seja do período informado
				return;
			}

			String issn = artigo.getIssn();
			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			Optional<QualisPeriodico> singleResult = this.qualisPeriodicoRepository.findOneByIssnAndAreaAvaliacao(issn,
					avaliarForm.getArea().toUpperCase());

			if (singleResult.isPresent()) {
				conceito = new Conceito();
				conceito.setConceito(singleResult.get().getEstrato());
				conceito.setNomeEvento(singleResult.get().getTitulo());
				conceito.setSimilaridade("1.0");
				conceitos.add(conceito);
			} else if (SilqStringUtils.isBlank(issn)) {
				String tituloVeiculo;
				try {
					tituloVeiculo = artigo.getTituloVeiculo();
					tituloVeiculo = SilqStringUtils.normalizeString(tituloVeiculo);

					Connection connection = this.dataSource.getConnection();

					Statement st = connection.createStatement();
					st.executeQuery("SELECT set_limit(" + avaliarForm.getNivelSimilaridade() + "::real)");
					ResultSet rs = st.executeQuery(
							this.createSqlStatement("TB_QUALIS_PERIODICO", tituloVeiculo, avaliarForm.getArea(), SilqConfig.MAX_PARSE_RESULTS));

					while (rs.next()) {
						conceito = new Conceito();
						conceito.setConceito(rs.getString("NO_ESTRATO"));
						conceito.setNomeEvento(rs.getString("NO_TITULO"));
						conceito.setSimilaridade(rs.getFloat("SML") + "");
						conceitos.add(conceito);
					}

					rs.close();
					st.close();
					connection.close();
				} catch (Exception e) {
					throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
				}
			}
			artigo.setConceitos(conceitos);
		});
	}

	private void avaliarTrabalhos(List<Trabalho> trabalhos, AvaliarForm avaliarForm) {
		trabalhos.parallelStream().forEach(trabalho -> {
			if (!avaliarForm.periodoInclui(trabalho.getAno())) {
				// Não realiza avaliação do trabalho caso não seja do período informado
				return;
			}

			String tituloVeiculo = trabalho.getTituloVeiculo();
			tituloVeiculo = SilqStringUtils.normalizeString(tituloVeiculo);

			List<Conceito> conceitos = new ArrayList<>();
			Conceito conceito;

			try {
				Connection connection = this.dataSource.getConnection();
				Statement st = connection.createStatement();
				st.executeQuery("SELECT set_limit(" + avaliarForm.getNivelSimilaridade() + "::real)");
				ResultSet rs = st.executeQuery(
						this.createSqlStatement("TB_QUALIS_EVENTO", tituloVeiculo, avaliarForm.getArea(), SilqConfig.MAX_PARSE_RESULTS));

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
				throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
			}
		});
	}

	/**
	 * Cria uma instrução SQL (Postgres) que, ao executada, retorna os veículos mais similares ao artigo ou trabalho parâmetro.
	 *
	 * @param table Tabela a ser utilizada na avaliação.
	 * @param tituloVeiculo Título do veículo onde o artigo ou trabalho foi publicado/apresentado.
	 * @param area Área de avaliação a ser utilizada.
	 * @param limit Número máximo de registros similares a serem retornados.
	 * @return Uma instrução SQL que utiliza a função de similaridade do PostgreSQL.
	 */
	private String createSqlStatement(String table, String tituloVeiculo, String area, int limit) {
		String sql = "";
		sql += "SELECT NO_ESTRATO, NO_TITULO, SIMILARITY(NO_TITULO, \'" + tituloVeiculo + "\') AS SML";
		sql += " FROM " + table + " WHERE NO_TITULO % \'" + tituloVeiculo + "\'";
		sql += " AND NO_AREA_AVALIACAO LIKE \'%" + area.toUpperCase() + "\'";
		sql += " ORDER BY SML DESC LIMIT " + limit;
		return sql;
	}

}
