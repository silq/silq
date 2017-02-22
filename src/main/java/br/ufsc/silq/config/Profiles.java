package br.ufsc.silq.config;

/**
 * Application constants.
 */
public final class Profiles {

	// Spring profile for development, production and "fast", see
	// http://jhipster.github.io/profiles.html
	public static final String DEVELOPMENT = "dev";
	public static final String PRODUCTION = "prod";
	public static final String FAST = "fast";
	public static final String TEST = "test";

	// Spring profile used when testing on Travis CI
	public static final String TRAVIS = "travis";

	public static final String SYSTEM_ACCOUNT = "system";
}
