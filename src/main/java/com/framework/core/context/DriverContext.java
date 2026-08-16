package com.framework.core.context;

import org.openqa.selenium.WebDriver;

import com.framework.core.excepions.FrameworkException;

/**
 * DriverContext
 *
 * Holds the WebDriver instance for one test execution thread.
 *
 * Flow: DriverManager.initializeDriver() → setDriver() → pages/actions read via
 * ExecutionContext → quitDriver() on cleanup
 *
 * One DriverContext per ExecutionContext; never shared across threads.
 */
public class DriverContext {

	private WebDriver driver;

	public WebDriver getDriver() {

		if (driver == null) {

			throw new FrameworkException("WebDriver has not been initialized.");
		}

		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public boolean hasDriver() {
		return driver != null;
	}
}
