package br.ufsc.silq.core.persistence.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class Qualis {

	@Column(name = "no_titulo")
	private String titulo;

	@Column(name = "nu_ano")
	private Integer ano;

	@Column(name = "no_estrato")
	private String estrato;

	@Column(name = "no_area_avaliacao")
	private String areaAvaliacao;

	public abstract Long getId();
}
