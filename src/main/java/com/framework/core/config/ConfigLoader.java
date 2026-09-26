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
 * ============================================================================
 * Class Name : ConfigLoader
 * ============================================================================
 *
 * PURPOSE
 * -------
 * Loads environment-specific configuration and converts it into a typed
 * EnvConfig object.
 *
 * FLOW
 * ----
 *
 * load()
 *   |
 *   +--> resolveEnvironment()
 *   |
 *   +--> loadProperties()
 *   |
 *   +--> validateProperties()
 *   |
 *   +--> buildConfig()
 *
 * RESPONSIBILITIES
 * ---------------
 * - Resolve active environment
 * - Load environment properties
 * - Validate mandatory properties
 * - Convert String properties into typed values
 * - Validate base URL
 * - Build EnvConfig
 *
 * DOES NOT
 * --------
 * - Create WebDriver
 * - Manage ExecutionContext
 * - Manage TestNG
 * - Manage reporting
 *
 * ============================================================================
 */
public class ConfigLoader {

    private static final List<String> REQUIRED_PROPERTIES =
            ConfigConstants.MANDATORY_PROPERTIES;

    /**
     * Loads the active environment configuration.
     *
     * @return typed EnvConfig
     */
    public EnvConfig load() {

        /*
         * 1. Resolve active environment.
         */
        EnvironmentType environment =
                resolveEnvironment();

        /*
         * 2. Load corresponding properties file.
         */
        Properties properties =
                loadProperties(environment);

        /*
         * 3. Validate mandatory properties.
         */
        validateProperties(properties);

        /*
         * 4. Convert properties into typed configuration.
         */
        return buildConfig(
                properties,
                environment);
    }

    // =========================================================================
    // ENVIRONMENT
    // =========================================================================

    /**
     * Resolves active environment.
     *
     * Priority:
     *
     * 1. JVM system property:
     *      -Denv=qa
     *
     * 2. OS / CI environment variable:
     *      ENV=qa
     *
     * 3. Framework default:
     *      ConfigConstants.DEFAULT_FALLBACK_ENV
     *
     * @return resolved EnvironmentType
     */
    private EnvironmentType resolveEnvironment() {

        /*
         * Priority 1:
         * JVM system property.
         *
         * Example:
         * mvn test -Denv=qa
         */
        String env =
                System.getProperty("env");

        /*
         * Priority 2:
         * OS / CI environment variable.
         *
         * Example:
         * ENV=uat
         */
        if (isBlank(env)) {

            env = System.getenv("ENV");
        }

        /*
         * Priority 3:
         * Framework default.
         */
        if (isBlank(env)) {

            env =
                    ConfigConstants.DEFAULT_FALLBACK_ENV;
        }

        /*
         * Convert String -> typed enum.
         *
         * EnvironmentType.from() is responsible for validating
         * whether the supplied environment is supported.
         */
        return EnvironmentType.from(env);
    }

    // =========================================================================
    // PROPERTY FILE
    // =========================================================================

    /**
     * Loads the properties file for the selected environment.
     *
     * Example:
     *
     * EnvironmentType.QA
     *      ↓
     * config/qa.properties
     *
     * @param environment active environment
     * @return loaded properties
     */
    private Properties loadProperties(
            EnvironmentType environment) {

        String file =
                ConfigConstants.CONFIG_DIRECTORY
                + environment.getConfigFileName();

        Properties properties =
                new Properties();

        try (InputStream input =
                     getClass()
                         .getClassLoader()
                         .getResourceAsStream(file)) {

            if (input == null) {

                throw new FrameworkException(
                        "Configuration file not found: "
                        + file);
            }

            properties.load(input);

        } catch (IOException e) {

            throw new FrameworkException(
                    "Failed to read configuration file: "
                    + file,
                    e);
        }

        return properties;
    }

    // =========================================================================
    // VALIDATION
    // =========================================================================

    /**
     * Validates mandatory configuration properties.
     *
     * All missing properties are collected first.
     *
     * Example:
     *
     * browser =
     * baseUrl =
     *
     * Result:
     *
     * Config validation failed.
     * Missing properties: browser, baseUrl
     */
    private void validateProperties(
            Properties properties) {

        List<String> missingProperties =
                new ArrayList<>();

        for (String key : REQUIRED_PROPERTIES) {

            String value =
                    properties.getProperty(key);

            if (isBlank(value)) {

                missingProperties.add(key);
            }
        }

        if (!missingProperties.isEmpty()) {

            throw new FrameworkException(
                    "Config validation failed. "
                    + "Missing properties: "
                    + String.join(
                            ", ",
                            missingProperties));
        }
    }

