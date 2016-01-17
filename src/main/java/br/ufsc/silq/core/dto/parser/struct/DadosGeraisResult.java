package br.ufsc.silq.core.dto.parser.struct;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DadosGeraisResult implements Serializable {

	private static final long serialVersionUID = -7762349339590570720L;

	private String nome;
	private String idCurriculo;
	private AreaConhecimento areaGrandeAreaConhecimento;
	private String nomeEspecialidade;
	private String nomeSubAreaConhecimento;
	private TipoOrigemCurriculo tipoOrigemCurriculo;
	private Date ultimaAtualizacao;

	public DadosGeraisResult() {
		this.nome = "-";
		this.areaGrandeAreaConhecimento = new AreaConhecimento();
		this.nomeEspecialidade = "-";
		this.nomeSubAreaConhecimento = "-";
	}

}
