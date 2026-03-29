package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverManager {

	
	
	  // Thread-safe driver
	  private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	    public static void initDriver(String browser) {

	        if (driver.get() == null) {
	            WebDriver webDriver = WebDriverFactory.createDriver(browser);
	            driver.set(webDriver);
	        }
	    }

	    public static WebDriver getDriver() {
	        return driver.get();
	    }

	    public static void quitDriver() {
	        if (driver.get() != null) {
	            driver.get().quit();
	            driver.remove();
	        }
	    }
    
    
}
