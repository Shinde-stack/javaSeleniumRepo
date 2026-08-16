package com.framework.web.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.core.excepions.ElementActionException;
import com.framework.core.logging.TestLogger;
import com.framework.web.waits.WaitManager;

/**
 * JsActions
 *
 * JavaScript-based fallback interactions when standard Selenium actions fail.
 *
 * Flow: stubborn element → JsActions.click/scrollTo → executeScript on located
 * element
 *
 * Not wired into ElementActions yet; use only as a secondary strategy, not the
 * default path.
 */
public class JsActions {

	private final JavascriptExecutor js;
	private final WebDriver driver;
	private final WaitManager waitManager;

	public JsActions(WebDriver driver, WaitManager waitManager) {
		this.driver = driver;
		this.js = (JavascriptExecutor) driver;
		this.waitManager = waitManager;

	}

	public void jsClick(By locator, String elementName) {

		try {
			TestLogger.logAction("(JS)Clicking on: " + elementName);

			WebElement element = waitManager.waitForClickable(locator, elementName);

			js.executeScript("arguments[0].click();", element);

			TestLogger.logStep("(JS) Clicked successfully: " + elementName);

		} catch (Exception e) {

			TestLogger.logFailure("(JS) Click failed: " + elementName, e);

			throw new ElementActionException("Failed to click element: " + elementName, e);
		}
	}

	public void scrollTo(By locator, String elementName) {

		try {
			TestLogger.logAction("Scroll to element: " + elementName);

			waitManager.waitForPageLoad();

			js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(locator));

			TestLogger.logStep("Scrolled to element: " + elementName);

		} catch (Exception e) {

			TestLogger.logFailure("Failed to scroll to element: " + elementName, e);

			throw new ElementActionException("Failed to scroll to element: " + elementName, e);
		}
	}

}
