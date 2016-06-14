package br.ufsc.silq.config;

import java.util.Set;
import java.util.SortedSet;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ehcache.InstrumentedEhcache;

import br.ufsc.silq.config.datasource.DatabaseConfiguration;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class, DatabaseConfiguration.class })
public class CacheConfiguration {

	private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private MetricRegistry metricRegistry;

	private net.sf.ehcache.CacheManager cacheManager;

	@PreDestroy
	public void destroy() {
		this.log.info("Remove Cache Manager metrics");
		SortedSet<String> names = this.metricRegistry.getNames();
		names.forEach(this.metricRegistry::remove);
		this.log.info("Closing Cache Manager");
		this.cacheManager.shutdown();
	}

	@Bean
	public CacheManager cacheManager(JHipsterProperties jHipsterProperties) {
		this.log.debug("Starting Ehcache");
		this.cacheManager = net.sf.ehcache.CacheManager.create();
		this.cacheManager.getConfiguration().setMaxBytesLocalHeap(jHipsterProperties.getCache().getEhcache().getMaxBytesLocalHeap());
		this.log.debug("Registering Ehcache Metrics gauges");
		Set<EntityType<?>> entities = this.entityManager.getMetamodel().getEntities();
		for (EntityType<?> entity : entities) {

			String name = entity.getName();
			if (name == null || entity.getJavaType() != null) {
				name = entity.getJavaType().getName();
			}
			Assert.notNull(name, "entity cannot exist without a identifier");

			net.sf.ehcache.Cache cache = this.cacheManager.getCache(name);
			if (cache != null) {
				cache.getCacheConfiguration().setTimeToLiveSeconds(jHipsterProperties.getCache().getTimeToLiveSeconds());
				net.sf.ehcache.Ehcache decoratedCache = InstrumentedEhcache.instrument(this.metricRegistry, cache);
				this.cacheManager.replaceCacheWithDecoratedCache(cache, decoratedCache);
			}
		}
		EhCacheCacheManager ehCacheManager = new EhCacheCacheManager();
		ehCacheManager.setCacheManager(this.cacheManager);
		return ehCacheManager;
	}
}
