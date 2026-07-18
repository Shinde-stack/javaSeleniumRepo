package com.framework.core.context;

import org.openqa.selenium.WebDriver;

/**
 * DriverContext
 *
 * Holds the WebDriver instance for one test execution thread.
 *
 * Flow:
 *   DriverManager.initializeDriver() → setDriver() → pages/actions read via ExecutionContext → quitDriver() on cleanup
 *
 * One DriverContext per ExecutionContext; never shared across threads.
 */
public class DriverContext {

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Closes the browser session and clears the reference.
     */
    public void quitDriver() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
