package br.ufsc.silq.core.business.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.business.entities.DadoGeral;
import br.ufsc.silq.core.business.entities.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	List<Grupo> findAllByCoordenador(DadoGeral coordenador);

	Optional<Grupo> findOneByIdAndCoordenador(Long id, DadoGeral coordenador);
}
