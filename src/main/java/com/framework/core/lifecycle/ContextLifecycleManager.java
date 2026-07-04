package com.framework.core.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.core.config.ConfigLoader;
import com.framework.core.config.EnvConfig;
import com.framework.core.context.ContextState;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.driver.DriverManager;
import com.framework.core.logging.TestLogger;

public class ContextLifecycleManager {

    private static final Logger log =
            LoggerFactory.getLogger(ContextLifecycleManager.class);

    private final DriverManager driverManager = new DriverManager();

    /**
     * Creates ExecutionContext.
     *
     * Does NOT initialize reporting.
     * Does NOT perform test logging.
     */
    public ExecutionContext initializeContext() {

        log.info("Initializing ExecutionContext");

        EnvConfig config = new ConfigLoader().load();

        ExecutionContext context = new ExecutionContext();

        context.setConfig(config);
        context.setState(ContextState.CREATED);

        ExecutionContextHolder.setContext(context);

        context.setState(ContextState.INITIALIZED);

        log.info("ExecutionContext initialized");

        return context;
    }

    /**
     * Initializes browser.
     */
    public void initDriver(ExecutionContext context) {

        log.info("Initializing WebDriver");

        driverManager.initializeDriver(
                context,
                context.getConfig().getBrowserType(),
                context.getConfig().isHeadless());

        context.setState(ContextState.RUNNING);

        log.info("Driver initialized");
    }

    /**
     * Cleanup after every test.
     */
    public void cleanupContext() {

        ExecutionContext context = null;

        try {

            context = ExecutionContextHolder.getContext();

            if (driverManager != null) {
                driverManager.quitDriver(context);
            }

        } catch (IllegalStateException ignored) {

            // Context was never created.

        } catch (Exception e) {

            log.error("Cleanup failed", e);

        } finally {

            if (context != null) {
                context.setState(ContextState.DESTROYED);
            }

            ExecutionContextHolder.removeContext();

            log.info("ExecutionContext destroyed");
        }
    }
    
    /**
     * ============================================================================
     * Starts complete framework lifecycle for ONE test execution.
     * ============================================================================
     *
     * Responsibilities:
     * -----------------
     * 1. Load framework configuration
     * 2. Create ExecutionContext
     * 3. Bind ExecutionContext to current thread
     * 4. Initialize browser driver
     * 5. Launch application
     * 6. Mark execution as RUNNING
     *
     * This is the ONLY public startup method used by TestListener.
     *
     * Returns:
     * --------
     * Current ExecutionContext for the running test.
     *
     * Future Expansion:
     * -----------------
     * Web:
     *  - Browser initialization
     *
     * API:
     *  - REST client initialization
     *
     * Database:
     *  - Connection initialization
     *
     * Mobile:
     *  - Appium driver initialization
     *
     * Cloud:
     *  - Remote execution initialization
     *
     * ============================================================================
     */
    public ExecutionContext start() {

        TestLogger.logStep("Lifecycle -> Starting framework");

        // ---------------------------------------------------------
        // STEP 1
        // Create and initialize execution context.
        // ---------------------------------------------------------
        ExecutionContext context = initializeContext();

        // ---------------------------------------------------------
        // STEP 2
        // Create browser driver.
        // Driver is stored inside DriverContext.
        // ---------------------------------------------------------
        initDriver(context);

        // ---------------------------------------------------------
        // STEP 3
        // Launch application.
        // Browser opens configured Base URL.
        // ---------------------------------------------------------
        launchApplication(context);

        // ---------------------------------------------------------
        // STEP 4
        // Framework ready.
        // ---------------------------------------------------------
        TestLogger.logStep("Lifecycle -> Framework started successfully");

        return context;
    }
    
    public void launchApplication(ExecutionContext context) {

        TestLogger.logStep("Lifecycle -> Launching application");

        context.getDriverContext()
               .getDriver()
               .get(context.getConfig().getBaseUrl());

        TestLogger.logStep(
            "Application opened : "
            + context.getConfig().getBaseUrl());
    }
    
}