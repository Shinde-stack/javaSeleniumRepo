package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import com.framework.core.excepions.DriverException;

/**
 * DriverFactory
 *
 * Creates browser-specific WebDriver instances.
 *
 * Flow:
 *   DriverManager → createDriver(browserType, headless) → DriverOptionsBuilder → new ChromeDriver/EdgeDriver
 *
 * Stateless factory: does not store, quit, or bind drivers to ExecutionContext.
 */
public class DriverFactory {

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
