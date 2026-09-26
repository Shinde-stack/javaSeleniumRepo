package com.framework.core.config;

import com.framework.core.driver.BrowserType;
import com.framework.core.enums.EnvironmentType;

/**
 * ============================================================================
 * Class Name : EnvConfig
 * ============================================================================
 *
 * Purpose:
 * --------
 * Strongly typed runtime configuration loaded by ConfigLoader.
 *
 * Flow:
 *
 * ConfigLoader.load()
 *        ↓
 *     EnvConfig
 *        ↓
 * ExecutionContext.setConfig()
 *        ↓
 * Driver / Logging / Reporting / Framework
 *
 * Design:
 * -------
 * - Configuration is loaded once for an execution.
 * - Tests and framework components only READ configuration.
 * - ConfigLoader is responsible for POPULATING configuration.
 *
 * Note:
 * -----
 * Setters are currently kept because ConfigLoader builds this object
 * property-by-property.
 *
 * Future improvement:
 * -------------------
 * Convert this class to an immutable configuration object/record once
 * configuration loading is stable.
 *
 * ============================================================================
 */
public class EnvConfig {

    // =========================================================================
    // BROWSER CONFIGURATION
    // =========================================================================

    private BrowserType browserType;

    private boolean headless;

    private String baseUrl;

    // =========================================================================
    // LOGGING CONFIGURATION
    // =========================================================================

    private boolean logToConsole;

    private boolean logToReport;

    private boolean logElementActions;

    private boolean logWaitActions;

    // =========================================================================
    // SCREENSHOT CONFIGURATION
    // =========================================================================

    private boolean screenshotOnFailure;

	private EnvironmentType environment;


    // =========================================================================
    // GETTERS
    // =========================================================================

    public BrowserType getBrowserType() {
        return browserType;
    }

    public boolean isHeadless() {
        return headless;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isLogToConsole() {
        return logToConsole;
    }

    public boolean isLogToReport() {
        return logToReport;
    }

    public boolean isLogElementActions() {
        return logElementActions;
    }

    public boolean isLogWaitActions() {
        return logWaitActions;
    }

    public boolean isScreenshotOnFailure() {
        return screenshotOnFailure;
    }


    // =========================================================================
    // SETTERS
    // =========================================================================
    //
    // Used by ConfigLoader while constructing the configuration.
    //

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }

    public void setLogToReport(boolean logToReport) {
        this.logToReport = logToReport;
    }

    public void setLogElementActions(boolean logElementActions) {
        this.logElementActions = logElementActions;
    }

    public void setLogWaitActions(boolean logWaitActions) {
        this.logWaitActions = logWaitActions;
    }

    public void setScreenshotOnFailure(boolean screenshotOnFailure) {
        this.screenshotOnFailure = screenshotOnFailure;
    }

 // ========================================================================
 // ENVIRONMENT
 // ========================================================================

 public EnvironmentType getEnvironmentType() {
     return environment;
 }

 public void setEnvironmentType(EnvironmentType environmentType) {
     this.environment = environmentType;
 }

    // =========================================================================
    // DEBUG / LOGGING
    // =========================================================================

    @Override
    public String toString() {

        return "EnvConfig{" +
                "browserType=" + browserType +
                ", headless=" + headless +
                ", baseUrl='" + baseUrl + '\'' +
                ", logToConsole=" + logToConsole +
                ", logToReport=" + logToReport +
                ", logElementActions=" + logElementActions +
                ", logWaitActions=" + logWaitActions +
                ", screenshotOnFailure=" + screenshotOnFailure +
                '}';
    }
}