package br.ufsc.silq.core.business.service.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadManager {

	/**
	 * Cria um {@link File} temporário a partir de um {@link MultipartFile}
	 * upload, gerando um nome aleatório para o arquivo.
	 * 
	 * @param upload
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static File createTempFileFromUpload(MultipartFile upload) throws IllegalStateException, IOException {
		String newName = UUID.randomUUID().toString();
		File tempFile = File.createTempFile(newName, null);
		upload.transferTo(tempFile);
		return tempFile;
	}

}
