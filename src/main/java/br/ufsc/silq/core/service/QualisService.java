package br.ufsc.silq.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.core.data.NivelSimilaridade;
import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.core.persistence.repository.QualisEventoRepository;
import br.ufsc.silq.core.persistence.repository.QualisPeriodicoRepository;
import br.ufsc.silq.core.persistence.repository.search.QualisEventoSearchRepository;
import br.ufsc.silq.core.persistence.repository.search.QualisPeriodicoSearchRepository;
import br.ufsc.silq.core.service.AvaliacaoService.TipoAvaliacao;
import br.ufsc.silq.core.utils.SilqStringUtils;

@Service
@Transactional(readOnly = true)
public class QualisService {

	@Inject
	private ElasticsearchTemplate elasticsearchTemplate;

	@Inject
	private QualisPeriodicoRepository periodicoRepository;

	@Inject
	private QualisPeriodicoSearchRepository periodicoSearchRepository;

	@Inject
	private QualisEventoRepository eventoRepository;

	@Inject
	private QualisEventoSearchRepository eventoSearchRepository;

	@Inject
	private AvaliacaoService avaliacaoService;

	@PersistenceContext
	private EntityManager em;

	/**
	 * Pesquisa por peridócios na base Qualis do sistema que sejam similares à query.
	 *
	 * @param query Query a ser utilizada para busca.
	 * @param pageable Configuração de paginação.
	 * @return Uma página (sublista) dos resultados encontrados.
	 */
	public PageImpl<SimilarityResult<QualisPeriodico>> searchPeriodicos(String query, Pageable pageable) {
		List<Object[]> results = this.nativeSearch(TipoAvaliacao.PERIODICO, query, pageable);
		List<SimilarityResult<QualisPeriodico>> periodicos = results.stream()
				.map(this::mapResultToPeriodico)
				.collect(Collectors.toList());
		return new PageImpl<>(periodicos, pageable, 0);
	}

	/**
	 * Pesquisa por eventos na base Qualis do sistema que sejam similares à query.
	 *
	 * @param query Query a ser utilizada para busca.
	 * @param pageable Configuração de paginação.
	 * @return Uma página (sublista) dos resultados encontrados.
	 */
	public PageImpl<SimilarityResult<QualisEvento>> searchEventos(String query, Pageable pageable) {
		List<Object[]> results = this.nativeSearch(TipoAvaliacao.EVENTO, query, pageable);
		List<SimilarityResult<QualisEvento>> eventos = results.stream()
				.map(this::mapResultToEvento)
				.collect(Collectors.toList());
		return new PageImpl<>(eventos, pageable, 0);
	}

	private List<Object[]> nativeSearch(TipoAvaliacao tipo, String query, Pageable pageable) {
		this.avaliacaoService.setSimilarityThreshold(0.1F);

		String sql = "SELECT *, SIMILARITY(NO_TITULO, ?1) AS SML";
		sql += " FROM " + tipo.getTable() + " WHERE NO_TITULO % ?1";
		sql += " ORDER BY SML DESC";
		sql += " LIMIT ?2";
		sql += " OFFSET ?3";

		Query q = this.em.createNativeQuery(sql);
		q.setParameter(1, SilqStringUtils.normalizeString(query));
		q.setParameter(2, pageable.getPageSize());
		q.setParameter(3, pageable.getOffset());

		return q.getResultList();
	}

	private SimilarityResult<QualisEvento> mapResultToEvento(Object[] result) {
		QualisEvento evento = new QualisEvento();
		evento.setId(((BigDecimal) result[0]).longValue());
		evento.setSigla((String) result[1]);
		evento.setTitulo((String) result[2]);
		evento.setIndiceH(((BigDecimal) result[3]).intValue());
		evento.setEstrato((String) result[4]);
		evento.setAreaAvaliacao((String) result[5]);
		evento.setAno((Integer) result[6]);
		return new SimilarityResult<>(evento, new NivelSimilaridade((Float) result[7]));
	}

	private SimilarityResult<QualisPeriodico> mapResultToPeriodico(Object[] result) {
		QualisPeriodico periodico = new QualisPeriodico();
		periodico.setId(((BigDecimal) result[0]).longValue());
		periodico.setIssn((String) result[1]);
		periodico.setTitulo((String) result[2]);
		periodico.setEstrato((String) result[3]);
		periodico.setAreaAvaliacao((String) result[4]);
		periodico.setAno((Integer) result[5]);
		return new SimilarityResult<>(periodico, new NivelSimilaridade((Float) result[6]));
	}
}
