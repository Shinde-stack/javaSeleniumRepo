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
 * ============================================================================
 * Class Name : ConfigLoader
 * ============================================================================
 *
 * PURPOSE: -------- Loads environment-specific configuration and converts it
 * into a strongly typed EnvConfig object used by the framework at runtime.
 *
 * CORE RESPONSIBILITY FLOW: -------------------------
 *
 * 1. Resolve environment (QA / DEV / STAGE) 2. Locate correct properties file
 * 3. Load file into memory (Properties object) 4. Validate mandatory keys (fail
 * fast with full report) 5. Convert raw strings → typed objects (EnvConfig)
 *
 * FAILURE STRATEGY: ------------------ - Missing file → STOP execution
 * immediately - Missing mandatory keys → STOP execution with full list -
 * Invalid values → fail during type conversion
 *
 * ============================================================================
 */
public class ConfigLoader {

	/**
	 * Mandatory keys required for framework startup.
	 *
	 * If any of these are missing, framework must NOT start.
	 */
	private static final List<String> REQUIRED_PROPERTIES = ConfigConstants.MANDATORY_PROPERTIES;

	/**
	 * ENTRY POINT
	 *
	 * This method orchestrates full config loading lifecycle.
	 */
	public EnvConfig load() {

		// ------------------------------------------------------------
		// STEP 1: LOAD RAW PROPERTIES
		// ------------------------------------------------------------
		// Responsibility:
		// - Identify environment
		// - Locate correct file
		// - Load properties into memory
		//
		// Output:
		// - Raw key-value pairs (String-based)
		Properties properties = loadProperties();

		// ------------------------------------------------------------
		// STEP 2: VALIDATION (FAIL FAST, BULK REPORTING)
		// ------------------------------------------------------------
		// Responsibility:
		// - Ensure all mandatory keys exist
		// - Collect ALL missing keys (not one-by-one failure)
		//
		// Why this step exists:
		// - Avoid repeated test failures
		// - Provide single consolidated failure report
		validateProperties(properties);

		// ------------------------------------------------------------
		// STEP 3: TYPE CONVERSION (RAW → STRONG TYPE)
		// ------------------------------------------------------------
		// Responsibility:
		// - Convert String values into enums/booleans/etc.
		// - Build EnvConfig object used by framework
		//
		// Example:
		// browser=chrome → BrowserType.CHROME
		return buildConfig(properties);
	}

	// =========================================================================
	// STEP 1 IMPLEMENTATION: LOAD PROPERTIES
	// =========================================================================

	/**
	 * Loads properties file based on resolved environment.
	 *
	 * FLOW: ----- 1. Resolve environment (qa/dev/stage) 2. Build file path 3. Read
	 * file from classpath 4. Load into Properties object
	 */
	private Properties loadProperties() {

	  	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("temp---loadProperties");

		// STEP 1.1: Identify runtime environment
		// Example output: "qa"
		String env = EnvResolver.resolve();

		// STEP 1.2: Build file name dynamically
		// Example: config-qa.properties
		String file = ConfigConstants.CONFIG_PATH + env + ConfigConstants.CONFIG_EXTENSION;
        TestLogger.logStep("temp---config file path ->"+file);

		Properties props = new Properties();

		// STEP 1.3: Load file from classpath
		// WHY classpath:
		// - Works in local, CI, Docker, JAR execution
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(file)) {

			// STEP 1.4: Fail fast if file not found
			if (input == null) {
				throw new RuntimeException("Configuration file not found: " + file);
			}

			// STEP 1.5: Load key-value pairs into memory
			props.load(input);
	        TestLogger.logStep("temp---loaded props ->"+props);

		} catch (Exception e) {
			throw new RuntimeException("Failed to load configuration file: " + file, e);
		}

