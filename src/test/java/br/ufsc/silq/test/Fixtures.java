package br.ufsc.silq.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;

public class Fixtures {
	public static final String CHRISTIANE_XML = "src/test/resources/fixtures/curricula/christiane.xml";
	public static final String RAUL_XML = "src/test/resources/fixtures/curricula/raul.xml";
	public static final String RONALDO_XML = "src/test/resources/fixtures/curricula/ronaldo.xml";
	public static final String ERROR_XML = "src/test/resources/fixtures/curricula/error.xml";
	public static final String EMPTY_XML = "src/test/resources/fixtures/curricula/empty.xml";

	public static final String CHRISTIANE_ZIP = CHRISTIANE_XML + ".zip";
	public static final String RAUL_ZIP = RAUL_XML + ".zip";
	public static final String RONALDO_ZIP = RONALDO_XML + ".zip";
	public static final String EMPTY_ZIP = "src/test/resources/fixtures/curricula/empty.zip";
	public static final String NO_XML_ZIP = "src/test/resources/fixtures/curricula/no-xml.zip";

	public static final MockMultipartFile CHRISTIANE_XML_UPLOAD = createMockUpload(CHRISTIANE_XML);
	public static final MockMultipartFile RAUL_XML_UPLOAD = createMockUpload(RAUL_XML);
	public static final MockMultipartFile RONALDO_XML_UPLOAD = createMockUpload(RONALDO_XML);
	public static final MockMultipartFile ERROR_XML_UPLOAD = createMockUpload(ERROR_XML);
	public static final MockMultipartFile EMPTY_XML_UPLOAD = createMockUpload(EMPTY_XML);

	public static final MockMultipartFile CHRISTIANE_ZIP_UPLOAD = createMockZipUpload(CHRISTIANE_ZIP);
	public static final MockMultipartFile RAUL_ZIP_UPLOAD = createMockZipUpload(RAUL_ZIP);
	public static final MockMultipartFile RONALDO_ZIP_UPLOAD = createMockZipUpload(RONALDO_ZIP);
	public static final MockMultipartFile EMPTY_ZIP_UPLOAD = createMockZipUpload(EMPTY_ZIP);
	public static final MockMultipartFile NO_XML_ZIP_UPLOAD = createMockZipUpload(NO_XML_ZIP);

	public static MockMultipartFile createMockUpload(String filepath) {
		return createMockUpload(filepath, "application/xml");
	}

	public static MockMultipartFile createMockZipUpload(String filepath) {
		return createMockUpload(filepath, "application/zip");
	}

	public static MockMultipartFile createMockUpload(String filepath, String contentType) {
		try {
			FileInputStream fis = new FileInputStream(new File(filepath));
			MockMultipartFile upload = new MockMultipartFile("upload.xml", null, contentType,
					FileCopyUtils.copyToByteArray(fis));
			return upload;
		} catch (IOException e) {
			System.err.println("Falha ao criar MockMultipartFile de testes: " + filepath);
			e.printStackTrace();
			return null;
		}
	}
}
