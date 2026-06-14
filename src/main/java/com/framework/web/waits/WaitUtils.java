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
 *
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
 *
 * =============================================================================
 */
public class WaitUtils {

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
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
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

        WebDriverWait wait =
                new WebDriverWait(driver,
                        Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));

        return wait.until(
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

        WebDriverWait wait =
                new WebDriverWait(driver,
                        Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));

        return wait.until(
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

        WebDriverWait wait =
                new WebDriverWait(driver,
                        Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));

        return wait.until(
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

        WebDriverWait wait =
                new WebDriverWait(driver,
                        Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));

        return wait.until(
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

        WebDriverWait wait =
                new WebDriverWait(driver,
                        Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));

        wait.until(driver -> ((JavascriptExecutor) driver)
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

        WebDriverWait wait =
                new WebDriverWait(driver,
                        Duration.ofSeconds(timeoutInSeconds));

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }   
}
