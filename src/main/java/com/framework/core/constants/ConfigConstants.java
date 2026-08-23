package com.framework.core.constants;

import java.util.List;

/**
 * ConfigConstants
 *
 * Immutable paths and required property keys for environment configuration.
 *
 * Flow: EnvResolver.resolve() → CONFIG_DIRECTORY + env + CONFIG_EXTENSION →
 * ConfigLoader validates MANDATORY_PROPERTIES
 */
public final class ConfigConstants {

	private ConfigConstants() {
	    throw new UnsupportedOperationException(
	            "Utility class should not be instantiated.");
	}

	/*
	 * File.separator represents the operating-system filesystem separator. For
	 * example: Windows → \ Linux → / But a ClassLoader resource path is a classpath
	 * resource name, not an OS filesystem path. The resource naming convention is:
	 * config/qa.properties regardless of OS.
	 */

	public static final String CONFIG_DIRECTORY = "config/";

	public static final String CONFIG_EXTENSION = ".properties";

	public static final String DEFAULT_FALLBACK_ENV = "QA";

	public static final List<String> MANDATORY_PROPERTIES = List.of("browser", "headless", "baseUrl");
}
