package com.framework.core.driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * DriverOptionsBuilder
 *
 * Responsibility:
 * ----------------
 * Creates and configures browser-specific option objects.
 *
 * Why separate from DriverFactory?
 * --------------------------------
 * DriverFactory should only create drivers.
 *
 * Browser-specific settings such as:
 * - headless mode
 * - notifications
 * - startup arguments
 * - browser preferences
 *
 * should be maintained separately.
 *
 * Benefits:
 * ---------
 * 1. Cleaner DriverFactory
 * 2. Easier browser customization
 * 3. Easier future Grid / Docker support
 * 4. Single place to manage browser arguments
 *
 * Future Upgrade:
 * ---------------
 * Move browser arguments to external config.
 */
public final class DriverOptionsBuilder {

    /**
     * Utility class.
     * Prevent object creation.
     */
    private DriverOptionsBuilder() {
    }

    /**
     * Creates Chrome options.
     *
     * @param headless Run browser in headless mode.
     * @return Configured ChromeOptions
     */
    public static ChromeOptions buildChromeOptions(boolean headless) {

        ChromeOptions options = new ChromeOptions();

        // Open browser maximized
        options.addArguments("--start-maximized");

        // Disable notification popups
        options.addArguments("--disable-notifications");

        // Better stability in CI/CD environments
        options.addArguments("--disable-dev-shm-usage");

        // Required in many Docker/Linux environments
        options.addArguments("--no-sandbox");

        if (headless) {
            options.addArguments("--headless=new");
        }

        return options;
    }

    /**
     * Creates Edge options.
     *
     * @param headless Run browser in headless mode.
     * @return Configured EdgeOptions
     */
    public static EdgeOptions buildEdgeOptions(boolean headless) {

        EdgeOptions options = new EdgeOptions();

        options.addArguments("--start-maximized");

        if (headless) {
            options.addArguments("--headless=new");
        }

        return options;
    }    
}
