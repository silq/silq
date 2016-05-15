package br.ufsc.silq.config;

/**
 * Application constants.
 */
public final class Constants {

	// Spring profile for development, production and "fast", see
	// http://jhipster.github.io/profiles.html
	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
	public static final String SPRING_PROFILE_PRODUCTION = "prod";
	public static final String SPRING_PROFILE_FAST = "fast";
	public static final String SPRING_PROFILE_TEST = "test";

	// Spring profile used when deploying with Spring Cloud (used when deploying
	// to CloudFoundry)
	public static final String SPRING_PROFILE_CLOUD = "cloud";

	// Spring profile used when deploying to Heroku
	public static final String SPRING_PROFILE_HEROKU = "heroku";

	// Spring profile used when testing on Travis CI
	public static final String SPRING_PROFILE_TRAVIS = "travis";

	public static final String SYSTEM_ACCOUNT = "system";
}
