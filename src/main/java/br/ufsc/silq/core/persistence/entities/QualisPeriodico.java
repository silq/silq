package br.ufsc.silq.core.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "tb_qualis_periodico")
@Getter
@Setter
public class QualisPeriodico extends Qualis {

	@Id
	@Column(name = "co_seq_periodico")
	private Long id;

	@Column(name = "co_issn")
	private String issn;

}
