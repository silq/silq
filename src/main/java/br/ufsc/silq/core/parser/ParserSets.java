package br.ufsc.silq.core.parser;

import java.util.Arrays;

import br.ufsc.silq.core.dto.parser.struct.ParserSet;

public class ParserSets {

	public static final ParserSet NOME_SET = new ParserSet(Arrays.asList("dados-gerais"),
			Arrays.asList("nome-completo"), Arrays.asList("dados-gerais"));

	public static final ParserSet AREAS_SET = new ParserSet(
			Arrays.asList("dados-gerais", "areas-de-atuacao", "area-de-atuacao"),
			Arrays.asList("nome-da-especialidade", "nome-da-sub-area-do-conhecimento", "nome-da-area-do-conhecimento",
					"nome-grande-area-do-conhecimento"),
			Arrays.asList("area-de-atuacao"));

	public static final ParserSet PRODUCOES_SET = new ParserSet(
			Arrays.asList("producao-bibliografica", "trabalhos-em-eventos", "trabalho-em-eventos"),
			Arrays.asList("ano-do-trabalho", "titulo-do-trabalho", "nome-do-evento"),
			Arrays.asList("dados-basicos-do-trabalho", "detalhamento-do-trabalho"));

	public static final ParserSet DADOS_GERAIS_SET = new ParserSet(Arrays.asList("curriculo-vitae"),
			Arrays.asList("sistema-origem-xml", "data-atualizacao", "hora-atualizacao", "numero-identificador"),
			Arrays.asList("curriculo-vitae"));
}
