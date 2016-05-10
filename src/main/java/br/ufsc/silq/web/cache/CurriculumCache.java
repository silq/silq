package br.ufsc.silq.web.cache;

import javax.inject.Inject;

import org.joda.time.Period;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.service.DocumentManager;
import br.ufsc.silq.web.cache.CurriculumCache.Curriculum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Cache utilizado para guardar os currículos Lattes enviados de forma
 * assíncrona ao servidor.
 */
@Service
@Slf4j
public class CurriculumCache extends AbstractExpirableCache<Curriculum> {

	@Inject
	protected DocumentManager documentManager;

	/**
	 * Salva um currículo em cache e associa-o ao cacheId especificado.
	 *
	 * @param cacheId
	 *            ID do cache a ser utilizado. Um mesmo ID mapeia vários
	 *            currículos (a serem utilizados em uma mesma avaliação).
	 * @param upload
	 *            Arquivo a ser salvo.
	 * @return
	 * @throws SilqLattesException
	 */
	public Curriculum insert(String cacheId, MultipartFile upload) throws SilqLattesException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(upload);
		Curriculum curriculum = new Curriculum(document, upload.getOriginalFilename(), upload.getSize(), cacheId);
		this.insert(cacheId, curriculum);
		return curriculum;
	}

	@Override
	Period expirePeriod() {
		return Period.hours(1);
	}

	@Scheduled(fixedDelay = 1000 * 60 * 15) // 15 minutos
	public void expireJob() {
		int count = this.clearExpired();
		log.info("Cache de currículos limpo. Itens expirados: " + count + ". Total: " + this.count());
	}

	@Data
	public static class Curriculum {
		@JsonIgnore
		private final Document document;
		private final String name;
		private final long size;
		private final String cacheId;
	}
}
