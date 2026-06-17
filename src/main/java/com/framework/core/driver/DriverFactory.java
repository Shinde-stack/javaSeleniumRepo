package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import com.framework.core.excepions.DriverException;

/**
 * DriverFactory
 *
 * Responsibility: ---------------- Create browser driver instances.
 *
 * IMPORTANT: ---------- DriverFactory DOES NOT: - Store drivers - Manage driver
 * lifecycle - Quit drivers - Interact with context
 *
 * Those responsibilities belong to DriverManager.
 *
 * Design Principle: ----------------- Single Responsibility Principle (SRP)
 */
public class DriverFactory {

	/**
	 * Creates browser driver.
	 *
	 * @param browserType Browser to launch
	 * @param headless    Headless execution flag
	 * @return WebDriver instance
	 */

	public static WebDriver createDriver(BrowserType browserType, boolean headless) {

		try {

			switch (browserType) {

			case CHROME:
				return new ChromeDriver(DriverOptionsBuilder.buildChromeOptions(headless));

			case EDGE:
				return new EdgeDriver(DriverOptionsBuilder.buildEdgeOptions(headless));

			default:
				throw new IllegalArgumentException("Unsupported browser: " + browserType);
			}

		} catch (Exception ex) {

			throw new DriverException("Failed to create browser: " + browserType + " - " + ex);
		}
	}

}
