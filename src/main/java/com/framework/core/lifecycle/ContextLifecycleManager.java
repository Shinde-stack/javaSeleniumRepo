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
import com.framework.core.execution.ExecutionDirectoryManager;
import com.framework.core.execution.ExecutionWorkspace;
import com.framework.core.execution.ExecutionWorkspaceManager;
import com.framework.core.validation.ContextValidator;
import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : ContextLifecycleManager
 * ============================================================================
 *
 * Orchestrates the lifecycle of one test execution.
 *
 * Responsibilities
 * ----------------
 * - Load framework configuration.
 * - Create ExecutionWorkspace.
 * - Create ExecutionContext.
 * - Validate ExecutionContext.
 * - Initialize WebDriver.
 * - Launch application.
 * - Destroy framework resources.
 *
 * This class is the composition root for per-test execution.
 *
 * Flow
 * ----
 *
 * start()
 *      │
 *      ▼
 * create ExecutionWorkspace
 *      │
 *      ▼
 * create ExecutionContext
 *      │
 *      ▼
 * initializeDriver()
 *      │
 *      ▼
 * launchApplication()
 *      │
 *      ▼
 * destroyContext()
 *
 * ============================================================================
 */
public class ContextLifecycleManager {

    private static final Logger log =
            LoggerFactory.getLogger(ContextLifecycleManager.class);

    private final ConfigLoader configLoader;

    private final ContextValidator contextValidator;

    private final DriverFactory driverFactory;

    private final DriverManager driverManager;

    private final ExecutionWorkspaceManager executionWorkspaceManager;

    public ContextLifecycleManager() {

        this.configLoader = new ConfigLoader();

        this.contextValidator = new ContextValidator();

        this.driverFactory = new DriverFactory();

        this.driverManager = new DriverManager(driverFactory);

        this.executionWorkspaceManager =
                new ExecutionWorkspaceManager(
                        new ExecutionDirectoryManager());
    }

    /**
     * Executes the complete framework startup sequence.
     */
    public ExecutionContext start() {

        log.info("Starting framework lifecycle.");

        ExecutionContext context = initializeContext();

        initializeDriver(context);

        launchApplication(context);

        log.info("Framework started successfully.");

        return context;
    }

    /**
     * Creates and initializes ExecutionContext.
     */
    private ExecutionContext initializeContext() {

        log.debug("Loading framework configuration.");

        EnvConfig config = configLoader.load();

        log.debug("Creating execution workspace.");

        ExecutionWorkspace workspace =
                executionWorkspaceManager.createWorkspace();

        log.debug("Creating ExecutionContext.");

        ExecutionContext context =
                new ExecutionContext(workspace);

        context.setConfig(config);

        context.setState(ContextState.INITIALIZED);

        // Validate before publishing to ThreadLocal
        contextValidator.validate(context);

        ExecutionContextHolder.set(context);

        log.debug("ExecutionContext initialized successfully.");

        return context;
    }

    /**
     * Creates the WebDriver and stores it inside DriverContext.
     */
    private void initializeDriver(
            ExecutionContext context) {

        if (context.getState() != ContextState.INITIALIZED) {

            throw new FrameworkException(
                    "ExecutionContext must be INITIALIZED before driver initialization.");
        }

        log.info("Initializing WebDriver.");

        driverManager.initializeDriver(context);

        context.setState(ContextState.RUNNING);

        log.info("WebDriver initialized successfully.");
    }

    /**
     * Opens the configured application URL.
     */
    private void launchApplication(
            ExecutionContext context) {

        if (context.getState() != ContextState.RUNNING) {

            throw new FrameworkException(
                    "ExecutionContext must be RUNNING before launching application.");
        }

        String url =
                context.getConfig().getBaseUrl();

        log.info("Launching application : {}", url);

        driverManager
                .getDriver(context)
                .get(url);
    }

    /**
     * Releases framework resources.
     *
     * This method always clears ThreadLocal storage even if browser shutdown
     * fails.
     */
    public void destroyContext() {

        ExecutionContext context = null;

        try {

            context = ExecutionContextHolder.get();

            driverManager.quitDriver(context);

            log.info("WebDriver shutdown completed.");

        } catch (Exception ex) {

            log.error("Framework cleanup failed.", ex);

        } finally {

            if (context != null) {

                context.setState(ContextState.DESTROYED);
            }

            ExecutionContextHolder.clear();

            log.info("ExecutionContext destroyed.");
        }
    }
}