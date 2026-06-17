package com.framework.core.config;

import com.framework.core.driver.BrowserType;

/**
 * Runtime configuration loaded from properties file.
 *
 * Populated by:
 * ConfigLoader
 *
 * Used by:
 * BaseTest
 * DriverManager
 * Logger
 * Reporting
 * Screenshot Service
 */
public class EnvConfig {

    // ---------------------------------------------------------
    // Browser Configuration
    // ---------------------------------------------------------

    private BrowserType browserType;

    private boolean headless;

    private String baseUrl;

    // ---------------------------------------------------------
    // Logging Configuration
    // ---------------------------------------------------------

    private boolean logToConsole;

    private boolean logToReport;

    private boolean logElementActions;

    private boolean logWaitActions;

    // ---------------------------------------------------------
    // Reporting Configuration
    // ---------------------------------------------------------

    private boolean screenshotOnFailure;

    // ---------------------------------------------------------
    // Getters / Setters
    // ---------------------------------------------------------

    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isLogToConsole() {
        return logToConsole;
    }

    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }

    public boolean isLogToReport() {
        return logToReport;
    }

    public void setLogToReport(boolean logToReport) {
        this.logToReport = logToReport;
    }

    public boolean isLogElementActions() {
        return logElementActions;
    }

    public void setLogElementActions(boolean logElementActions) {
        this.logElementActions = logElementActions;
    }

    public boolean isLogWaitActions() {
        return logWaitActions;
    }

    public void setLogWaitActions(boolean logWaitActions) {
        this.logWaitActions = logWaitActions;
    }

    public boolean isScreenshotOnFailure() {
        return screenshotOnFailure;
    }

    public void setScreenshotOnFailure(boolean screenshotOnFailure) {
        this.screenshotOnFailure = screenshotOnFailure;
    }

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