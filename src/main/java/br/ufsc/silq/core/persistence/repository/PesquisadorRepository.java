package br.ufsc.silq.core.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.Pesquisador;

public interface PesquisadorRepository extends JpaRepository<Pesquisador, Long> {

	Optional<Pesquisador> findOneByIdAndGrupoId(Long id, Long grupoId);

	Optional<Pesquisador> findOneByIdCurriculoAndGrupoId(Long idCurriculo, Long grupoId);

	Optional<Pesquisador> findOneById(Long pesquisadorId);
}
