package com.framework.core.driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * DriverOptionsBuilder
 *
 * Builds browser-specific option objects (headless, window size, CI flags).
 *
 * Flow:
 *   DriverFactory.createDriver() → buildChromeOptions/buildEdgeOptions(headless) → passed to WebDriver constructor
 *
 * Keeps browser argument tuning out of DriverFactory for easier Grid/Docker customization later.
 */
public final class DriverOptionsBuilder {

    private DriverOptionsBuilder() {
    }

    public static ChromeOptions buildChromeOptions(boolean headless) {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        if (headless) {
            options.addArguments("--headless=new");
        }

        return options;
    }

    public static EdgeOptions buildEdgeOptions(boolean headless) {

        EdgeOptions options = new EdgeOptions();

        options.addArguments("--start-maximized");

        if (headless) {
            options.addArguments("--headless=new");
        }

        return options;
    }    
}
