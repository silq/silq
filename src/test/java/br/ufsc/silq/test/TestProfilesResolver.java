package br.ufsc.silq.test;

import org.springframework.test.context.ActiveProfilesResolver;

import br.ufsc.silq.config.Constants;

public class TestProfilesResolver implements ActiveProfilesResolver {

	@Override
	public String[] resolve(Class<?> arg0) {
		String[] defaultProfiles = { Constants.SPRING_PROFILE_TEST };
		String profiles = System.getProperty("spring.profiles.active");

		if (profiles != null) {
			return new String[] { profiles };
		} else {
			return defaultProfiles;
		}
	}
}
