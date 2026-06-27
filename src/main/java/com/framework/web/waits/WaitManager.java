package com.framework.web.waits;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.core.constants.WaitConstants;
import com.framework.core.driver.DriverManager;
import com.framework.core.logging.TestLogger;

/**
 * =============================================================================
 * Class Name : WaitUtils
 * =============================================================================
 * Responsibility:
 * Centralized explicit wait utility for framework.
 *
 * Why this class exists:
 * - Avoid hardcoded waits (Thread.sleep)
 * - Avoid duplicating WebDriverWait logic everywhere
 * - Improve maintainability
 * - Provide consistent synchronization strategy
 *
 * Framework Rule:
 * All waits should go through this class.
 *
 * Example:
 *
 * waitUtils.waitForVisible(loginButton);
 * waitUtils.waitForClickable(submitButton);
 * =============================================================================
 */

public class WaitManager {

    /**
     * Default explicit wait timeout.
     *
     * Future Improvement:
     * Read from framework configuration file. -----------------------------------
     */

    private final WebDriver driver;

    /**
     * Constructor
     *
     * @param driver Active WebDriver instance
     */
    public WaitManager(WebDriver driver) {
        this.driver = driver;
    }
    
    
    private WebDriverWait createWait() {
        return new WebDriverWait(
                driver,
                Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));
    }
    

    /**
     * Wait until element becomes visible.
     *
     * Use when:
     * - Element should appear on screen
     * - Before reading text
     * - Before entering data
     *
     * @param locator Element locator
     * @return Visible WebElement
     */
    public WebElement waitForVisible(By locator) {

        TestLogger.logWait(
                "Waiting for visibility: " + locator);

        return createWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until element becomes clickable.
     *
     * Use when:
     * - Element must be clicked
     * - Avoid click interception issues
     *
     * @param locator Element locator
     * @return Clickable WebElement
     */
    public WebElement waitForClickable(By locator) {

        TestLogger.logWait(
                "Waiting for clickable: " + locator);

        return createWait().until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until element exists in DOM.
     *
     * Element may not yet be visible.
     *
     * Useful for:
     * - Lazy loaded elements
     * - Dynamic content
     *
     * @param locator Element locator
     * @return Located WebElement
     */
    public WebElement waitForPresence(By locator) {

        TestLogger.logWait(
                "Waiting for presence: " + locator);

        return createWait().until(
                ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until element disappears.
     *
     * Useful for:
     * - Loader/spinner
     * - Progress bars
     * - Success/error messages
     *
     * @param locator Element locator
     * @return true if element disappeared
     */
    public boolean waitForInvisible(By locator) {

        TestLogger.logWait(
                "Waiting for invisible: " + locator);

        return createWait().until(
                ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until page loading completes.
     *
     * Uses:
     * document.readyState == complete
     *
     * Useful after:
     * - Page navigation
     * - Refresh
     * - Redirect
     */
    public void waitForPageLoad() {

		TestLogger.logStep("web-> waits -> WaitsUtils ->waitForPageLoad === EXPLICIT_WAIT_SECONDS ="+WaitConstants.EXPLICIT_WAIT_SECONDS);

        TestLogger.logWait(
                "Waiting for pageloaded");

        createWait().until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState")
                .equals("complete"));
    }

    /**
     * Generic custom timeout visibility wait.
     *
     * Use when:
     * Specific operation needs longer/shorter timeout.
     *
     * Example:
     * waitForVisible(locator, 30);
     *
     * @param locator Element locator
     * @param timeoutInSeconds Custom timeout
     * @return Visible WebElement
     */
    public WebElement waitForVisible(By locator,
                                     int timeoutInSeconds) {
        TestLogger.logWait(
                "Waiting for visible: " + locator);

        return createWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    } 
    
    // ------------------------------------------------------------------------
    // FUTURE EXTENSION NOTE
    // ------------------------------------------------------------------------
    /*
     * Future improvements:
     * --------------------
     * - FluentWait support (polling, ignore exceptions)
     * - Custom retry mechanism
     * - Smart wait (JS + network idle detection)
     * - Config-based timeout per page/module
     * - Element state caching for performance
     */
    
}
