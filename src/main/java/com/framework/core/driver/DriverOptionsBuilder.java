package com.framework.core.driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverOptionsBuilder {

	
	
	  public static ChromeOptions getChromeOptions() {
	        ChromeOptions options = new ChromeOptions();

	        options.addArguments("--start-maximized");
	        options.addArguments("--disable-notifications");

	        return options;
	    }

	    public static EdgeOptions getEdgeOptions() {
	        EdgeOptions options = new EdgeOptions();

	        options.addArguments("--start-maximized");

	        return options;
	    }
	    
	    
}
