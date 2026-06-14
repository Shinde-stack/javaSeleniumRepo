package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.framework.core.config.ExecutionContext;

/**
 * DriverManager
 *
 * Responsibility: ---------------- Manage WebDriver lifecycle.
 *
 * Responsibilities: ----------------- Initialize driver Store driver in
 * ExecutionContext Retrieve driver Close driver
 *
 * Architecture: -------------
 *
 * Test ↓ BaseTest ↓ DriverManager ↓ DriverFactory ↓ Browser Driver
 *
 */
public class DriverManager {

//	private final DriverFactory driverFactory;
//
//	public DriverManager() {
//		this.driverFactory = new DriverFactory();
//	}

	/**
	 * Creates and stores driver inside current ExecutionContext.
	 *
	 * @param context     Current execution context
	 * @param browserType Browser type
	 * @param headless    Headless mode flag
	 */
	public void initializeDriver(ExecutionContext context, BrowserType browserType, boolean headless) {

		WebDriver driver = DriverFactory.createDriver(browserType, headless);

		context.driver().setDriver(driver);
	}

	/**
	 * Returns current WebDriver.
	 *
	 * @param context Current execution context
	 * @return Active WebDriver
	 */
	public WebDriver getDriver(ExecutionContext context) {

		return context.driver().getDriver();
	}

	/**
	 * Safely closes browser.
	 *
	 * @param context Current execution context
	 */
	public void quitDriver(ExecutionContext context) {
		// Check if context and driver sub-context reference are active
		if (context != null && context.driver() != null) {
			try {
				// Closes native browser application processes
				context.driver().quitDriver();
			} finally {
				// CRITICAL: Wipe reference so next test block on this thread starts fresh
				context.driver().setDriver(null);
			}
		}
	}

}
