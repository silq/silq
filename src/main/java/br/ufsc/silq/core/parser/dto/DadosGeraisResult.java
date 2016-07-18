package br.ufsc.silq.core.parser.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "nome", "idCurriculo" })
public class DadosGeraisResult {
	private String nome;
	private String idCurriculo;
	private AreaConhecimento areaGrandeAreaConhecimento = new AreaConhecimento();
	private String nomeEspecialidade;
	private String nomeSubAreaConhecimento;
	private TipoOrigemCurriculo tipoOrigemCurriculo;
	private Date ultimaAtualizacao;
}
