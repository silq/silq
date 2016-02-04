package br.ufsc.silq.core.business.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "Token_generator", sequenceName = "sq_pesquisador", allocationSize = 1, initialValue = 1)
@Table(name = "tb_pesquisador")
@NoArgsConstructor
public class Pesquisador {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
	@Column(name = "co_seq_pesquisador")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "co_grupo")
	private Grupo grupo;

	@Column(name = "nome_pesquisador")
	private String nome;

	@Column(name = "id_curriculo")
	private Long idCurriculo;

	@Column(name = "dt_atualizacao_curriculo")
	private Date dataAtualizacaoCurriculo;

	@Column(name = "dt_atualizacao_usuario")
	private Date dataAtualizacaoUsuario;

	@Column(name = "ds_area_atuacao")
	private String areaAtuacao;

	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "xml")
	private byte[] curriculoXml;
}
