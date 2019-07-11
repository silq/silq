package br.ufsc.silq.core.persistence.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "Token_generator", sequenceName = "sq_grupo", allocationSize = 1, initialValue = 1)
@Table(name = "tb_grupo")
@Data
@NoArgsConstructor
public class Grupo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
	@Column(name = "co_seq_grupo")
	private Long id;

	@Column(name = "no_grupo")
	private String nomeGrupo;

	@Column(name = "no_instituicao")
	private String nomeInstituicao;

	@Column(name = "no_area")
	private String nomeArea;

	@ManyToOne
	@JoinColumn(name = "co_usuario")
	@JsonIgnore
	private Usuario coordenador;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "rl_grupo_pesquisador", joinColumns = @JoinColumn(name = "co_grupo") , inverseJoinColumns = @JoinColumn(name = "co_curriculum") )
	private Set<CurriculumLattes> pesquisadores = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rl_espectador", joinColumns = @JoinColumn(name = "co_grupo") , inverseJoinColumns = @JoinColumn(name = "co_usuario") )
    private Set<Usuario> espectadores = new HashSet<>();
}
