package com.framework.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class WebDriverFactory {

	
	

    public static WebDriver createDriver(String browser) {

        switch (browser.toLowerCase()) {

            case "chrome":
                return new ChromeDriver(
                        DriverOptionsBuilder.getChromeOptions()
                );

            case "edge":
                return new EdgeDriver(
                        DriverOptionsBuilder.getEdgeOptions()
                );

            default:
                throw new RuntimeException("Invalid browser: " + browser);
        }
    }
    
}
