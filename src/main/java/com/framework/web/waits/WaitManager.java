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
 * WaitManager
 *
 * Central explicit-wait utility; all synchronization should go through this class.
 *
 * Flow:
 *   ElementActions/BasePage → waitForVisible/Clickable/Presence/etc.
 *   → WebDriverWait + ExpectedConditions → return stable WebElement
 *
 * Timeout defaults to WaitConstants.EXPLICIT_WAIT_SECONDS for every wait.
 */
public class WaitManager {

    private final WebDriver driver;

    public WaitManager(WebDriver driver) {
        this.driver = driver;
    }
    
    
    private WebDriverWait createWait() {
        return new WebDriverWait(
                driver,
                Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));
    }
    

    public WebElement waitForVisible(By locator) {

        TestLogger.logWait(
                "Waiting for visibility: " + locator);

        return createWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {

        TestLogger.logWait(
                "Waiting for clickable: " + locator);

        return createWait().until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForPresence(By locator) {

        TestLogger.logWait(
                "Waiting for presence: " + locator);

        return createWait().until(
                ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean waitForInvisible(By locator) {

        TestLogger.logWait(
                "Waiting for invisible: " + locator);

        return createWait().until(
                ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until document.readyState equals "complete".
     */
    public void waitForPageLoad() {

		TestLogger.logStep("web-> waits -> WaitsUtils ->waitForPageLoad === EXPLICIT_WAIT_SECONDS ="+WaitConstants.EXPLICIT_WAIT_SECONDS);

        TestLogger.logWait(
                "Waiting for pageloaded");

        createWait().until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState")
                .equals("complete"));
    }

    public WebElement waitForVisible(By locator,
                                     int timeoutInSeconds) {
        TestLogger.logWait(
                "Waiting for visible: " + locator);

        return createWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    } 
    
}
