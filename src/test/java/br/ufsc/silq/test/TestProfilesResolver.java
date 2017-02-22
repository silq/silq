package br.ufsc.silq.test;

import org.springframework.test.context.ActiveProfilesResolver;

import br.ufsc.silq.config.Profiles;

public class TestProfilesResolver implements ActiveProfilesResolver {
	public static String[] defaultProfiles = {
			Profiles.TEST,
			Profiles.FAST
	};

	@Override
	public String[] resolve(Class<?> arg0) {
		String profiles = System.getProperty("spring.profiles.active");

		if (profiles != null) {
			return new String[] { profiles };
		} else {
			return defaultProfiles;
		}
	}
}
