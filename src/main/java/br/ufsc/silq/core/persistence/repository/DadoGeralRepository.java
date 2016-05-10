package br.ufsc.silq.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.DadoGeral;
import br.ufsc.silq.core.persistence.entities.Usuario;

public interface DadoGeralRepository extends JpaRepository<DadoGeral, Long> {

	DadoGeral findByUsuario(Usuario usuario);

	void deleteByUsuario(Usuario usuario);
}
