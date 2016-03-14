package br.ufsc.silq.web.cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.business.service.util.UploadManager;
import lombok.Data;
import lombok.ToString;

/**
 * Cache utilizado para guardar os currículos Lattes enviados de forma
 * assíncrona ao servidor. Cada cache é específico à sessão (usuário) atual,
 * graças ao escopo "session" definido na classe.
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ToString
public class CurriculumCache {
	private static final long serialVersionUID = 6545739383868346008L;

	private Map<String, ArrayList<Curriculum>> map = new HashMap<>();

	/**
	 * Salva um currículo em cache e associa-o ao cacheId especificado
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
		if (!this.map.containsKey(cacheId)) {
			this.map.put(cacheId, new ArrayList<>());
		}

		ArrayList<Curriculum> list = this.map.get(cacheId);
		Curriculum curriculum = new Curriculum(upload, cacheId);
		list.add(curriculum);
		return curriculum;
	}

	/**
	 * Retorna os currículos salvos relacionados ao cacheId informado.
	 *
	 * @param cacheId
	 * @return A lista de currículos associado ao ID ou uma lista vazia caso
	 *         nada foi encontrado.
	 */
	public ArrayList<Curriculum> get(String cacheId) {
		ArrayList<Curriculum> list = this.map.get(cacheId);
		return list != null ? list : new ArrayList<>();
	}

	/**
	 * Remove os objetos do cache e exclui os arquivos temporários relacionados
	 * a ele.
	 *
	 * @param cacheId
	 */
	public void clear(String cacheId) {
		for (Curriculum curriculum : this.get(cacheId)) {
			curriculum.getFile().delete();
		}

		this.map.remove(cacheId);
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
