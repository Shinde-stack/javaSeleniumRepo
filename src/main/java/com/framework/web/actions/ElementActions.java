package com.framework.web.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.core.logging.TestLogger;
import com.framework.web.exceptions.ElementActionException;
import com.framework.web.waits.WaitManager;

/**
 * ElementActions
 *
 * Primary UI interaction layer for page objects. Wraps waits, logging, and exception translation.
 *
 * Flow:
 *   page method → ElementActions.click/sendKeys/getText → WaitManager stabilizes element
 *   → Selenium action → log success, or catch and throw ElementActionException
 *
 * Page classes must not call WebDriver directly; use this class instead.
 */
public class ElementActions {

    private final WebDriver driver;
    private final WaitManager waitManager;

    public ElementActions(WebDriver driver, WaitManager waitManager) {
        this.driver = driver;
        this.waitManager = waitManager;
    }

    public void click(By locator, String elementName) {

        try {
            TestLogger.logAction("Clicking on: " + elementName);

            WebElement element = waitManager.waitForClickable(locator);

            element.click();

            TestLogger.logStep("Clicked successfully: " + elementName);

        } catch (Exception e) {

            TestLogger.logFailure("Click failed: " + elementName, e);

            throw new ElementActionException(
                    "Failed to click element: " + elementName, e);
        }
    }

    public void sendKeys(By locator, String value, String elementName) {

        try {
            TestLogger.logAction("Entering value in: " + elementName);

            WebElement element = waitManager.waitForVisible(locator);

            element.clear();
            element.sendKeys(value);

            TestLogger.logStep("Value entered in: " + elementName);

        } catch (Exception e) {

            TestLogger.logFailure("SendKeys failed: " + elementName, e);

            throw new ElementActionException(
                    "Failed to enter value in: " + elementName, e);
        }
    }

    public String getText(By locator, String elementName) {

        try {
            TestLogger.logAction("Getting text from: " + elementName);

            WebElement element = waitManager.waitForVisible(locator);

            String text = element.getText();

            TestLogger.logStep("Text retrieved from: " + elementName);

            return text;

        } catch (Exception e) {

            TestLogger.logFailure("GetText failed: " + elementName, e);

            throw new ElementActionException(
                    "Failed to get text from: " + elementName, e);
        }
    }

    public void scrollIntoView(By locator, String elementName) {

        try {
            TestLogger.logAction("Scrolling to: " + elementName);

            WebElement element = waitManager.waitForPresence(locator);

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", element);

            TestLogger.logStep("Scrolled to: " + elementName);

        } catch (Exception e) {

            TestLogger.logFailure("Scroll failed: " + elementName, e);

            throw new ElementActionException(
                    "Failed to scroll to: " + elementName, e);
        }
    }

    public boolean isDisplayed(By locator, String elementName) {

        try {
            TestLogger.logAction("Checking visibility: " + elementName);

            WebElement element = waitManager.waitForPresence(locator);

            boolean displayed = element.isDisplayed();

            TestLogger.logStep("Visibility checked: " + elementName);

            return displayed;

        } catch (Exception e) {

            TestLogger.logFailure("Visibility check failed: " + elementName, e);

            return false;
        }
    }

}
