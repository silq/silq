package br.ufsc.silq.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;

public class Fixtures {
	private static final String BASE_PATH = "src/test/resources/fixtures/curricula/";

	private static final String ERROR_XML = BASE_PATH + "error.xml";
	private static final String EMPTY_XML = BASE_PATH + "empty.xml";
	private static final String CHRISTIANE_XML = BASE_PATH + "christiane.xml";
	private static final String RAUL_XML = BASE_PATH + "raul.xml";
	private static final String RONALDO_XML = BASE_PATH + "ronaldo.xml";

	private static final String EMPTY_ZIP = BASE_PATH + "empty.zip";
	private static final String NO_XML_ZIP = BASE_PATH + "no-xml.zip";
	private static final String SEM_ID_ZIP = BASE_PATH + "sem-id.zip";
	private static final String CHRISTIANE_ZIP = CHRISTIANE_XML + ".zip";
	private static final String RAUL_ZIP = RAUL_XML + ".zip";
	private static final String RONALDO_ZIP = RONALDO_XML + ".zip";
	private static final String GUNTZEL_ZIP = BASE_PATH + "guntzel.zip";
	private static final String MARCIO_ZIP = BASE_PATH + "marcio.castro.zip";

	public static final MockMultipartFile ERROR_XML_UPLOAD = createMockUpload(ERROR_XML);
	public static final MockMultipartFile EMPTY_XML_UPLOAD = createMockUpload(EMPTY_XML);
	public static final MockMultipartFile CHRISTIANE_XML_UPLOAD = createMockUpload(CHRISTIANE_XML);
	public static final MockMultipartFile RAUL_XML_UPLOAD = createMockUpload(RAUL_XML);
	public static final MockMultipartFile RONALDO_XML_UPLOAD = createMockUpload(RONALDO_XML);

	public static final MockMultipartFile EMPTY_ZIP_UPLOAD = createMockZipUpload(EMPTY_ZIP);
	public static final MockMultipartFile NO_XML_ZIP_UPLOAD = createMockZipUpload(NO_XML_ZIP);
	public static final MockMultipartFile SEM_ID_ZIP_UPLOAD = createMockZipUpload(SEM_ID_ZIP);
	public static final MockMultipartFile CHRISTIANE_ZIP_UPLOAD = createMockZipUpload(CHRISTIANE_ZIP);
	public static final MockMultipartFile RAUL_ZIP_UPLOAD = createMockZipUpload(RAUL_ZIP);
	public static final MockMultipartFile RONALDO_ZIP_UPLOAD = createMockZipUpload(RONALDO_ZIP);
	public static final MockMultipartFile GUNTZEL_ZIP_UPLOAD = createMockZipUpload(GUNTZEL_ZIP);
	public static final MockMultipartFile MARCIO_ZIP_UPLOAD = createMockZipUpload(MARCIO_ZIP);

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
