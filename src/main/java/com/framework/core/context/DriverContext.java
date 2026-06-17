package com.framework.core.context;

import org.openqa.selenium.WebDriver;

/**
 * Holds WebDriver instance for the current execution.
 *
 * Responsibility:
 * - Store browser session
 * - Provide browser access
 * - Handle browser cleanup
 *
 * IMPORTANT:
 * One DriverContext belongs to one test execution.
 */
public class DriverContext {

    private WebDriver driver;

    /**
     * Returns current WebDriver instance.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Stores WebDriver instance.
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Safely closes browser session.
     */
    public void quitDriver() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
