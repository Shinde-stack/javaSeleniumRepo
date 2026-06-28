package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.framework.core.context.ExecutionContext;
import com.framework.core.excepions.DriverException;

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

	/**
	 * Creates and stores driver inside current ExecutionContext.
	 *
	 * @param context     Current execution context
	 * @param browserType Browser type
	 * @param headless    Headless mode flag
	 */
	public void initializeDriver(ExecutionContext context, BrowserType browserType, boolean headless) {

		WebDriver driver =
		        DriverFactory.createDriver(browserType, headless);

		if (driver == null) {
		    throw new DriverException(
		            "Failed to create driver: " + browserType);
		}

		context.getDriverContext().setDriver(driver);
	}

	/**
	 * Safely closes browser.
	 *
	 * @param context Current execution context
	 */
	public void quitDriver(ExecutionContext context) {
		// Check if context and driver sub-context reference are active
		if (context != null) {
			try {
				// Closes native browser application processes
				context.getDriverContext().quitDriver();
			} finally {
				// CRITICAL: Wipe reference so next test block on this thread starts fresh
				context.getDriverContext().setDriver(null);
			}
		}
	}

}
