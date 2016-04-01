package br.ufsc.silq.core.business.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.ufsc.silq.core.exception.SilqLattesException;

@Service
public class DocumentManager {
	public static final String LATTES_DTD_PATH = "src/main/resources/xml/lattes.dtd";
	public static final File LATTES_DTD = new File(LATTES_DTD_PATH);

	protected final DocumentBuilderFactory builderFactory;

	public DocumentManager() {
		this.builderFactory = DocumentBuilderFactory.newInstance();
	}

	/**
	 * Extrai um {@link Document} representando o currículo Lattes de um
	 * pesquisador.
	 *
	 * @param upload
	 *            Upload contendo o currículo Lattes em XML ou ZIP de um
	 *            pesquisador.
	 * @return O documento XML extraído do upload.
	 * @throws SilqLattesException
	 *             Caso o upload não seja um currículo Lattes XML ou ZIP válido.
	 */
	public Document extractXmlDocumentFromUpload(MultipartFile upload) throws SilqLattesException {
		if (this.isZipUpload(upload)) {
			return this.extractXmlDocumentFromZipUpload(upload);
		}

		Document document = null;
		DocumentBuilder builder = null;

		try {
			builder = this.builderFactory.newDocumentBuilder();
			document = builder.parse(upload.getInputStream());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			throw new SilqLattesException();
		}

		document.getDocumentElement().normalize();
		this.validateAgainstDTD(document);
		return document;
	}

	/**
	 * Checa se o upload trata-se de um arquivo tipo Zip.
	 *
	 * @param upload
	 * @return
	 */
	public boolean isZipUpload(MultipartFile upload) {
		return upload.getContentType() != null && upload.getContentType().equals("application/zip");
	}

	/**
	 * Extrai um {@link Document} representando o currículo Lattes de um
	 * pesquisador de um upload de um arquivo ZIP contendo uma entrada única de
	 * um currículo Lattes XML.
	 *
	 * @param upload
	 *            Upload de um arquivo ZIP contendo um currículo Lattes em XML.
	 * @return O documento XML extraído do upload.
	 * @throws SilqLattesException
	 *             Caso o arquivo ZIP seja inválido.
	 */
	public Document extractXmlDocumentFromZipUpload(MultipartFile upload) throws SilqLattesException {
		ZipInputStream zis;
		ZipEntry entry;
		int bufferSize = 1024;
		byte buffer[] = new byte[bufferSize];
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		String curriculumStr;

		try {
			zis = new ZipInputStream(upload.getInputStream());

			while ((entry = zis.getNextEntry()) != null) {
				int count;
				while ((count = zis.read(buffer, 0, bufferSize)) != -1) {
					dest.write(buffer, 0, count);
				}
			}

			curriculumStr = new String(dest.toByteArray(), Charset.forName("ISO8859_1"));
			dest.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new SilqLattesException("Arquivo ZIP inválido");
		}

		return this.stringToDocument(curriculumStr);
	}

	/**
	 * Transforma um {@link Document} para String.
	 *
	 * @param document
	 * @return
	 */
	public String documentToString(Document document) {
		DOMSource domSource = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * Transforma uma String representando um currículo XML em um Document
	 * correspondente.
	 *
	 * @param curriculo
	 * @return
	 * @throws SilqLattesException
	 */
	public Document stringToDocument(String curriculo) throws SilqLattesException {
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = this.builderFactory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(curriculo)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new SilqLattesException();
		}

		return document;
	}

	protected void validateAgainstDTD(Document document) throws SilqLattesException {
		try {
			DocumentBuilder documentBuilder = this.builderFactory.newDocumentBuilder();

			DOMSource source = new DOMSource(document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, LATTES_DTD.getAbsolutePath());
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);

			documentBuilder.parse(new InputSource(new StringReader(writer.toString())));
		} catch (Exception e) {
			e.printStackTrace();
			throw new SilqLattesException();
		}
	}

}
