package br.ufsc.silq.core.service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.data.Conceito;
import br.ufsc.silq.core.data.Conceituavel;
import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.data.TipoConceito;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.forms.QualisSearchForm;
import br.ufsc.silq.core.persistence.entities.Feedback;
import br.ufsc.silq.core.persistence.entities.Qualis;
import br.ufsc.silq.core.persistence.entities.Usuario;
import br.ufsc.silq.core.utils.SilqStringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço de similaridade para avaliação e consulta dos dados Qualis.
 * Encapsula as consultas nativas do PostgreSQL que realizam busca por similaridade.
 */
@Service
@Transactional
@Slf4j
public class SimilarityService {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Seta o nível de similaridade mínimo (threshold) que será usado para as queries de similaridade no banco.
	 * Utiliza a função nativa do PostgreSQL `set_limit()`.
	 *
	 * @param value Valor numérico de 0 a 1 representando o threshold de similaridade.
	 */
	public void setSimilarityThreshold(Float value) {
		Query query = this.getCurrentSession().createSQLQuery("SELECT set_limit(:limit)");
		query.setParameter("limit", value);
		query.uniqueResult();
	}

	private Session getCurrentSession() {
		return this.em.unwrap(Session.class);
	}

	/**
	 * Obtém os conceitos de um evento ou periódico realizando uma busca por similaridade na base Qualis.
	 * Utiliza as funções de similaridade nativas do PostgreSQL.
	 *
	 * @param clazz Tipo do registro qualis a ser pesquisado. Deve extender {@link Qualis}.
	 * @param conceituavel Trabalho ou artigo a ser avaliado.
	 * @param avaliarForm Opções de avaliação.
	 * @return A lista de conceitos do veículo.
	 */
	public <T extends Qualis> List<Conceito> getConceitos(Class<T> clazz, Conceituavel conceituavel, @Valid AvaliarForm avaliarForm) {
		this.setSimilarityThreshold(avaliarForm.getNivelSimilaridade().getValue());

		String sql = "SELECT *, SIMILARITY(NO_TITULO, :query) AS SML";
		sql += " FROM " + clazz.getAnnotation(Table.class).name();
		sql += " WHERE NO_TITULO % :query";
		if (avaliarForm.hasArea()) {
			sql += " AND NO_AREA_AVALIACAO = :area";
		}
		sql += " ORDER BY SML DESC, ABS(NU_ANO - :ano) ASC";
		sql += " LIMIT :limit";

		Query q = this.getCurrentSession().createSQLQuery(sql)
				.addEntity(clazz)
				.addScalar("sml")
				.setString("query", SilqStringUtils.normalizeString(conceituavel.getTituloVeiculo()))
				.setInteger("ano", conceituavel.getAno())
				.setInteger("limit", avaliarForm.getMaxConceitos());

		if (avaliarForm.hasArea()) {
			q.setString("area", avaliarForm.getArea().toUpperCase());
		}

		List<Object[]> tuples = q.list();

		return tuples.stream()
				.map(r -> new Conceito((T) r[0], new NivelSimilaridade((float) r[1]), TipoConceito.SIMILARIDADE_TEXTUAL))
				.collect(Collectors.toList());
	}

	/**
	 * Realiza uma busca nos dados Qualis dado uma query qualquer.
	 * Utiliza as funções de similaridade nativas do PostgreSQL.
	 *
	 * @param clazz Tipo do registro qualis a ser pesquisado. Deve extender {@link Qualis}.
	 * @param form Form contendo a query e os filtros de busca.
	 * @param pageable Configurações de paginação.
	 * @return Uma página dos resultados encontrados.
	 */
	public <T extends Qualis> Page<SimilarityResult<T>> searchQualis(Class<T> clazz, @Valid QualisSearchForm form, Pageable pageable) {
		this.setSimilarityThreshold(0.1F);

		String sql = "SELECT *, SIMILARITY(NO_TITULO, :query) AS sml, COUNT(*) OVER() AS total";
		sql += " FROM " + clazz.getAnnotation(Table.class).name();
		sql += " WHERE NO_TITULO % :query";

		if (StringUtils.isNotBlank(form.getArea())) {
			sql += " AND NO_AREA_AVALIACAO = :area";
		}

		sql += " ORDER BY SML DESC";
		sql += " LIMIT :limit";
		sql += " OFFSET :offset";

		Query q = this.getCurrentSession().createSQLQuery(sql)
				.addEntity(clazz)
				.addScalar("sml")
				.addScalar("total")
				.setString("query", SilqStringUtils.normalizeString(form.getQuery()))
				.setInteger("limit", pageable.getPageSize())
				.setInteger("offset", pageable.getOffset());

		if (StringUtils.isNotBlank(form.getArea())) {
			q.setString("area", form.getArea().toUpperCase());
		}

		List<Object[]> tuples = q.list();
		BigInteger total = tuples.isEmpty() ? BigInteger.valueOf(0) : (BigInteger) tuples.get(0)[2];

		List<SimilarityResult<T>> results = tuples.stream()
				.map(r -> new SimilarityResult<>((T) r[0], new NivelSimilaridade((float) r[1])))
				.collect(Collectors.toList());
		return new PageImpl<>(results, pageable, total.intValue());
	}

	/**
	 * Pesquisa por feedbacks semelhantes à query e pertencentes ao usuário.
	 *
	 * @param clazz Tipo do Feedback a ser retornado (deve extender {@link Feedback}}.
	 * @param query Query a ser pesquisada.
	 * @param usuario Usuário que fez o feedback. Só serão pesquisados feedbacks deste usuário.
	 * @param threshold Treshold da pesquisa.
	 * @return O feedback mais similar à query, ou {@code null} caso não encontrado.
	 */
	public <T extends Feedback> SimilarityResult<T> searchFeedback(Class<T> clazz, String query, Usuario usuario, NivelSimilaridade threshold) {
		this.setSimilarityThreshold(threshold.getValue());

		String sql = "";
		sql += "SELECT *, SIMILARITY(ds_query, :query) AS sml FROM tb_feedback";
		sql += " WHERE co_usuario = :usuarioId";
		sql += " AND st_validation = false";
		sql += " AND co_tipo = :discriminator";
		sql += " AND ds_query % :query";
		sql += " ORDER BY sml DESC";
		sql += " LIMIT 1";

		Query q = this.getCurrentSession().createSQLQuery(sql)
				.addEntity(clazz)
				.addScalar("sml")
				.setString("query", query)
				.setLong("usuarioId", usuario.getId())
				.setString("discriminator", clazz.getAnnotation(DiscriminatorValue.class).value());
		List<Object[]> results = q.list();

		if (results.isEmpty()) {
			return null;
		}

		Object[] first = results.get(0);
		return new SimilarityResult<>((T) first[0], new NivelSimilaridade((float) first[1]));
	}

	/**
	 * Calcula a similaridade textual entre duas strings.
	 *
	 * @param s1 Primeira string.
	 * @param s2 Segunda string.
	 * @return O {@link NivelSimilaridade} calculado entre as duas strings.
	 */
	public NivelSimilaridade calcularSimilaridade(String s1, String s2) {
		SQLQuery q = this.getCurrentSession().createSQLQuery("SELECT similarity(:s1, :s2)");
		q.setParameter("s1", s1);
		q.setParameter("s2", s2);
		Float value = (Float) q.uniqueResult();
		return new NivelSimilaridade(value);
	}
}
