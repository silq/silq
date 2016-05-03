package br.ufsc.silq.web.cache;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

public class AbstractExpirableCacheTest {

	public static class TestCache extends AbstractExpirableCache<String> {
		@Override
		public Period expirePeriod() {
			return Period.millis(500);
		}
	}

	public TestCache testCache;

	@Before
	public void setup() {
		this.testCache = new TestCache();
	}

	public void populate(TestCache cache) {
		cache.insert("id_1", "Test 1");
		cache.insert("id_1", "Test 2");
		cache.insert("id_2", "Test 3");
	}

	@Test
	public void testInsertAndGet() {
		this.populate(this.testCache);

		Assertions.assertThat(this.testCache.get("id_1")).isEqualTo(Arrays.asList("Test 1", "Test 2"));
		Assertions.assertThat(this.testCache.get("id_2")).isEqualTo(Arrays.asList("Test 3"));
		Assertions.assertThat(this.testCache.get("id_999")).isEqualTo(Arrays.asList());
	}

	@Test
	public void testClear() {
		this.populate(this.testCache);

		this.testCache.clear("id_1");

		Assertions.assertThat(this.testCache.get("id_1")).isEqualTo(Arrays.asList());
		Assertions.assertThat(this.testCache.get("id_2")).isEqualTo(Arrays.asList("Test 3"));
	}

	@Test
	public void testGetModifiedAt() throws InterruptedException {
		LocalDateTime init = LocalDateTime.now();
		Thread.sleep(100);
		this.populate(this.testCache);
		Thread.sleep(100);
		LocalDateTime end = LocalDateTime.now();

		Assertions.assertThat(this.testCache.getModifiedAt("id_1").isAfter(init)).isTrue();
		Assertions.assertThat(this.testCache.getModifiedAt("id_1").isBefore(end)).isTrue();

		Thread.sleep(100);
		this.testCache.insert("id_1", "New Test");
		Assertions.assertThat(this.testCache.getModifiedAt("id_1").isAfter(end)).isTrue();
	}

	@Test
	public void testExpired() throws InterruptedException {
		this.populate(this.testCache);

		this.testCache.clearExpired();
		Assertions.assertThat(this.testCache.get("id_1")).hasSize(2);
		Assertions.assertThat(this.testCache.get("id_2")).hasSize(1);

		Thread.sleep(1000);

		this.testCache.insert("id_2", "Cache item to update modifiedAt");
		this.testCache.clearExpired();
		Assertions.assertThat(this.testCache.get("id_1")).hasSize(0);
		Assertions.assertThat(this.testCache.get("id_2")).hasSize(2);
	}
}