    // =========================================================================
    // BUILD CONFIGURATION
    // =========================================================================

    /**
     * Converts raw properties into typed EnvConfig.
     */
    private EnvConfig buildConfig(
            Properties properties,
            EnvironmentType environment) {

        /*
         * Browser.
         */
        BrowserType browserType =
                BrowserType.from(
                        properties.getProperty("browser"));

        /*
         * URL.
         */
        String baseUrl =
                getBaseUrl(
                        properties,
                        "baseUrl");

        /*
         * Boolean configuration.
         */
        boolean headless =
                getBoolean(
                        properties,
                        "headless",
                        false);

        boolean logToConsole =
                getBoolean(
                        properties,
                        "log.console",
                        true);

        boolean logToReport =
                getBoolean(
                        properties,
                        "log.report",
                        true);

        boolean logElementActions =
                getBoolean(
                        properties,
                        "log.element.actions",
                        true);

        boolean logWaitActions =
                getBoolean(
                        properties,
                        "log.wait.actions",
                        true);

        boolean screenshotOnFailure =
                getBoolean(
                        properties,
                        "screenshot.on.failure",
                        true);

        /*
         * Build typed configuration object.
         */
        EnvConfig config =
                new EnvConfig();

        config.setEnvironmentType(
                environment);

        config.setBrowserType(
                browserType);

        config.setHeadless(
                headless);

        config.setBaseUrl(
                baseUrl);

        config.setLogToConsole(
                logToConsole);

        config.setLogToReport(
                logToReport);

        config.setLogElementActions(
                logElementActions);

        config.setLogWaitActions(
                logWaitActions);

        config.setScreenshotOnFailure(
                screenshotOnFailure);

        return config;
    }

    // =========================================================================
    // BOOLEAN
    // =========================================================================

    /**
     * Reads a boolean property.
     *
     * Missing property:
     *      -> supplied default value
     *
     * true / false:
     *      -> accepted case-insensitively
     *
     * Anything else:
     *      -> FrameworkException
     */
    private boolean getBoolean(
            Properties properties,
            String key,
            boolean defaultValue) {

        String value =
                properties.getProperty(key);

        if (isBlank(value)) {

            return defaultValue;
        }

        value =
                value.trim();

        if ("true".equalsIgnoreCase(value)) {

            return true;
        }

        if ("false".equalsIgnoreCase(value)) {

            return false;
        }

        throw new FrameworkException(
                "Invalid boolean value for property '"
                + key
                + "': '"
                + value
                + "'. Expected 'true' or 'false'.");
    }

    // =========================================================================
    // URL
    // =========================================================================

    /**
     * Validates configured application URL.
     *
     * This validates URL syntax/configuration.
     *
     * It does NOT check whether the application is reachable.
     */
    private String getBaseUrl(
            Properties properties,
            String key) {

        String value =
                properties.getProperty(key);

        if (isBlank(value)) {

            throw new FrameworkException(
                    "Configuration property '"
                    + key
                    + "' cannot be null or blank.");
        }

        value =
                value.trim();

        try {

            URI uri =
                    new URI(value);

            String scheme =
                    uri.getScheme();

            /*
             * Only HTTP/HTTPS are allowed.
             */
            if (scheme == null
                    || !(scheme.equalsIgnoreCase("http")
                    || scheme.equalsIgnoreCase("https"))) {

                throw new FrameworkException(
                        "Invalid URL scheme for property '"
                        + key
                        + "'. Only HTTP and HTTPS are supported.");
            }

            /*
             * Host must exist.
             */
            if (uri.getHost() == null
                    || uri.getHost().isBlank()) {

                throw new FrameworkException(
                        "Invalid URL for property '"
                        + key
                        + "'. Host name is missing.");
            }

            return value;

        } catch (URISyntaxException e) {

            throw new FrameworkException(
                    "Invalid URL syntax for property '"
                    + key
                    + "': "
                    + value,
                    e);
        }
    }

    // =========================================================================
    // UTILITY
    // =========================================================================

    private boolean isBlank(String text) {

        return text == null
                || text.isBlank();
    }
}