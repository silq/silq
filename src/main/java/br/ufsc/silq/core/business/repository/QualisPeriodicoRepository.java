package br.ufsc.silq.core.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.business.entities.QualisPeriodico;

public interface QualisPeriodicoRepository extends JpaRepository<QualisPeriodico, Long> {

	Optional<QualisPeriodico> findOneByIssnAndAreaAvaliacao(String issn, String upperCase);

}
