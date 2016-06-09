package br.ufsc.silq.core.persistence.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import br.ufsc.silq.core.persistence.entities.QualisEvento;

public interface QualisEventoSearchRepository extends ElasticsearchRepository<QualisEvento, Long> {

}
