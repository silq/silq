package br.ufsc.silq.core.parser.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufsc.silq.core.parser.dto.Artigo;

public class ArtigoAttributeGetter {

	public static List<Artigo> iterateUntilArtigos(Node raizLocal) {
		NodeList qualisList = raizLocal.getChildNodes();
		List<Artigo> artigos = new ArrayList<>();

		if ("artigo-publicado".equalsIgnoreCase(raizLocal.getNodeName())) {
			artigos.add(ArtigoAttributeGetter.getArtigoInfo(raizLocal));
		} else if (qualisList.getLength() > 0) {
			for (int i = 0; i < qualisList.getLength(); i++) {
				Node nodoFilho = qualisList.item(i);
				artigos.addAll(iterateUntilArtigos(nodoFilho));
			}
		}

		return artigos;
	}

	private static Artigo getArtigoInfo(Node raizLocal) {
		NodeList filhos = raizLocal.getChildNodes();

		String titulo = null;
		Integer ano = null;
		String tituloVeiculo = null;
		String issn = null;

		for (int i = 0; i < filhos.getLength(); i++) {
			Node filho = filhos.item(i);
			String nodeName = filho.getNodeName().toLowerCase();

			if ("dados-basicos-do-artigo".equals(nodeName)) {
				List<String> result = getAttribute(filho, Arrays.asList("titulo-do-artigo", "ano-do-artigo"));
				titulo = result.get(1);
				ano = parseIntegerSafely(result.get(0));
			} else if ("detalhamento-do-artigo".equals(nodeName)) {
				List<String> result = getAttribute(filho, Arrays.asList("titulo-do-periodico-ou-revista", "issn"));
				tituloVeiculo = result.get(1);
				issn = result.get(0);
			}
		}

		return new Artigo(titulo, ano, tituloVeiculo, issn);
	}

	public static List<String> getAttribute(Node nodo, List<String> attrList) {
		NamedNodeMap attributes = nodo.getAttributes();
		List<String> resultado = new ArrayList<>();

		for (int g = 0; g < attributes.getLength(); g++) {
			Attr attribute = (Attr) attributes.item(g);
			if (attrList.contains(attribute.getName().toLowerCase())) {
				resultado.add(attribute.getValue());
			}
		}

		return resultado;
	}

	public static Integer parseIntegerSafely(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}
		String filteredNumber = "";
		for (int i = 0; i < text.length(); i++) {
			char charAt = text.charAt(i);
			if (Character.isDigit(charAt)) {
				filteredNumber += Character.toString(charAt);
			}
		}

		if (filteredNumber.isEmpty()) {
			return null;
		}

		return Integer.parseInt(filteredNumber);
	}
}
