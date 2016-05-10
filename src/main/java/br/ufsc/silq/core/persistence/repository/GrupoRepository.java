package br.ufsc.silq.core.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.Grupo;
import br.ufsc.silq.core.persistence.entities.Usuario;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	List<Grupo> findAllByCoordenador(Usuario coordenador);

	Optional<Grupo> findOneByIdAndCoordenador(Long id, Usuario coordenador);
}
