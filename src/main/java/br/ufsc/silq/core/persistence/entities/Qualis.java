package br.ufsc.silq.core.persistence.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Qualis {

	@Column(name = "no_titulo")
	protected String titulo;

	@Column(name = "nu_ano")
	protected Integer ano;

	@Column(name = "no_estrato")
	protected String estrato;

	@Column(name = "no_area_avaliacao")
	protected String areaAvaliacao;

	public abstract Long getId();
}
