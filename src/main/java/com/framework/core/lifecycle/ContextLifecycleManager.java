package com.framework.core.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.core.config.ConfigLoader;
import com.framework.core.config.EnvConfig;
import com.framework.core.context.ContextState;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.driver.DriverManager;

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
    public void cleanupContext(ExecutionContext context) {

        log.info("Cleaning ExecutionContext");

        try {

            driverManager.quitDriver(context);

        } finally {

            context.setState(ContextState.DESTROYED);

            ExecutionContextHolder.removeContext();

            log.info("ExecutionContext destroyed");
        }
    }
}