		return props;
	}

	// =========================================================================
	// STEP 2: VALIDATION LOGIC
	// =========================================================================

	/**
	 * Validates mandatory configuration keys.
	 *
	 * DESIGN: ------- - Collect all missing keys - Fail once with full report -
	 * Avoid iterative failure cycles
	 */
	private void validateProperties(Properties properties) {

	  	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("validateProperties");

		List<String> missingProperties = new ArrayList<>();

		for (String key : REQUIRED_PROPERTIES) {

			String value = properties.getProperty(key);

			if (isBlank(value)) {
				missingProperties.add(key);
			}
		}

		// ---------------------------------------------------------
		// CASE 1: FAILURE PATH (IMPORTANT)
		// ---------------------------------------------------------
		if (!missingProperties.isEmpty()) {

			String message = "CONFIG VALIDATION FAILED. Missing properties: " + String.join(", ", missingProperties);

			// Console + file log (always available)
			TestLogger.logFailure(message, new RuntimeException(message));

			// Extent report (only if initialized)
			try {
				ReportManager.fail(message);
			} catch (Exception ignored) {
				// ignore if reporting not initialized yet
			}

			throw new RuntimeException(message);
		}

		// ---------------------------------------------------------
		// CASE 2: SUCCESS PATH
		// ---------------------------------------------------------
	
		try {
			TestLogger.logStep("CONFIG VALIDATION PASSED");	
		} catch (Exception ignored) {
			// safe ignore if report not initialized yet
		}
	}

	// =========================================================================
	// STEP 3: TYPE CONVERSION
	// =========================================================================

	/**
	 * Converts raw String properties into typed EnvConfig object.
	 *
	 * WHY THIS STEP EXISTS: --------------------- Properties file is String-based.
	 * Framework needs strongly typed values.
	 *
	 * Example conversion: ------------------- "chrome" → BrowserType.CHROME "true"
	 * → boolean true
	 */
	// =========================================================================
	// STEP 3: TYPE CONVERSION
	// =========================================================================

	/**
	 * Converts raw String properties into strongly typed EnvConfig.
	 *
	 * WHY THIS STEP EXISTS:
	 * ---------------------
	 * Properties files store everything as String.
	 *
	 * Framework components require:
	 * - Enums
	 * - Booleans
	 * - Validated configuration
	 *
	 * Example:
	 *
	 * browser=chrome
	 *      ↓
	 * BrowserType.CHROME
	 *
	 * headless=true
	 *      ↓
	 * boolean true
	 *
	 * log.report=true
	 *      ↓
	 * boolean true
	 *
	 * Final Output:
	 *
	 * EnvConfig
	 *      ↓
	 * Stored in ExecutionContext
	 *      ↓
	 * Accessible throughout framework
	 */
	private EnvConfig buildConfig(Properties properties) {

	  	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("buildConfig");

	    // ---------------------------------------------------------
	    // STEP 3.1
	    // Browser Configuration
	    // ---------------------------------------------------------

	    BrowserType browserType =
	            BrowserType.valueOf(
	                    properties.getProperty("browser")
	                            .trim()
	                            .toUpperCase());

	    boolean headless =
	            Boolean.parseBoolean(
	                    properties.getProperty("headless"));

	    String baseUrl =
	            properties.getProperty("baseUrl")
	                    .trim();

	    // ---------------------------------------------------------
	    // STEP 3.2
	    // Logging Configuration
	    // ---------------------------------------------------------

	    boolean logToConsole =
	            Boolean.parseBoolean(
	                    properties.getProperty(
	                            "log.console",
	                            "true"));

	    boolean logToReport =
	            Boolean.parseBoolean(
	                    properties.getProperty(
	                            "log.report",
	                            "true"));

	    boolean logElementActions =
	            Boolean.parseBoolean(
	                    properties.getProperty(
	                            "log.element.actions",
	                            "true"));

	    boolean logWaitActions =
	            Boolean.parseBoolean(
	                    properties.getProperty(
	                            "log.wait.actions",
	                            "true"));

	    // ---------------------------------------------------------
	    // STEP 3.3
	    // Reporting Configuration
	    // ---------------------------------------------------------

	    boolean screenshotOnFailure =
	            Boolean.parseBoolean(
	                    properties.getProperty(
	                            "screenshot.on.failure",
	                            "true"));

	    // ---------------------------------------------------------
	    // STEP 3.4
	    // Build Config Object
	    // ---------------------------------------------------------
	    //
	    // Using setter-based population.
	    //
	    // Advantage:
	    // - Easier future expansion
	    // - Avoids constructor explosion
	    // - Simpler maintenance
	    //
	    // ---------------------------------------------------------

	    EnvConfig config = new EnvConfig();

	    // Browser
	    config.setBrowserType(browserType);
	    config.setHeadless(headless);
	    config.setBaseUrl(baseUrl);

	    // Logging
	    config.setLogToConsole(logToConsole);
	    config.setLogToReport(logToReport);
	    config.setLogElementActions(logElementActions);
	    config.setLogWaitActions(logWaitActions);

	    // Reporting
	    config.setScreenshotOnFailure(screenshotOnFailure);

		TestLogger.logStep("config ->"+config);
		
	    return config;
	}

	// =========================================================================
	// UTILITY METHOD
	// =========================================================================

	/**
	 * Safe blank check utility.
	 *
	 * Covers: - null - "" - " "
	 */
	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}