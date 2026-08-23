package com.framework.core.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.framework.core.config.EnvConfig;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.reporting.ReportManager;

/**
 * TestLogger
 *
 * Unified logging facade: console (Log4j) and HTML report (Extent) behind one API.
 *
 * Flow:
 *   framework component → TestLogger.logStep/logAction/logPass/...
 *   → read EnvConfig from ExecutionContext (if available)
 *   → write to LOG (console) and/or ReportManager (Extent) based on config flags
 *
 * Does not create ExtentTest or manage report lifecycle; ReportManager owns that.
 */
public final class TestLogger {

    private static final Logger LOG =
            LogManager.getLogger(TestLogger.class);

    private TestLogger() {
    }

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

    public static void logAction(String message) {

        EnvConfig config = getConfig();

        if (config == null) {
            LOG.debug(message);
            return;
        }

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

    public static void logWait(String message) {

        EnvConfig config = getConfig();

        if (config == null) {
            LOG.debug(message);
            return;
        }

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

    public static void logPass(String message) {

        LOG.info(message);

        try {
            ReportManager.pass(message);
        } catch (Exception ignored) {
        }
    }

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
     * Returns EnvConfig from the active ExecutionContext, or null during early bootstrap.
     */
    private static EnvConfig getConfig() {

        try {

            ExecutionContext context =
                    ExecutionContextHolder.get();

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
