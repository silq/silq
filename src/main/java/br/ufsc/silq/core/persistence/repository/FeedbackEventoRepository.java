package br.ufsc.silq.core.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.FeedbackEvento;
import br.ufsc.silq.core.persistence.entities.Usuario;

public interface FeedbackEventoRepository extends JpaRepository<FeedbackEvento, Long> {

	Optional<FeedbackEvento> findOneByQueryAndUsuarioAndValidation(String query, Usuario usuario, Boolean validation);

	Long deleteByQueryAndUsuario(String query, Usuario usuario);

	List<FeedbackEvento> findAllByUsuarioAndValidation(Usuario usuario, Boolean validation);
}
