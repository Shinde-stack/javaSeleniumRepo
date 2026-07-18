package com.framework.web.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.core.logging.TestLogger;

/**
 * WebActions
 *
 * Low-level browser operations without explicit waits or retry logic.
 *
 * Flow:
 *   caller must ensure element stability before use → direct WebDriver call → log action
 *
 * Prefer ElementActions for page objects; this class is for cases where the element is already stable.
 */
public class WebActions {

	private final WebDriver driver;

	public WebActions(WebDriver driver) {
		this.driver = driver;
	}

	public void openUrl(String url) {
		TestLogger.logAction("webAction - openUrl: " + url);

		driver.get(url);
	}

	public void click(By by) {
		TestLogger.logAction("webAction - click: ");

		driver.findElement(by).click();
	}

	public void type(By by, String text) {
		TestLogger.logAction("webAction - send keys: " + text);

		driver.findElement(by).sendKeys(text);
	}

	public void clear(By by) {
		TestLogger.logAction("webAction - clear: ");

		driver.findElement(by).clear();
	}

	public String getText(By by) {
		TestLogger.logAction("webAction - get text: ");

		return driver.findElement(by).getText();
	}

	public void refresh() {
		TestLogger.logAction("webAction - navigate refresh: ");

		driver.navigate().refresh();
	}
}
