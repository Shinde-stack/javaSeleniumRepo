package com.framework.core.driver;

/**
 * BrowserType
 *
 * Supported browser identifiers used during driver creation.
 *
 * Flow:
 *   properties file (browser=chrome) → ConfigLoader → EnvConfig → DriverManager → DriverFactory
 *
 * Extend this enum when adding FIREFOX, SAFARI, or remote/grid browsers.
 */
public enum BrowserType {

    CHROME,

    EDGE
}
