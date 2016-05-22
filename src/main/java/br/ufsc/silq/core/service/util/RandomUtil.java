package br.ufsc.silq.core.service.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

	private static final int DEF_COUNT = 20;

	/**
	 * Generates a password.
	 *
	 * @return the generated password
	 */
	public static String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
	}

	/**
	 * Generates a register key.
	 *
	 * @return the generated register key
	 */
	public static String generateRegisterKey() {
		return RandomStringUtils.randomNumeric(DEF_COUNT);
	}

	/**
	 * Generates a reset key.
	 *
	 * @return the generated reset key
	 */
	public static String generateResetKey() {
		return RandomStringUtils.randomNumeric(DEF_COUNT);
	}
}
