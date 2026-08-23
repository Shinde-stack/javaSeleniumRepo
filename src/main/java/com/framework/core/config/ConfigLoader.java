package com.framework.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.framework.core.constants.ConfigConstants;
import com.framework.core.driver.BrowserType;
import com.framework.core.enums.EnvironmentType;
import com.framework.core.excepions.FrameworkException;

/**
 * ConfigLoader
 *
 * Loads environment properties from classpath and produces a typed EnvConfig.
 *
 * Flow: load() → resolve env → read config/{env}.properties → validate
 * mandatory keys → build EnvConfig
 *
 * Fails fast on missing file or missing keys before driver or reporting startup
 * continues.
 */
public class ConfigLoader {

	private static final List<String> REQUIRED_PROPERTIES = ConfigConstants.MANDATORY_PROPERTIES;

	public EnvConfig load() {

		Properties properties = loadProperties();

		validateProperties(properties);

		return buildConfig(properties);
	}

	private Properties loadProperties() {

		EnvironmentType env = EnvResolver.resolve();

		String file = ConfigConstants.CONFIG_DIRECTORY + env.getConfigFileName();

		Properties props = new Properties();

		try (InputStream input = getClass().getClassLoader().getResourceAsStream(file)) {

			if (input == null) {
				throw new FrameworkException("Configuration file not found: " + file);
			}
			props.load(input);
		} catch (IOException e) {

			throw new FrameworkException(
					"Failed to read configuration file: " + file + " Exception msg. is :" + e.getMessage());
		}
		return props;
	}

	private void validateProperties(Properties properties) {

		List<String> missingProperties = new ArrayList<>();

		for (String key : REQUIRED_PROPERTIES) {

			String value = properties.getProperty(key);

			if (isBlank(value)) {
				missingProperties.add(key);
			}
		}

		if (!missingProperties.isEmpty()) {
			String message = "Config validation failed. Missing properties: " + String.join(", ", missingProperties);
			throw new FrameworkException(message);
		}
	}

	private EnvConfig buildConfig(Properties properties) {

		BrowserType browserType = BrowserType.from(properties.getProperty("browser"));
		String baseUrl = getBaseUrl(properties, "baseUrl");

		// boolean values
		boolean headless = getBoolean(properties, "headless", false);

		boolean logToConsole = getBoolean(properties, "log.console", true);

		boolean logToReport = getBoolean(properties, "log.report", true);

		boolean logElementActions = getBoolean(properties, "log.element.actions", true);

		boolean logWaitActions = getBoolean(properties, "log.wait.actions", true);

		boolean screenshotOnFailure = getBoolean(properties, "screenshot.on.failure", true);

		EnvConfig config = new EnvConfig();

		config.setBrowserType(browserType);
		config.setHeadless(headless);
		config.setBaseUrl(baseUrl);

		config.setLogToConsole(logToConsole);
		config.setLogToReport(logToReport);
		config.setLogElementActions(logElementActions);
		config.setLogWaitActions(logWaitActions);
		config.setScreenshotOnFailure(screenshotOnFailure);

		return config;
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean getBoolean(Properties properties, String key, boolean defaultValue) {

		String value = properties.getProperty(key);

		if (value == null) {
			return defaultValue;
		}

		value = value.trim();

		if ("true".equalsIgnoreCase(value)) {
			return true;
		}

		if ("false".equalsIgnoreCase(value)) {
			return false;
		}

		throw new FrameworkException(
				"Invalid boolean value for property '" + key + "': '" + value + "'. Expected 'true' or 'false'.");
	}

	private String getBaseUrl(Properties properties, String key) {

		String value = properties.getProperty(key);

		if (isBlank(value)) {
			throw new FrameworkException("Configuration property '" + key + "' cannot be null or blank.");
		}

		value = value.trim();

		try {

			URI uri = new URI(value);

			String scheme = uri.getScheme();

			if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {

				throw new FrameworkException(
						"Invalid URL scheme for property '" + key + "'. Only HTTP and HTTPS are supported.");
			}

			if (uri.getHost() == null || uri.getHost().isBlank()) {

				throw new FrameworkException("Invalid URL for property '" + key + "'. Host name is missing.");
			}

			return value;

		} catch (URISyntaxException e) {

			throw new FrameworkException("Invalid URL syntax for property '" + key + "': " + value
					+ " Exception msg. is :" + e.getMessage());
		}
	}
}
