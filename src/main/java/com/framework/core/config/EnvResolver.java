package com.framework.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.framework.core.constants.ConfigConstants;
import com.framework.core.excepions.FrameworkException;
import com.framework.core.logging.TestLogger;

/**
 * EnvResolver
 *
 * Resolves the active environment name used to pick a properties file.
 *
 * Flow: resolve() → System property "env" → OS env "ENV" → default "qa" →
 * ConfigLoader builds path config/{env}.properties
 */
public class EnvResolver {

	public static String resolve() {

		TestLogger.logStep("temp ---EnvResolver.resolve");

		// Priority 1: JVM argument (-Denv=qa)
		String env = System.getProperty("env");

		// Priority 2: OS / CI environment variable
		if (env == null || env.isBlank()) {
			env = System.getenv("ENV");
		}

		// Priority 3: default fallback
		if (ConfigConstants.isDefaultFallbackEnvExpected) {
			if (env == null || env.isBlank()) {
				env = ConfigConstants.default_fallback_env;
			}
		} else {
			throw new FrameworkException("Enviornment NOT FOUND to the EnvResolver class.");
		}

		return env.toLowerCase();
	}
}
