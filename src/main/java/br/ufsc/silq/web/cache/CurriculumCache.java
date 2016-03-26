package br.ufsc.silq.web.cache;

import java.io.File;
import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.business.service.util.UploadManager;
import br.ufsc.silq.web.cache.CurriculumCache.Curriculum;
import lombok.Data;

/**
 * Cache utilizado para guardar os currículos Lattes enviados de forma
 * assíncrona ao servidor.
 */
@Service
public class CurriculumCache extends AbstractCache<Curriculum> {

	/**
	 * Salva um currículo em cache e associa-o ao cacheId especificado.
	 *
	 * @param cacheId
	 *            ID do cache a ser utilizado. Um mesmo ID mapeia vários
	 *            currículos (a serem utilizados em uma mesma avaliação).
	 * @param file
	 *            Arquivo a ser salvo.
	 * @return
	 * @throws IOException
	 */
	public Curriculum insert(String cacheId, MultipartFile upload) throws IOException {
		Curriculum curriculum = new Curriculum(upload, cacheId);
		this.insert(cacheId, curriculum);
		return curriculum;
	}

	/**
	 * Remove os objetos do cache e exclui os arquivos temporários relacionados
	 * a ele.
	 *
	 * @param cacheId
	 */
	@Override
	@Async
	public void clear(String cacheId) {
		for (Curriculum curriculum : this.get(cacheId)) {
			curriculum.getFile().delete();
		}

		super.clear(cacheId);
	}

	@Data
	public static class Curriculum {
		@JsonIgnore
		private final File file;
		private final String name;
		private final long size;
		private final String cacheId;

		public Curriculum(MultipartFile upload, String cacheId) throws IllegalStateException, IOException {
			this.file = UploadManager.createTempFileFromUpload(upload);
			this.name = upload.getOriginalFilename();
			this.size = upload.getSize();
			this.cacheId = cacheId;
		}
	}

}
