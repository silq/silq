package br.ufsc.silq.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.QualisPeriodico;

public interface QualisPeriodicoRepository extends JpaRepository<QualisPeriodico, Long> {

	List<QualisPeriodico> findAllByIssnAndAreaAvaliacao(String issn, String upperCase);

}
