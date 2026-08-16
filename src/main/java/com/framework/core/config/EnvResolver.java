package com.framework.core.config;

import com.framework.core.constants.ConfigConstants;
import com.framework.core.enums.EnvironmentType;


/**
 * EnvResolver
 *
 * Resolves the active environment name used to pick a properties file.
 *
 * Flow: resolve() → System property "env" → OS env "ENV" → default "qa" →
 * ConfigLoader builds path config/{env}.properties
 */
public class EnvResolver {

	public static EnvironmentType resolve() {

		// Priority 1: JVM argument (-Denv=qa)
		String env = System.getProperty("env");

		// Priority 2: OS / CI environment variable
		if (isBlank(env)) {
			env = System.getenv("ENV");
		}

		// Priority 3: default fallback
		if (isBlank(env)) {

			env = ConfigConstants.DEFAULT_FALLBACK_ENV;

		}

		return EnvironmentType.from(env);
	}

	private static boolean isBlank(String text) {

		if (text == null || text.isBlank()) {
			return true;
		} else {
			return false;
		}

	}
}
