package br.ufsc.silq.core.dto.parser.struct;

import java.io.Serializable;
import java.util.HashMap;

public class AreaConhecimento implements Serializable {

	private static final long serialVersionUID = -4862606476367568773L;
	
	private String nomeArea;
	private String nomeGrandeArea;
	private HashMap<String, String> areaMap;

	public AreaConhecimento() {
		this("", "");
	}

	public AreaConhecimento(String nomeArea, String nomeGrandeArea) {
		this.nomeArea = nomeArea;
		this.nomeGrandeArea = nomeGrandeArea;
		this.createAreaMap();
	}

	@Override
	public String toString() {
		String info = "";

		info += this.getNomeArea() + " - " + this.getNomeGrandeArea();

		return info;
	}

	public String getNomeArea() {
		return nomeArea;
	}

	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}

	public String getNomeGrandeArea() {
		if (areaMap.containsKey(this.nomeGrandeArea)) {
			return areaMap.get(nomeGrandeArea);
		}

		return nomeGrandeArea;
	}

	public void setNomeGrandeArea(String nomeGrandeArea) {
		this.nomeGrandeArea = nomeGrandeArea;
	}

	private void createAreaMap() {
		areaMap = new HashMap<String, String>();

		areaMap.put("CIENCIAS_EXATAS_E_DA_TERRA", "Ciências Exatas e da Terra");
		areaMap.put("CIENCIAS_BIOLOGICAS", "Ciências Biológicas");
		areaMap.put("ENGENHARIAS", "Engenharias");
		areaMap.put("CIENCIAS_DA_SAUDE", "Ciências da Saúde");
		areaMap.put("CIENCIAS_AGRARIAS", "Ciências Agrárias");
		areaMap.put("CIENCIAS_SOCIAIS_APLICADAS", "Ciências Sociais Aplicadas");
		areaMap.put("CIENCIAS_HUMANAS", "Ciências Humanas");
		areaMap.put("LINGUISTICA_LETRAS_E_ARTES", "Linguística, Letras e Artes");
	}

}
