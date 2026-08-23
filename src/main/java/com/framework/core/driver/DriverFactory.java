package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import com.framework.core.config.EnvConfig;
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

	public  WebDriver createDriver(EnvConfig config) {

		BrowserType browserType =config.getBrowserType();

		try {
			
			switch (browserType) {

			case CHROME:
				return new ChromeDriver(DriverOptionsBuilder.buildChromeOptions(config));

			case EDGE:
				return new EdgeDriver(DriverOptionsBuilder.buildEdgeOptions(config));

			default:
				throw new IllegalArgumentException("Unsupported browser: " + browserType);
			}

		} catch (Exception e) {

			throw new DriverException(
				    "Failed to create browser: "
				    + browserType +"Exception is -"+e);		}
	}

}
