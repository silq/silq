package br.ufsc.silq.core.utils.combo;

import java.util.Calendar;
import java.util.LinkedHashMap;

import br.ufsc.silq.core.enums.GrupoConceitual;
import br.ufsc.silq.core.enums.Periodo;
import br.ufsc.silq.core.enums.TipoGrafico;

public class ComboValueHelper {

	public static LinkedHashMap<String, String> getSexoValues() {
		LinkedHashMap<String, String> sexoMap = new LinkedHashMap<String, String>();

		sexoMap.put("Masculino", "Masculino");
		sexoMap.put("Feminino", "Feminino");

		return sexoMap;
	}

	// TODO ficou horrível
	public static LinkedHashMap<String, String> getAnoPublicacaoValues(boolean a) {
		LinkedHashMap<String, String> anoMap = new LinkedHashMap<String, String>();

		Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int ano = currentYear; ano > 1990; ano--) {
			anoMap.put(ano + "", ano + "");
		}

		return anoMap;
	}

	// TODO ficou horrível
	public static LinkedHashMap<String, String> getAnoPublicacaoValues() {
		LinkedHashMap<String, String> anoMap = new LinkedHashMap<String, String>();

		anoMap.put("", "");

		Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int ano = currentYear; ano > 1990; ano--) {
			anoMap.put(ano + "", ano + "");
		}

		return anoMap;
	}

	public static LinkedHashMap<String, String> getAreaValues() {
		LinkedHashMap<String, String> areaMap = new LinkedHashMap<String, String>();

		areaMap.put("Administração, Ciências Contábeis e Turismo", "Administração, Ciências Contábeis e Turismo");
		areaMap.put("Antropologia / Arqueologia", "Antropologia / Arqueologia");
		areaMap.put("Arquitetura e Urbanismo", "Arquitetura e Urbanismo");
		areaMap.put("Artes / Música", "Artes / Música");
		areaMap.put("Astronomia / Física", "Astronomia / Física");
		areaMap.put("Biodiversidade", "Biodiversidade");
		areaMap.put("Biotecnologia", "Biotecnologia");
		areaMap.put("Ciência da Computação", "Ciência da Computação");
		areaMap.put("Ciência de Alimentos", "Ciência de Alimentos");
		areaMap.put("Ciência Política e Relações Internacionais", "Ciência Política e Relações Internacionais");
		areaMap.put("Ciências Agrárias I", "Ciências Agrárias I");
		areaMap.put("Ciências Ambientais", "Ciências Ambientais");
		areaMap.put("Ciências Biológicas I", "Ciências Biológicas I");
		areaMap.put("Ciências Biológicas II", "Ciências Biológicas II");
		areaMap.put("Ciências Biológicas III", "Ciências Biológicas III");
		areaMap.put("Ciências Sociais Aplicadas I", "Ciências Sociais Aplicadas I");
		areaMap.put("Direito", "Direito");
		areaMap.put("Economia", "Economia");
		areaMap.put("Educação", "Educação");
		areaMap.put("Educação Física", "Educação Física");
		areaMap.put("Enfermagem", "Enfermagem");
		areaMap.put("Engenharias I", "Engenharias I");
		areaMap.put("Engenharias II", "Engenharias II");
		areaMap.put("Engenharias III", "Engenharias III");
		areaMap.put("Engenharias IV", "Engenharias IV");
		areaMap.put("Ensino", "Ensino");
		areaMap.put("Farmácia", "Farmácia");
		areaMap.put("Filosofia / Teologia", "Filosofia / Teologia");
		areaMap.put("Geociências", "Geociências");
		areaMap.put("Geografia", "Geografia");
		areaMap.put("História", "História");
		areaMap.put("Interdisciplinar", "Interdisciplinar");
		areaMap.put("Letras / Lingüística", "Letras / Lingüística");
		areaMap.put("Matemática / Probabilidade e Estatística", "Matemática / Probabilidade e Estatística");
		areaMap.put("Materiais", "Materiais");
		areaMap.put("Medicina I", "Medicina I");
		areaMap.put("Medicina II", "Medicina II");
		areaMap.put("Medicina III", "Medicina III");
		areaMap.put("Medicina Veterinária", "Medicina Veterinária");
		areaMap.put("Nutrição", "Nutrição");
		areaMap.put("Odontologia", "Odontologia");
		areaMap.put("Planejamento Urbano e Regional / Demografia", "Planejamento Urbano e Regional / Demografia");
		areaMap.put("Psicologia", "Psicologia");
		areaMap.put("Química", "Química");
		areaMap.put("Saúde Coletiva", "Saúde Coletiva");
		areaMap.put("Serviço Social", "Serviço Social");
		areaMap.put("Sociologia", "Sociologia");
		areaMap.put("Zootecnia / Recursos Pesqueiros", "Zootecnia / Recursos Pesqueiros");

		return areaMap;
	}

	public static LinkedHashMap<String, String> getNivelSimilaridadeValues() {
		LinkedHashMap<String, String> similaridadeMap = new LinkedHashMap<String, String>();

		similaridadeMap.put("0.9", "Muito Alto (90%)");
		similaridadeMap.put("0.8", "Alto (80%)");
		similaridadeMap.put("0.6", "Normal (60%)");
		similaridadeMap.put("0.4", "Baixo (40%)");
		similaridadeMap.put("0.2", "Muito Baixo (20%)");

		return similaridadeMap;
	}

	public static LinkedHashMap<String, String> getPeriodoValues() {
		LinkedHashMap<String, String> periodosMap = new LinkedHashMap<String, String>();

		periodosMap.put(Periodo.TRIENIO_2013_2015.name(), "2013-2015");
		periodosMap.put(Periodo.TRIENIO_2010_2012.name(), "2010-2012");
		periodosMap.put(Periodo.TRIENIO_2007_2009.name(), "2007-2009");
		periodosMap.put(Periodo.TODOS_TRIENIOS.name(), "Todos");

		return periodosMap;
	}

	public static LinkedHashMap<String, String> getConceitoValues() {
		LinkedHashMap<String, String> periodosMap = new LinkedHashMap<String, String>();

		periodosMap.put(GrupoConceitual.ESTRITO.getDescricao(), "Estrito: A1, A2 e B1");
		periodosMap.put(GrupoConceitual.OUTRO_B2.getDescricao(), "Outro: B2 e B3");
		periodosMap.put(GrupoConceitual.OUTRO_B4.getDescricao(), "Outro: B4, B5 e C");
		periodosMap.put(GrupoConceitual.OUTRO_ATE_B2.getDescricao(), "Outro: A1, A2, B1, B2 e B3");
		periodosMap.put(GrupoConceitual.TODOS.getDescricao(), "Todos");

		return periodosMap;
	}

	public static LinkedHashMap<String, String> getTiposGraficos() {
		LinkedHashMap<String, String> tipoGraficoMap = new LinkedHashMap<String, String>();

		tipoGraficoMap.put(TipoGrafico.GERAL.getDescricao(), "Geral por período");
		tipoGraficoMap.put(TipoGrafico.ANUAL.getDescricao(), "Anual");

		return tipoGraficoMap;
	}
}
