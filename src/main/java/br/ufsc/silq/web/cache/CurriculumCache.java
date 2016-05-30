package br.ufsc.silq.web.cache;

import org.joda.time.Period;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import lombok.extern.slf4j.Slf4j;

/**
 * Cache utilizado para guardar os currículos Lattes enviados de forma
 * assíncrona ao servidor.
 */
@Service
@Slf4j
public class CurriculumCache extends AbstractExpirableCache<CurriculumLattes> {

	@Override
	Period expirePeriod() {
		return Period.hours(1);
	}

	@Scheduled(fixedDelay = 1000 * 60 * 15) // 15 minutos
	public void expireJob() {
		int count = this.clearExpired();
		log.info("Cache de currículos limpo. Itens expirados: " + count + ". Total: " + this.count());
	}
}
