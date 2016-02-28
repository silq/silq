package br.ufsc.silq.core.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.business.entities.QualisGeral;

public interface QualisGeralRepository extends JpaRepository<QualisGeral, Long> {

	Optional<QualisGeral> findOneByIssnAndAreaAvaliacao(String issn, String upperCase);

}
