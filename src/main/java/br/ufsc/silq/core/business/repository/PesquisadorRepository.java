package br.ufsc.silq.core.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufsc.silq.core.business.entities.Pesquisador;

public interface PesquisadorRepository extends JpaRepository<Pesquisador, Long> {

}
