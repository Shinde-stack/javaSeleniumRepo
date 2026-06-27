package com.framework.web.base;

import org.openqa.selenium.WebDriver;

import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.web.actions.ElementActions;
import com.framework.web.waits.WaitManager;
import com.framework.core.logging.TestLogger;

/**
 * ============================================================================
 * Class Name : BasePage
 * ============================================================================
 *
 * ROLE:
 * -----
 * Parent class for ALL Page Objects.
 *
 * Provides:
 * - ExecutionContext access
 * - ElementActions
 * - WaitManager
 * - Common UI utilities
 *
 * RULE:
 * -----
 * ALL pages MUST extend BasePage.
 *
 * WHY:
 * ----
 * Avoid duplicate driver/wait/action initialization.
 */
public abstract class BasePage {

    protected ExecutionContext context;
    protected WebDriver driver;

    protected ElementActions actions;
    protected WaitManager waits;

    /**
     * Constructor initializes all page dependencies.
     */
    public BasePage() {

        this.context = ExecutionContextHolder.getContext();

        this.driver = context.getDriverContext().getDriver();

        this.waits = new WaitManager(driver);
        this.actions = new ElementActions(driver, waits);

        TestLogger.logStep("BasePage initialized for: "
                + this.getClass().getSimpleName());
    }

    // ------------------------------------------------------------------------
    // COMMON PAGE UTILITIES
    // ------------------------------------------------------------------------

    /**
     * Refresh current page
     */
    public void refreshPage() {
        TestLogger.logAction("Refreshing page");
        driver.navigate().refresh();
    }

    /**
     * Get current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Navigate back
     */
    public void goBack() {
        TestLogger.logAction("Navigating back");
        driver.navigate().back();
    }
}