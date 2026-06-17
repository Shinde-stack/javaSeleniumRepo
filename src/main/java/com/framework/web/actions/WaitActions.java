package com.framework.web.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.core.logging.TestLogger;

import java.time.Duration;

/**
 * ============================================================================
 * WaitActions
 * ============================================================================
 *
 * Responsibility: - Centralized explicit wait handling - Handles dynamic DOM
 * updates safely - Provides stable synchronization layer
 *
 * IMPORTANT: - Must NOT perform element actions (click/type) - Must NOT contain
 * business logic
 * ============================================================================
 */
public class WaitActions {

	private final WebDriverWait wait;

	public WaitActions(WebDriver driver) {
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	/**
	 * Wait until element is visible in DOM.
	 */
	public void waitForVisible(By by) {

		TestLogger.logWait("Waiting for visibilityOfElementLocated");
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	}

	/**
	 * Wait until element is clickable. Includes DOM re-evaluation internally.
	 */
	public void waitForClickable(By by) {
		TestLogger.logWait("Waiting for elementToBeClickable.");
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}

	/**
	 * Critical for SPA apps (React/Angular). Re-fetches element reference to avoid
	 * stale issues.
	 */
	public void waitForRefreshedClickable(By by) {
		TestLogger.logWait("Waiting for refreshed_elementToBeClickable.");
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(by)));
	}
}