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

/**
 * ContextLifecycleManager
 *
 * Orchestrates per-test framework startup and teardown.
 *
 * Flow:
 *   start() → load config → create ExecutionContext → init driver → navigate to baseUrl
 *   cleanupContext() → quit driver → mark DESTROYED → clear ThreadLocal
 *
 * Called exclusively from TestListener on each test method boundary.
 */
public class ContextLifecycleManager {

    private static final Logger log =
            LoggerFactory.getLogger(ContextLifecycleManager.class);

    private final DriverManager driverManager = new DriverManager();

    /**
     * Loads config, creates context, and binds it to the current thread.
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
     * Creates WebDriver from config and marks context as RUNNING.
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
     * Quits driver and removes ExecutionContext from ThreadLocal after each test.
     */
    public void cleanupContext() {

        ExecutionContext context = null;

        try {

            context = ExecutionContextHolder.getContext();

            if (driverManager != null) {
                driverManager.quitDriver(context);
            }

        } catch (IllegalStateException ignored) {

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
     * Full startup sequence invoked at the beginning of each test.
     */
    public ExecutionContext start() {

        TestLogger.logStep("Lifecycle -> Starting framework");

        ExecutionContext context = initializeContext();

        initDriver(context);

        launchApplication(context);

        TestLogger.logStep("Lifecycle -> Framework started successfully");

        return context;
    }
    
    /**
     * Opens the configured base URL in the active browser.
     */
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
