package br.ufsc.silq.core.cache;

import org.joda.time.Period;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.ufsc.silq.core.data.AvaliacaoResult;
import lombok.extern.log4j.Log4j;

/**
 * Cache utilizado para guardar resultados de avaliação ({@link AvaliacaoResult}) de
 * currículos. Na avaliação livre, por exemplo, os currículos são enviados e
 * salvos em um {@link CurriculumCache} com um ID representando a avaliação. Ao
 * realizar a avaliação é criada uma nova entrada em {@link AvaliacaoCache} com
 * o mesmo ID do cache dos currículos, contendo os resultados das avaliações.
 */
@Service
@Log4j
public class AvaliacaoCache extends AbstractExpirableCache<AvaliacaoResult> {

	@Override
	public Period expirePeriod() {
		return Period.hours(2);
	}

	@Scheduled(fixedDelay = 1000 * 60 * 30) // 30 minutos
	public void expireJob() {
		int count = this.clearExpired();
		log.info("Cache de avaliação limpo. Itens expirados: " + count + ". Total: " + this.count());
	}
}
