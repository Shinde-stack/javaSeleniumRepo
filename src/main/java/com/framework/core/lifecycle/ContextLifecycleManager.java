package com.framework.core.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.core.config.ConfigLoader;
import com.framework.core.config.EnvConfig;
import com.framework.core.context.ContextState;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.driver.DriverFactory;
import com.framework.core.driver.DriverManager;
import com.framework.core.excepions.FrameworkException;
import com.framework.core.execution.ExecutionWorkspace;
import com.framework.core.validation.ContextValidator;

/**
 * ============================================================================
 * Class Name : ContextLifecycleManager
 * ============================================================================
 *
 * PURPOSE
 * -------
 * Manages the lifecycle of ONE test-method execution.
 *
 * Responsibilities:
 *
 * 1. Load configuration.
 * 2. Create ExecutionContext.
 * 3. Validate ExecutionContext.
 * 4. Initialize WebDriver.
 * 5. Launch application.
 * 6. Cleanup WebDriver.
 * 7. Remove ExecutionContext from ThreadLocal.
 *
 * IMPORTANT
 * ---------
 * ExecutionWorkspace is created outside this class.
 *
 * TestNG Listener owns suite-level execution workspace creation.
 *
 * Therefore:
 *
 * TestListener
 *      ↓
 * ExecutionWorkspace
 *      ↓
 * ContextLifecycleManager.start(workspace)
 *      ↓
 * ExecutionContext
 *
 * This class does NOT know about:
 *
 * - TestNG
 * - ExtentReports
 * - ScreenshotService
 *
 * ============================================================================
 */
public class ContextLifecycleManager {

    private static final Logger log =
            LoggerFactory.getLogger(
                    ContextLifecycleManager.class);

    private final ConfigLoader configLoader;

    private final ContextValidator contextValidator;

    private final DriverFactory driverFactory;

    private final DriverManager driverManager;


    /**
     * Constructor.
     *
     * Dependencies are created here for simplicity.
     *
     * Later, if required, these can be injected from a
     * composition root / dependency injection layer.
     */
    public ContextLifecycleManager() {

        this.configLoader =
                new ConfigLoader();

        this.contextValidator =
                new ContextValidator();

        this.driverFactory =
                new DriverFactory();

        this.driverManager =
                new DriverManager(driverFactory);
    }


    /**
     * =========================================================================
     * START
     * =========================================================================
     *
     * Starts ONE test execution.
     *
     * ExecutionWorkspace is supplied by TestListener because workspace
     * belongs to the suite/run while ExecutionContext belongs to the
     * individual test execution.
     *
     * Flow:
     *
     * workspace
     *     ↓
     * load configuration
     *     ↓
     * create ExecutionContext
     *     ↓
     * validate context
     *     ↓
     * bind context to ThreadLocal
     *     ↓
     * initialize driver
     *     ↓
     * launch application
     *
     * @param workspace shared execution workspace
     *
     * @return initialized ExecutionContext
     */
    public ExecutionContext start(
            ExecutionWorkspace workspace) {

        if (workspace == null) {

            throw new FrameworkException(
                    "ExecutionWorkspace cannot be null.");
        }

        log.info(
                "Starting framework lifecycle | thread={}",
                Thread.currentThread().getName());


        // ================================================================
        // STEP 1: Load configuration
        // ================================================================

        log.debug(
                "Loading framework configuration.");

        EnvConfig config =
                configLoader.load();


        // ================================================================
        // STEP 2: Create ExecutionContext
        // ================================================================

        log.debug(
                "Creating ExecutionContext.");

        ExecutionContext context =
                new ExecutionContext(workspace);


        // ================================================================
        // STEP 3: Store configuration
        // ================================================================

        context.setConfig(config);


        // ================================================================
        // STEP 4: Mark context initialized
        // ================================================================

        context.setState(
                ContextState.INITIALIZED);


        // ================================================================
        // STEP 5: Validate context
        // ================================================================

        contextValidator.validate(context);


        // ================================================================
        // STEP 6: Bind context to current thread
        // ================================================================

        ExecutionContextHolder.set(context);


        log.debug(
                "ExecutionContext bound to thread={}",
                Thread.currentThread().getName());


        try {

            // ============================================================
            // STEP 7: Initialize WebDriver
            // ============================================================

            initializeDriver(context);


            // ============================================================
            // STEP 8: Launch application
            // ============================================================

            launchApplication(context);


            log.info(
                    "Framework lifecycle started successfully | thread={}",
                    Thread.currentThread().getName());

            return context;

        } catch (Exception e) {

            /*
             * Startup failed.
             *
             * We must cleanup immediately because the test may never
             * reach onTestFailure().
             */
            log.error(
                    "Framework startup failed.",
                    e);

            destroyContext();

            throw e;
        }
    }


    /**
     * =========================================================================
     * INITIALIZE DRIVER
     * =========================================================================
     */
    private void initializeDriver(
            ExecutionContext context) {

        if (context.getState()
                != ContextState.INITIALIZED) {

            throw new FrameworkException(
                    "ExecutionContext must be INITIALIZED "
                    + "before driver initialization.");
        }

        log.info(
                "Initializing WebDriver | browser={}",
                context.getConfig().getBrowserType());


        driverManager.initializeDriver(context);


        context.setState(
                ContextState.RUNNING);


        log.info(
                "WebDriver initialized successfully.");
    }


    /**
     * =========================================================================
     * LAUNCH APPLICATION
     * =========================================================================
     */
    private void launchApplication(
            ExecutionContext context) {

        if (context.getState()
                != ContextState.RUNNING) {

            throw new FrameworkException(
                    "ExecutionContext must be RUNNING "
                    + "before launching application.");
        }


        String url =
                context.getConfig().getBaseUrl();


        log.info(
                "Launching application: {}",
                url);


        driverManager
                .getDriver(context)
                .get(url);
    }


    /**
     * =========================================================================
     * DESTROY CONTEXT
     * =========================================================================
     *
     * Cleanup is intentionally defensive.
     *
     * Even if browser shutdown fails:
     *
     * ExecutionContextHolder.clear()
     *
     * MUST execute.
     *
     * This is especially important with TestNG parallel execution because
     * worker threads are reused.
     */
    public void destroyContext() {

        ExecutionContext context = null;

        try {

            context =
                    ExecutionContextHolder.get();

            log.info(
                    "Cleaning framework resources | thread={}",
                    Thread.currentThread().getName());


            // ============================================================
            // STEP 1: Quit WebDriver
            // ============================================================

            driverManager.quitDriver(context);


        } catch (IllegalStateException e) {

            /*
             * No context exists for this thread.
             *
             * This is not necessarily a framework failure during cleanup.
             */
            log.debug(
                    "No ExecutionContext found during cleanup.");


        } catch (Exception e) {

            /*
             * Never allow cleanup failure to prevent ThreadLocal cleanup.
             */
            log.error(
                    "Error while cleaning framework resources.",
                    e);


        } finally {

            // ============================================================
            // STEP 2: Mark context destroyed
            // ============================================================

            if (context != null) {

                context.setState(
                        ContextState.DESTROYED);
            }


            // ============================================================
            // STEP 3: ALWAYS clear ThreadLocal
            // ============================================================

            ExecutionContextHolder.clear();


            log.info(
                    "ExecutionContext destroyed | thread={}",
                    Thread.currentThread().getName());
        }
    }
}