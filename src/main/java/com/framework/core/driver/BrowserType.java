package com.framework.core.driver;

import java.util.Arrays;

import com.framework.core.excepions.FrameworkException;

/**
 * BrowserType
 *
 * Supported browser identifiers used during driver creation.
 *
 * Flow:
 * properties file
 *      ↓
 * ConfigLoader
 *      ↓
 * BrowserType.from()
 *      ↓
 * EnvConfig
 *      ↓
 * DriverManager
 *      ↓
 * DriverFactory
 */
public enum BrowserType {

    CHROME,
    EDGE;

    /**
     * Converts an external browser value into BrowserType.
     *
     * Examples:
     * "chrome" -> CHROME
     * "Chrome" -> CHROME
     * "CHROME" -> CHROME
     * "edge"   -> EDGE
     */
    public static BrowserType from(String browser) {

        if (browser == null || browser.isBlank()) {
            throw new FrameworkException(
                    "Browser cannot be null or blank.");
        }

        String normalizedBrowser = browser.trim();

        return Arrays.stream(values())
                .filter(type ->
                        type.name().equalsIgnoreCase(normalizedBrowser))
                .findFirst()
                .orElseThrow(() ->
                        new FrameworkException(
                                "Unsupported browser: '"
                                + browser
                                + "'. Supported values: "
                                + Arrays.toString(values())));
    }
}