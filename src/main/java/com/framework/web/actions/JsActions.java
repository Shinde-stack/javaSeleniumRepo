package com.framework.web.actions;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.core.logging.TestLogger;

/**
 * JsActions
 *
 * JavaScript-based fallback interactions when standard Selenium actions fail.
 *
 * Flow:
 *   stubborn element → JsActions.click/scrollTo → executeScript on located element
 *
 * Not wired into ElementActions yet; use only as a secondary strategy, not the default path.
 */
public class JsActions {

    private final JavascriptExecutor js;
    private final WebDriver driver;

    public JsActions(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    public void click(By by) {
    	
        TestLogger.logAction( "Js click: ");

        js.executeScript("arguments[0].click();",
                driver.findElement(by));
    }

    public void scrollTo(By by) {
    	
        TestLogger.logAction( "Js Scroll element");

        js.executeScript("arguments[0].scrollIntoView(true);",
                driver.findElement(by));
    }

    /**
     * Standalone page-load wait; prefer WaitManager.waitForPageLoad() in the main flow.
     */
    public void waitForPageLoad(WebDriver driver, long timeoutInSeconds) {
    	
		TestLogger.logStep("Js waitForPageLoad - temp method from web...not as per our framework");

        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .toString()
                .equals("complete")
        );}
}
