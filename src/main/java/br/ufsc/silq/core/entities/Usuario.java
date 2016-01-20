package br.ufsc.silq.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "Token_generator", sequenceName = "sq_usuario", allocationSize = 1, initialValue = 1)
@Table(name = "tb_usuario")
@NoArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
	@Column(name = "co_seq_usuario")
	private Long id;

	@NotBlank(message = "Campo obrigatório")
	@Column(name = "no_usuario")
	private String nome;

	@NotBlank(message = "Campo obrigatório")
	@Column(name = "ds_senha")
	private String senha;

	@NotBlank(message = "Entre com um e-mail válido")
	@Email
	@Column(name = "ds_email")
	private String email;

	@NotBlank(message = "Campo obrigatório")
	@Column(name = "no_sexo")
	private String sexo;

	@Size(max = 20)
	@Column(name = "reset_key", length = 20)
	private String resetKey;

	// TODO (bonetti): autoridades!

}
