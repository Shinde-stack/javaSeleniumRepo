package com.framework.core.excepions;

/**
 * DriverException
 *
 * Unchecked exception for browser driver creation or lifecycle failures.
 *
 * Flow:
 *   DriverFactory.createDriver() fails → DriverManager propagates → test aborts during onTestStart
 */
public class DriverException extends RuntimeException {
    public DriverException(String message) {
        super(message);
    }
}
