package com.framework.web.pages;

import org.openqa.selenium.WebDriver;

import com.framework.core.config.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.logging.TestLogger;
import com.framework.web.actions.ElementActions;
import com.framework.web.actions.WebActions;

/**
 * =============================================================================
 * Class Name : BasePage
 * =============================================================================
 *
 * Responsibility:
 * --------------
 * Root abstraction for all Web Page Objects.
 *
 * It provides:
 * - Thread-safe access to ExecutionContext
 * - Safe WebDriver retrieval
 * - Common UI action layer (ElementActions, WebActions)
 *
 * It DOES NOT:
 * - Create WebDriver
 * - Manage lifecycle
 * - Perform business logic
 * - Decide test flows
 *
 * =============================================================================
 *
 * Thread Safety Model:
 * --------------------
 * ExecutionContext is stored in ThreadLocal (ExecutionContextHolder).
 * Each test thread gets isolated driver + metadata + API + DB state.
 *
 * =============================================================================
 */
public abstract class BasePage {

    /**
     * ExecutionContext is the root state container for the current test thread.
     *
     * Contains:
     * - DriverContext
     * - ApiContext
     * - DbContext
     * - MetadataContext
     */
    protected final ExecutionContext context;

    /**
     * WebDriver instance resolved from ExecutionContext.
     *
     * IMPORTANT:
     * - Must NEVER be created inside Page layer
     * - Must always come from ExecutionContext
     */
    protected final WebDriver driver;

    /**
     * High-level UI interaction wrapper.
     *
     * Responsibility:
     * - click, sendKeys, waits, validations
     */
    protected final ElementActions actions;

    /**
     * Browser-level actions.
     *
     * Responsibility:
     * - navigation (openUrl, back, refresh)
     * - browser state operations
     */
    protected final WebActions webActions;

    /**
     * Constructor:
     * Initializes page object using thread-local ExecutionContext.
     *
     * Contract:
     * - ExecutionContext MUST be initialized before page creation
     * - BaseTest is responsible for lifecycle bootstrap
     */
    protected BasePage() {
    	
		TestLogger.logStep("---++++++++++++++++++++++++++++++--------------BasePage-------------------CONSTRUCTOR");

        // ---------------------------------------------------------------------
        // Step 1: Fetch ExecutionContext from ThreadLocal
        // ---------------------------------------------------------------------
        this.context = ExecutionContextHolder.getContext();
		TestLogger.logStep("---BasePage---"+context);

        if (this.context == null) {
            throw new IllegalStateException(
                    "ExecutionContext is not initialized. " +
                    "Ensure BaseTest / FrameworkBootstrap runs before page creation.");
        }

        // ---------------------------------------------------------------------
        // Step 2: Resolve WebDriver safely from DriverContext
        // ---------------------------------------------------------------------
        if (this.context.getDriverContext() == null ||
            this.context.getDriverContext().getDriver() == null) {

    		TestLogger.logStep("---BasePage---context driverrrr");

            throw new IllegalStateException(
                    "WebDriver is not initialized inside ExecutionContext.");
        }

        this.driver = this.context.getDriverContext().getDriver();

        // ---------------------------------------------------------------------
        // Step 3: Initialize action layers
        // ---------------------------------------------------------------------


        this.actions = new ElementActions(driver);
		TestLogger.logStep("---BasePage---"+actions);

        this.webActions = new WebActions(driver);
		TestLogger.logStep("---BasePage---"+webActions);
    }

    // =========================================================================
    // Browser State Helpers (lightweight convenience methods)
    // =========================================================================

    /**
     * Returns current page title.
     */
    public String getPageTitle() {
		TestLogger.logStep("BasePage methods -> getPageTitle");

        return driver.getTitle();
    }

    /**
     * Returns current URL.
     */
    public String getCurrentUrl() {
		TestLogger.logStep("BasePage methods -> getCurrentUrl");

        return driver.getCurrentUrl();
    }

    /**
     * Refresh current browser page.
     */
    public void refreshPage() {
		TestLogger.logStep("BasePage methods -> refreshPage");

        driver.navigate().refresh();
    }

    /**
     * Navigate back in browser history.
     */
    public void navigateBack() {
		TestLogger.logStep("BasePage methods -> navigateBack");

        driver.navigate().back();
    }

    /**
     * Navigate forward in browser history.
     */
    public void navigateForward() {
		TestLogger.logStep("BasePage methods -> navigateForward");

        driver.navigate().forward();
    }

    /**
     * Returns ElementActions for child pages.
     *
     * NOTE:
     * Kept for extensibility, but direct field access is preferred.
     */
    protected ElementActions actions() {
		TestLogger.logStep("BasePage methods -> actions");

        return actions;
    }

    /**
     * Returns WebActions for child pages.
     */
    protected WebActions webActions() {
		TestLogger.logStep("BasePage methods -> webActions");

        return webActions;
    }

    /**
     * Returns ExecutionContext for advanced use cases:
     * - metadata
     * - API chaining
     * - DB validation
     */
    protected ExecutionContext context() {
		TestLogger.logStep("BasePage methods -> context");

        return context;
    }
}
