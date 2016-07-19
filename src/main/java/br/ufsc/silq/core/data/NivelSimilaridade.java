package br.ufsc.silq.core.data;

import java.security.InvalidParameterException;

import lombok.Data;

@Data
public class NivelSimilaridade implements Comparable<NivelSimilaridade> {
	public static final NivelSimilaridade MUITO_BAIXO = new NivelSimilaridade(0.2F);
	public static final NivelSimilaridade BAIXO = new NivelSimilaridade(0.4F);
	public static final NivelSimilaridade NORMAL = new NivelSimilaridade(0.6F);
	public static final NivelSimilaridade ALTO = new NivelSimilaridade(0.8F);
	public static final NivelSimilaridade MUITO_ALTO = new NivelSimilaridade(0.9F);
	public static final NivelSimilaridade TOTAL = new NivelSimilaridade(1.0F);

	private final Float value;

	public NivelSimilaridade(Float value) {
		if (value < 0 || value > 1) {
			throw new InvalidParameterException("Nível de similaridade inválido: " + value + ". Valor deve estar no intervalo [0.0, 1.0].");
		}

		this.value = value;
	}

	public NivelSimilaridade(String textValue) {
		this(Float.parseFloat(textValue));
	}

	public String getText() {
		return this.toString();
	}

	@Override
	public String toString() {
		if (this.value == TOTAL.getValue()) {
			return "Total";
		}
		if (this.value >= MUITO_ALTO.getValue()) {
			return "Muito alto";
		}
		if (this.value >= ALTO.getValue()) {
			return "Alto";
		}
		if (this.value >= NORMAL.getValue()) {
			return "Normal";
		}
		if (this.value >= BAIXO.getValue()) {
			return "Baixo";
		}
		if (this.value >= MUITO_BAIXO.getValue()) {
			return "Muito baixo";
		}
		return "Desconhecido";
	}

	@Override
	public int compareTo(NivelSimilaridade o) {
		return this.getValue().compareTo(o.getValue());
	}
}
