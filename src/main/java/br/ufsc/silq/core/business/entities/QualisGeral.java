package br.ufsc.silq.core.business.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_qualis_geral")
@Getter
@Setter
public class QualisGeral {

	@Column(name = "no_area_avaliacao")
	private String areaAvaliacao;

	@Column(name = "no_estrato")
	private String estrato;

	@Id
	@Column(name = "co_seq_qualis_geral")
	private Long id;

	@Column(name = "co_issn")
	private String issn;

	@Column(name = "st_qualis")
	private String stQualis;

	@Column(name = "no_titulo")
	private String titulo;
}
