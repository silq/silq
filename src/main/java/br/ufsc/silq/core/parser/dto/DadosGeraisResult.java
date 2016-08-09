package br.ufsc.silq.core.parser.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = { "idCurriculo", "ultimaAtualizacao" })
@ToString(of = { "nome", "idCurriculo" })
public class DadosGeraisResult {
	private String nome;
	private String idCurriculo;
	private AreaConhecimento areaConhecimento = new AreaConhecimento();
	private TipoOrigemCurriculo tipoOrigemCurriculo;
	private Date ultimaAtualizacao;
}
