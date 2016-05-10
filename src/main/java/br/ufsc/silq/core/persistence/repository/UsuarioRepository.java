package br.ufsc.silq.core.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.persistence.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findOneByEmail(String email);

	Optional<Usuario> findOneByResetKey(String resetKey);

}
