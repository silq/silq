package br.ufsc.silq.core.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "Token_generator", sequenceName = "sq_curriculum_lattes", allocationSize = 1, initialValue = 1)
@Table(name = "tb_curriculum_lattes")
@Data
@ToString(of = { "id", "idLattes", "nome", "dataAtualizacaoCurriculo" })
@EqualsAndHashCode(of = { "id" })
@NoArgsConstructor
public class CurriculumLattes {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
	@Column(name = "co_seq_curriculum")
	private Long id;

	@Column(name = "id_lattes")
	private String idLattes;

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

	@Column(name = "data_atualizacao_curriculo")
	private Date dataAtualizacaoCurriculo;

	@Column(name = "data_atualizacao_usuario")
	private Date dataAtualizacaoUsuario;

	@JsonIgnore
	@Type(type = "br.ufsc.silq.core.persistence.types.XMLType")
	@Column(name = "lattes_xml")
	private String lattesXml;
}
