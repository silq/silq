package br.ufsc.silq.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_qualis_cco")
@Getter
@Setter
public class QualisCCO {

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
}
