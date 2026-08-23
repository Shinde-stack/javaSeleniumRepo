package com.framework.web.waits;

import java.time.Duration;
import java.util.function.Function;

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
 * Central explicit-wait utility; all synchronization should go through this
 * class.
 *
 * Flow: ElementActions/BasePage → waitForVisible/Clickable/Presence/etc. →
 * WebDriverWait + ExpectedConditions → return stable WebElement
 *
 * Timeout defaults to WaitConstants.EXPLICIT_WAIT_SECONDS for every wait.
 */
public class WaitManager {

	private final WebDriver driver;

	public WaitManager(WebDriver driver) {
		this.driver = driver;
	}

	private WebDriverWait createWait() {
		return new WebDriverWait(driver, Duration.ofSeconds(WaitConstants.EXPLICIT_WAIT_SECONDS));
	}

	public WebElement waitForVisible(By locator, String elementName) {

		TestLogger.logWait("Waiting for visibility: " + elementName);

		return createWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public WebElement waitForClickable(By locator, String elementName) {

		TestLogger.logWait("Waiting for clickable: " + elementName);

		return createWait().until(ExpectedConditions.elementToBeClickable(locator));
	}

	public WebElement waitForClickable(WebElement locator, String elementName) {

		TestLogger.logWait("Waiting for clickable: " + elementName);

		return createWait().until(ExpectedConditions.elementToBeClickable(locator));
	}

	public WebElement waitForPresence(By locator, String elementName) {

		TestLogger.logWait("Waiting for presence: " + elementName);

		return createWait().until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public boolean waitForInvisible(By locator, String elementName) {

		TestLogger.logWait("Waiting for invisible: " + elementName);

		return createWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/**
	 * Waits until document.readyState equals "complete".
	 */
	public void waitForPageLoad() {

		TestLogger.logWait("Waiting for pageloaded");

		createWait().until(
				driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
	}

	
	
	/**
	 * ============================================================================
	 * Generic Wait Engine
	 * ============================================================================
	 *
	 * This is the foundation of the WaitManager.
	 *
	 * Every specialized wait in this class ultimately delegates to this method.
	 *
	 * Generic Type
	 * ------------
	 * <T> allows this method to return any object type:
	 *
	 *      WebElement
	 *      Boolean
	 *      Alert
	 *      String
	 *      List<WebElement>
	 *
	 * depending on the ExpectedCondition supplied.
	 *
	 * Function<WebDriver, T>
	 * ----------------------
	 * Represents a lambda that receives the current WebDriver
	 * and returns the object being waited for.
	 *
	 * Example:
	 *
	 * waitUntil(driver ->
	 *      ExpectedConditions
	 *          .visibilityOfElementLocated(locator)
	 *          .apply(driver));
	 *
	 * ============================================================================
	 */
	public <T> T waitUntil(
	        Function<WebDriver, T> condition) {

	    return createWait().until(condition);
	}
}
