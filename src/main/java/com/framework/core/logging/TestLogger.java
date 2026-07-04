package com.framework.core.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.framework.core.config.EnvConfig;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.reporting.ReportManager;

/**
 * ============================================================================
 * Class Name : TestLogger
 * ============================================================================
 *
 * Purpose:
 * --------
 * Centralized logging facade for framework.
 *
 * All framework components should log only through this class.
 *
 * Responsibilities:
 * -----------------
 * - Console logging (Log4j)
 * - Report logging (Extent)
 * - Config-based logging control
 *
 * Non Responsibilities:
 * ---------------------
 * - Report lifecycle management
 * - ExtentTest creation
 * - Assertion execution
 *
 * Architecture:
 * -------------
 *
 * Framework Component
 *         ↓
 *     TestLogger
 *      ↙     ↘
 *  Log4j    ReportManager
 *
 * ============================================================================
 */
public final class TestLogger {

    private static final Logger LOG =
            LogManager.getLogger(TestLogger.class);

    private TestLogger() {
    }

    /**
     * Business level execution step.
     *
     * Example:
     * Login successful
     * Order created
     */
    public static void logStep(String message) {

        EnvConfig config = getConfig();

        if (config == null) {
            LOG.info(message);
            return;
        }

        if (config.isLogToConsole()) {
            LOG.info(message);
        }

        if (config.isLogToReport()) {
            ReportManager.info(message);
        }
    }

    /**
     * Element interaction logging.
     *
     * Example:
     * Clicked Login button
     * Entered Username
     */
    public static void logAction(String message) {

        EnvConfig config = getConfig();

        // Framework startup phase
        if (config == null) {
            LOG.debug(message);
            return;
        }

        // Feature disabled
        if (!config.isLogElementActions()) {
            return;
        }

        if (config.isLogToConsole()) {
            LOG.debug(message);
        }

        if (config.isLogToReport()) {
            ReportManager.info("[ACTION] " + message);
        }
    }

    /**
     * Wait operation logging.
     *
     * Example:
     * Waiting for visibility
     * Waiting for clickability
     */
    public static void logWait(String message) {

        EnvConfig config = getConfig();

        // Framework startup phase
        if (config == null) {
            LOG.debug(message);
            return;
        }

        // Feature disabled
        if (!config.isLogWaitActions()) {
            return;
        }

        if (config.isLogToConsole()) {
            LOG.debug(message);
        }

        if (config.isLogToReport()) {
            ReportManager.info("[WAIT] " + message);
        }
    }

    /**
     * Generic informational log.
     */
    public static void logInfo(String message) {

        EnvConfig config = getConfig();

        if (config == null) {
            LOG.info(message);
            return;
        }

        if (config.isLogToConsole()) {
            LOG.info(message);
        }

        if (config.isLogToReport()) {
            ReportManager.info(message);
        }
    }

    /**
     * Assertion or execution success.
     *
     * Generates PASS status in report.
     */
    public static void logPass(String message) {

        LOG.info(message);

        try {
            ReportManager.pass(message);
        } catch (Exception ignored) {
        }
    }

    /**
     * Assertion or execution failure.
     *
     * Generates FAIL status in report.
     */
    public static void logFailure(
            String message,
            Throwable throwable) {

        LOG.error(message, throwable);

        try {

            ReportManager.fail(message);

            if (throwable != null) {
                ReportManager.fail(throwable);
            }

        } catch (Exception ignored) {
        }
    }

    /**
     * Reads current runtime configuration.
     */
    private static EnvConfig getConfig() {

        try {

            ExecutionContext context =
                    ExecutionContextHolder.getContext();

            return context != null
                    ? context.getConfig()
                    : null;

        } catch (Exception e) {

            return null;
        }
    }
    
    public static void logWarning(String message) {

        EnvConfig config = getConfig();

        if (config == null) {
            LOG.warn(message);
            return;
        }

        if (config.isLogToConsole()) {
            LOG.warn(message);
        }

        if (config.isLogToReport()) {
            ReportManager.warn(message);
        }
    }
}