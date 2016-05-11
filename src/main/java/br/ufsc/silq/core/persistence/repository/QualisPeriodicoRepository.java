package br.ufsc.silq.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import br.ufsc.silq.core.persistence.entities.QualisPeriodico;

public interface QualisPeriodicoRepository extends JpaRepository<QualisPeriodico, Long>, QueryDslPredicateExecutor<QualisPeriodico> {

}
