package br.ufsc.silq.core.business.entities;

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
@Table(name = "tb_qualis_evento")
@Getter
@Setter
public class QualisEvento {

	@Id
	@Column(name = "co_seq_qualis_cco")
	private Long id;

	@Column(name = "ds_sigla")
	private String sigla;

	@Column(name = "no_titulo")
	private String titulo;

	@Column(name = "nu_indice_h")
	private Integer indiceH;

	@Column(name = "no_estrato")
	private String estrato;

	@Column(name = "no_area_avaliacao")
	private String areaAvaliacao;
}
