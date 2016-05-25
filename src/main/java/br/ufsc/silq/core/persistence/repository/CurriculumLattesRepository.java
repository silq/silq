package br.ufsc.silq.core.persistence.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ufsc.silq.core.persistence.entities.CurriculumLattes;

public interface CurriculumLattesRepository extends JpaRepository<CurriculumLattes, Long> {

	Optional<CurriculumLattes> findOneByIdLattesAndDataAtualizacaoCurriculo(String idLattes, Date dataAtualizacaoCurriculo);

	/**
	 * Procura pelos currículos não utilizados no banco, ou seja, que não estão relacionados a um usuário ou
	 * pesquisador de um grupo.
	 *
	 * @return Lista de IDs dos currículos em desuso.
	 */
	@Query(value = "SELECT l.co_seq_curriculum FROM tb_curriculum_lattes l EXCEPT "
			+ "(SELECT u.co_curriculum FROM tb_usuario u UNION SELECT p.co_curriculum FROM rl_grupo_pesquisador p)", nativeQuery = true)
	List<BigDecimal> findAllEmDesuso();
}
