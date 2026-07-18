package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.framework.core.context.ExecutionContext;
import com.framework.core.excepions.DriverException;

/**
 * DriverManager
 *
 * Manages WebDriver lifecycle for a single test execution.
 *
 * Flow:
 *   ContextLifecycleManager.initDriver() → createDriver via factory → store in DriverContext → quit on cleanup
 *
 * Does not create browser options or read configuration; receives BrowserType from ExecutionContext config.
 */
public class DriverManager {

	/**
	 * Creates a driver and stores it in the current thread's DriverContext.
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
	 * Quits the browser and clears the driver reference so the next test starts clean.
	 */
	public void quitDriver(ExecutionContext context) {
		if (context != null) {
			try {
				context.getDriverContext().quitDriver();
			} finally {
				context.getDriverContext().setDriver(null);
			}
		}
	}

}
