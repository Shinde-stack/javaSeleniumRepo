package com.framework.core.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.framework.core.constants.ConfigConstants;
import com.framework.core.driver.BrowserType;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ReportManager;

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

		String env = EnvResolver.resolve();

		String file = ConfigConstants.CONFIG_DIRECTORY + env + ConfigConstants.CONFIG_EXTENSION;
		TestLogger.logStep("temp---config file path ->" + file);

		Properties props = new Properties();

		try (InputStream input = getClass().getClassLoader().getResourceAsStream(file)) {

			if (input == null) {
				throw new RuntimeException("Configuration file not found: " + file);
			}
			props.load(input);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load configuration file: " + file, e);
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

			String message = "CONFIG VALIDATION FAILED. Missing properties: " + String.join(", ", missingProperties);

			TestLogger.logFailure(message, new RuntimeException(message));

			try {
				ReportManager.fail(message);
			} catch (Exception ignored) {
			}

			throw new RuntimeException(message);
		}

		try {
			TestLogger.logStep("CONFIG VALIDATION PASSED");
		} catch (Exception ignored) {
		}
	}

	private EnvConfig buildConfig(Properties properties) {

		TestLogger.logStep("buildConfig");

		BrowserType browserType = BrowserType.valueOf(properties.getProperty("browser").trim().toUpperCase());

		boolean headless = Boolean.parseBoolean(properties.getProperty("headless"));

		String baseUrl = properties.getProperty("baseUrl").trim();

		boolean logToConsole = Boolean.parseBoolean(properties.getProperty("log.console", "true"));

		boolean logToReport = Boolean.parseBoolean(properties.getProperty("log.report", "true"));

		boolean logElementActions = Boolean.parseBoolean(properties.getProperty("log.element.actions", "true"));

		boolean logWaitActions = Boolean.parseBoolean(properties.getProperty("log.wait.actions", "true"));

		boolean screenshotOnFailure = Boolean.parseBoolean(properties.getProperty("screenshot.on.failure", "true"));

		EnvConfig config = new EnvConfig();

		config.setBrowserType(browserType);
		config.setHeadless(headless);
		config.setBaseUrl(baseUrl);

		config.setLogToConsole(logToConsole);
		config.setLogToReport(logToReport);
		config.setLogElementActions(logElementActions);
		config.setLogWaitActions(logWaitActions);

		config.setScreenshotOnFailure(screenshotOnFailure);

		TestLogger.logStep("config ----->" + config);

		return config;
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
