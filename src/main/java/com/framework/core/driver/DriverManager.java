package com.framework.core.driver;

import org.openqa.selenium.WebDriver;

import com.framework.core.config.EnvConfig;
import com.framework.core.context.DriverContext;
import com.framework.core.context.ExecutionContext;
import com.framework.core.excepions.DriverException;

/**
 * DriverManager
 *
 * Manages the complete WebDriver lifecycle for a single test execution.
 *
 * Responsibilities: - Create WebDriver - Store WebDriver in DriverContext -
 * Provide access to current WebDriver - Quit WebDriver - Clear DriverContext
 * during cleanup
 *
 * Does NOT: - Build browser options - Decide browser configuration - Read
 * properties files
 */
public class DriverManager {


	private final DriverFactory driverFactory;

	public DriverManager(DriverFactory driverFactory) {
	    this.driverFactory = driverFactory;
	}
	
	/**
	 * Creates a WebDriver and stores it in the supplied ExecutionContext.
	 */
	public void initializeDriver(ExecutionContext context) {

		if (context == null) {
			throw new IllegalArgumentException("ExecutionContext cannot be null.");
		}

		EnvConfig config = context.getConfig();

		WebDriver driver = driverFactory.createDriver(config);

		context.getDriverContext().setDriver(driver);
	}

	/**
	 * Returns the current WebDriver.
	 */
	public WebDriver getDriver(ExecutionContext context) {

		if (context == null) {
			throw new IllegalArgumentException("ExecutionContext cannot be null.");
		}

		return context.getDriverContext().getDriver();
	}

	/**
	 * Returns true if a WebDriver has been initialized.
	 */
	public boolean hasDriver(ExecutionContext context) {

		if (context == null) {
			return false;
		}

		return context.getDriverContext().hasDriver();
	}

	/**
	 * Quits the browser and clears the DriverContext.
	 *
	 * Cleanup is best-effort. Driver reference is always removed even if
	 * driver.quit() throws an exception.
	 */
	public void quitDriver(ExecutionContext context) {

		if (context == null) {
			return;
		}

		DriverContext driverContext = context.getDriverContext();

		if (!driverContext.hasDriver()) {
			return;
		}

		WebDriver driver = driverContext.getDriver();

		try {

			driver.quit();

		} catch (Exception ex) {

			throw new DriverException("Failed to quit WebDriver. " + ex);

		} finally {

			driverContext.setDriver(null);
		}
	}
}