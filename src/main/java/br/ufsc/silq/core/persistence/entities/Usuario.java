package br.ufsc.silq.core.persistence.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "Token_generator", sequenceName = "sq_usuario", allocationSize = 1, initialValue = 1)
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@ToString(of = { "id", "nome" })
@EqualsAndHashCode(of = { "id" })
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
	@Column(name = "co_seq_usuario")
	private Long id;

	@Column(name = "no_usuario")
	private String nome;

	@Column(name = "ds_senha")
	@JsonIgnore
	private String senha;

	@Column(name = "ds_email")
	private String email;

	@Column(name = "reset_key", length = 20)
	@JsonIgnore
	private String resetKey;

	@Column(name = "register_key", length = 20)
	@JsonIgnore
	private String registerKey;

	@Column(name = "st_ativo")
	@JsonIgnore
	private Boolean ativo;

	@ManyToOne
	@JoinColumn(name = "co_curriculum")
	@JsonIgnore
	private CurriculumLattes curriculum;

	@JsonIgnore
	@OneToMany(mappedBy = "usuarioId", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Autoridade> autoridades = new HashSet<>();

	@OneToMany(mappedBy = "coordenador", orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Grupo> grupos = new ArrayList<>();

    @OneToMany(mappedBy = "espectadores", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Grupo> gruposEspectadores = new ArrayList<>();
}
