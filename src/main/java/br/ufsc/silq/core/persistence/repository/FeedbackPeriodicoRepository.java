package br.ufsc.silq.core.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.FeedbackPeriodico;
import br.ufsc.silq.core.persistence.entities.Usuario;

public interface FeedbackPeriodicoRepository extends JpaRepository<FeedbackPeriodico, Long> {

	Optional<FeedbackPeriodico> findOneByQueryAndUsuario(String query, Usuario usuario);

	Long deleteByQueryAndUsuario(String query, Usuario usuario);
}
