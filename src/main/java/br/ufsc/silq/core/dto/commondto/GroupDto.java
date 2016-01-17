package br.ufsc.silq.core.dto.commondto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupDto {

	private Long id;
	private String instituicao;
	private String nome;
	private String nomeArea;
	private List<PesquisadorDto> pesquisadores;

}
