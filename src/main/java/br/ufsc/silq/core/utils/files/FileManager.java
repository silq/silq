package br.ufsc.silq.core.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.ufsc.silq.core.exceptions.SilqErrorException;

public class FileManager {

	public static String extractCurriculum(File curriculo) {
		FileManager.validateCurriculumAgainstDTD(curriculo);

		List<String> documentLines = new ArrayList<>();
		try {
			String content = FileUtils.readFileToString(curriculo, "ISO8859_1");
			FileUtils.write(curriculo, content, "UTF-8");
			Path path = Paths.get(curriculo.getPath());
			documentLines = Files.readAllLines(path, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String curriculum = "";
		for (String line : documentLines) {
			curriculum += line;
		}

		return curriculum;
	}

	public static Document createXmlDocument(String curriculo) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(curriculo)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;
	}

	public static Document createXmlDocument(File curriculo) {
		FileManager.validateCurriculumAgainstDTD(curriculo);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;

		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(curriculo));
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;
	}

	public static Node getNodoRaiz(File curriculo) throws SilqErrorException {
		FileManager.validateCurriculumAgainstDTD(curriculo);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		Node raiz = null;

		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(curriculo));
			document.getDocumentElement().normalize();

			NodeList qualisList = document.getElementsByTagName("CURRICULO-VITAE");
			raiz = qualisList.item(0);
		} catch (Exception e) {
			throw new SilqErrorException("Ocorreu um erro inesperado: " + e.getMessage());
		}

		return raiz;
	}

	public static void main(String[] args) {
		File curriculo = new File("C:\\Users\\Felps\\Workspace\\silq\\public\\xmlvalidation\\curriculos\\c1.xml");

		validateCurriculumAgainstDTD(curriculo);
	}

	public static void validateCurriculumAgainstDTD(File curriculo) {
		try {
			File dtd = new File("C:\\Users\\Felps\\Workspace\\silq\\public\\xmlvalidation\\lattes.dtd");

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			// TODO documentBuilderFactory.setValidating(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(curriculo);

			DOMSource source = new DOMSource(document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd.getAbsolutePath());
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);

			documentBuilder.parse(new InputSource(new StringReader(writer.toString())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
