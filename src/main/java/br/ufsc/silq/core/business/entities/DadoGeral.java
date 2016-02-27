package br.ufsc.silq.core.business.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "Token_generator", sequenceName = "sq_dado_geral", allocationSize = 1, initialValue = 1)
@Table(name = "tb_dado_geral")
@Data
@ToString(exclude = { "curriculoXml", "grupos" })
@NoArgsConstructor
public class DadoGeral {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
	@Column(name = "co_seq_dado_geral")
	private Long id;

	@Column(name = "no_completo")
	private String nome;

	@Column(name = "no_especialidade")
	private String especialidade;

	@Column(name = "no_sub_area_conhecimento")
	private String subAreaConhecimento;

	@Column(name = "no_area_conhecimento")
	private String areaConhecimento;

	@Column(name = "no_grande_area_conhecimento")
	private String grandeAreaConhecimento;

	@Column(name = "id_curriculo")
	private String idCurriculo;

	@Column(name = "data_atualizacao_curriculo")
	private Date dataAtualizacaoCurriculo;

	@Column(name = "data_atualizacao_usuario")
	private Date dataAtualizacaoUsuario;

	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "xml")
	@JsonIgnore
	private byte[] curriculoXml;

	@OneToOne
	@JoinColumn(name = "co_usuario", unique = true, nullable = false)
	@JsonIgnore
	private Usuario usuario;

	@OneToMany(mappedBy = "coordenador", orphanRemoval = true)
	@JsonIgnore
	private List<Grupo> grupos;

}
