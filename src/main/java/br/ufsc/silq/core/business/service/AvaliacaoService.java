package br.ufsc.silq.core.business.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import br.ufsc.silq.core.SilqConfig;
import br.ufsc.silq.core.business.entities.QualisPeriodico;
import br.ufsc.silq.core.business.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.data.AvaliacaoResult;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.enums.AvaliacaoType;
import br.ufsc.silq.core.exception.SilqError;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;
import br.ufsc.silq.core.parser.dto.Artigo;
import br.ufsc.silq.core.parser.dto.Conceito;
import br.ufsc.silq.core.parser.dto.ParseResult;
import br.ufsc.silq.core.parser.dto.Trabalho;
import br.ufsc.silq.core.utils.SilqStringUtils;

@Service
public class AvaliacaoService {

	@Inject
	private DataSource dataSource;

	@Inject
	private QualisPeriodicoRepository qualisPeriodicoRepository;

	@Inject
	private LattesParser lattesParser;

	/**
	 * Extrai dados do currículo Lattes utilizando {@link LattesParser} e avalia-o.
	 *
	 * @param lattes XML do currículo Lattes do pesquisador a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqLattesException
	 */
	public AvaliacaoResult avaliar(Document lattes, @Valid AvaliarForm avaliarForm) throws SilqLattesException {
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		return this.avaliar(parseResult, avaliarForm);
	}

	/**
	 * Extrai dados do currículo Lattes utilizando {@link LattesParser} e avalia-o.
	 *
	 * @param lattes Byte array do XML do currículo Lattes do pesquisador a ser avaliado.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqLattesException
	 */
	public AvaliacaoResult avaliar(byte[] lattes, @Valid AvaliarForm avaliarForm) throws SilqLattesException {
		ParseResult parseResult = this.lattesParser.parseCurriculum(lattes);
		return this.avaliar(parseResult, avaliarForm);
	}

	/**
	 * Avalia dados de um currículo Lattes já parseados pelo {@link LattesParser}.
	 *
	 * @param parseResult Resultado do parsing do currículo Lattes de um pesquisador.
	 * @param avaliarForm Formulário contendo as opções de avaliação.
	 * @return Um {@link AvaliacaoResult} contendo os resultados de avaliação.
	 * @throws SilqLattesException
	 */
	public AvaliacaoResult avaliar(ParseResult parseResult, AvaliarForm form) {
		AvaliacaoResult result = new AvaliacaoResult(form, parseResult.getDadosGerais());

		if (form.getTipoAvaliacao().includes(AvaliacaoType.ARTIGO)) {
			parseResult.getArtigos().parallelStream().forEach((artigo) -> {
				if (!form.periodoInclui(artigo.getAno())) {
					// Não avalia artigo que não pertence ao período de avaliação informado.
					return;
				}

				result.getArtigos().add(this.avaliarArtigo(artigo, form));
			});
		}

		if (form.getTipoAvaliacao().includes(AvaliacaoType.TRABALHO)) {
			parseResult.getTrabalhos().parallelStream().forEach((trabalho) -> {
				if (!form.periodoInclui(trabalho.getAno())) {
					// Não avalia trabalho que não pertence ao período de avaliação informado.
					return;
				}

				result.getTrabalhos().add(this.avaliarTrabalho(trabalho, form));
			});
		}

		result.order();
		return result;
	}

	protected Artigo avaliarArtigo(Artigo artigo, AvaliarForm avaliarForm) {
		String issn = artigo.getIssn();
		List<Conceito> conceitos = new ArrayList<>();
		Optional<QualisPeriodico> singleResult = this.qualisPeriodicoRepository.findOneByIssnAndAreaAvaliacao(issn,
				avaliarForm.getArea().toUpperCase());

		if (singleResult.isPresent()) {
			QualisPeriodico periodico = singleResult.get();
			conceitos.add(new Conceito(periodico.getTitulo(), periodico.getEstrato(), NivelSimilaridade.TOTAL, periodico.getAno()));
		} else if (SilqStringUtils.isBlank(issn)) {
			String tituloVeiculo;
			try {
				tituloVeiculo = artigo.getTituloVeiculo();
				tituloVeiculo = SilqStringUtils.normalizeString(tituloVeiculo);

				Connection connection = this.dataSource.getConnection();

				Statement st = connection.createStatement();
				st.executeQuery("SELECT set_limit(" + avaliarForm.getNivelSimilaridade().getValue() + "::real)");
				ResultSet rs = st.executeQuery(
						this.createSqlStatement("TB_QUALIS_PERIODICO", tituloVeiculo, avaliarForm.getArea(), SilqConfig.MAX_PARSE_RESULTS));

				while (rs.next()) {
					conceitos.add(this.createConceito(rs));
				}

				rs.close();
				st.close();
				connection.close();
			} catch (Exception e) {
				throw new SilqError("Erro ao avaliar artigo: " + artigo.getTitulo(), e);
			}
		}
		artigo.setConceitos(conceitos);
		return artigo;
	}

	protected Trabalho avaliarTrabalho(Trabalho trabalho, AvaliarForm avaliarForm) {
		String tituloVeiculo = trabalho.getTituloVeiculo();
		tituloVeiculo = SilqStringUtils.normalizeString(tituloVeiculo);

		List<Conceito> conceitos = new ArrayList<>();

		try {
			Connection connection = this.dataSource.getConnection();
			Statement st = connection.createStatement();
			st.executeQuery("SELECT set_limit(" + avaliarForm.getNivelSimilaridade().getValue() + "::real)");
			ResultSet rs = st.executeQuery(
					this.createSqlStatement("TB_QUALIS_EVENTO", tituloVeiculo, avaliarForm.getArea(), SilqConfig.MAX_PARSE_RESULTS));

			while (rs.next()) {
				conceitos.add(this.createConceito(rs));
			}
			trabalho.setConceitos(conceitos);

			rs.close();
			st.close();
			connection.close();
		} catch (Exception e) {
			throw new SilqError("Erro ao avaliar trabalho: " + trabalho.getTitulo(), e);
		}

		return trabalho;
	}

	/**
	 * Cria um conceito a partir de um ResultSet.
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected Conceito createConceito(ResultSet rs) throws SQLException {
		return new Conceito(rs.getString("NO_TITULO"), rs.getString("NO_ESTRATO"),
				new NivelSimilaridade(rs.getFloat("SML")), rs.getInt("NU_ANO"));
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
	protected String createSqlStatement(String table, String tituloVeiculo, String area, int limit) {
		String sql = "";
		sql += "SELECT *, SIMILARITY(NO_TITULO, \'" + tituloVeiculo + "\') AS SML";
		sql += " FROM " + table + " WHERE NO_TITULO % \'" + tituloVeiculo + "\'";
		sql += " AND NO_AREA_AVALIACAO LIKE \'%" + area.toUpperCase() + "\'";
		sql += " ORDER BY SML DESC LIMIT " + limit;
		return sql;
	}

}
