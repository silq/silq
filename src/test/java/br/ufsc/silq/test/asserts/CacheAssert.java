package br.ufsc.silq.test.asserts;

import org.assertj.core.api.AbstractAssert;
import org.springframework.util.StopWatch;

import com.google.common.base.Supplier;

/**
 * Custom assert para checar se um método é cacheado.
 * Esta Assertion basicamente executa o {@link Supplier} dado como parâmetro duas vezes, e
 * checa se na segunda vez o método executa consideravelmente mais rápido que da primeira e retorna
 * o mesmo resultado.
 */
public class CacheAssert extends AbstractAssert<CacheAssert, Supplier<?>> {

	/**
	 * O valor padrão de quão rápido deve ser a segunda execução do método.
	 */
	private static final int DEFAULT_TIMES_FASTER = 25;

	public CacheAssert(Supplier<?> actual) {
		super(actual, CacheAssert.class);
	}

	public static CacheAssert assertThat(Supplier<?> supplier) {
		return new CacheAssert(supplier);
	}

	public CacheAssert isCached(int timesFaster) {
		this.isNotNull();

		StopWatch watch1 = new StopWatch();
		watch1.start();
		Object result1 = this.actual.get();
		watch1.stop();

		StopWatch watch2 = new StopWatch();
		watch2.start();
		Object result2 = this.actual.get();
		watch2.stop();

		if (watch2.getLastTaskTimeMillis() * timesFaster >= watch1.getLastTaskTimeMillis()) {
			this.failWithMessage("Method should be cached and execute at least %sx faster at second run. "
					+ "First run time (no cache): %sms. "
					+ "Second run time (with cache): %sms",
					timesFaster, watch1.getLastTaskTimeMillis(), watch2.getLastTaskTimeMillis());
		}

		if (!result2.equals(result1)) {
			this.failWithMessage("Cached method should return the same result for both executions. "
					+ "First result: <%s>, Second result: <%s>", result1, result2);
		}

		return this;
	}

	public CacheAssert isCached() {
		return this.isCached(DEFAULT_TIMES_FASTER);
	}
}
