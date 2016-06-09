package br.ufsc.silq.core.persistence.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import br.ufsc.silq.core.persistence.entities.QualisPeriodico;

public interface QualisPeriodicoSearchRepository extends ElasticsearchRepository<QualisPeriodico, Long> {

}
