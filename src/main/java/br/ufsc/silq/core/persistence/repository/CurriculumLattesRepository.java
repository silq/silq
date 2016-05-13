package br.ufsc.silq.core.persistence.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.CurriculumLattes;

public interface CurriculumLattesRepository extends JpaRepository<CurriculumLattes, Long> {

	Optional<CurriculumLattes> findOneByIdLattesAndDataAtualizacaoCurriculo(String idLattes, Date dataAtualizacaoCurriculo);

}
