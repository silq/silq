package br.ufsc.silq.core.commondto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PesquisadorDto implements Comparable<PesquisadorDto> {

	private Long id;
	private Long idGrupo;
	private Long idCurriculo;
	private String nome;
	private Date dataAtualizacaoCurriculo;
	private Date dataAtualizacaoUsuario;
	private String curriculoXml;

	@Override
	public int compareTo(PesquisadorDto o) {
		return this.nome.compareTo(o.nome);
	}

}
