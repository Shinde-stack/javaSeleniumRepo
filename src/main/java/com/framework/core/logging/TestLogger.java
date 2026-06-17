//package com.framework.core.logging;
//
//import com.framework.core.reporting.ReportManager;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
///**
// * ============================================================================
// * Class Name : TestLogger
// * ============================================================================
// *
// * Purpose:
// * --------
// * Unified logging facade:
// * - Console logs (Log4j)
// * - ExtentReports (via ReportManager)
// *
// * IMPORTANT:
// * ----------
// * This class MUST NOT manage ExtentTest lifecycle.
// * It only consumes reporting layer.
// */
//public class TestLogger {
//
//    private static final Logger log =
//            LogManager.getLogger(TestLogger.class);
//
//    // ---------------------------------------------------------------------
//    // BUSINESS STEP LOGGING
//    // ---------------------------------------------------------------------
//
//    public static void logStep(String message) {
//
//        log.info(message);
//        ReportManager.info(message);
//    }
//
//    // ---------------------------------------------------------------------
//    // UI / ACTION LOGGING
//    // ---------------------------------------------------------------------
//
//    public static void logAction(String message) {
//
//        log.debug(message);
//        ReportManager.info("[ACTION] " + message);
//    }
//
//    // ---------------------------------------------------------------------
//    // GENERAL INFO LOGGING
//    // ---------------------------------------------------------------------
//
//    public static void logInfo(String message) {
//
//        log.info(message);
//        ReportManager.info(message);
//    }
//
//    // ---------------------------------------------------------------------
//    // FAILURE LOGGING
//    // ---------------------------------------------------------------------
//
//    public static void logFailure(String message, Throwable t) {
//
//        log.error(message, t);
//
//        ReportManager.fail(message);
//        ReportManager.fail(t);
//    }
//}

package com.framework.core.logging;

import com.framework.core.config.EnvConfig;
import com.framework.core.config.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.reporting.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ============================================================================
 * Class Name : TestLogger
 * ============================================================================
 *
 * PURPOSE: -------- Centralized logging facade for framework.
 *
 * Supports: - Console logging (Log4j) - Report logging (Extent)
 *
 * WHY THIS CLASS EXISTS: ---------------------- Framework code should never
 * directly call:
 *
 * log.info(...) ReportManager.info(...)
 *
 * Instead:
 *
 * TestLogger.logStep(...) TestLogger.logAction(...) TestLogger.logFailure(...)
 *
 * This gives: - Centralized control - Configurable logging - Cleaner framework
 * code - Easier future enhancements
 *
 * ============================================================================
 *
 * ARCHITECTURE
 * ============================================================================
 *
 * Framework Component ↓ TestLogger ↓ ---------------- | | Log4j ReportManager
 *
 * ============================================================================
 *
 * CONFIGURATION SOURCE
 * ============================================================================
 *
 * qa.properties ↓ ConfigLoader ↓ EnvConfig ↓ ExecutionContext ↓ TestLogger
 *
 * ============================================================================
 */
public final class TestLogger {

	/**
	 * Log4j logger.
	 */
	private static final Logger LOG = LogManager.getLogger(TestLogger.class);

	/**
	 * Utility class.
	 */
	private TestLogger() {
	}

	// =========================================================================
	// BUSINESS STEPS
	// =========================================================================

	/**
	 * High-level business step.
	 *
	 * Example:
	 *
	 * Login successful Order submitted Customer created
	 *
	 * Always important.
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

	// =========================================================================
	// ELEMENT ACTIONS
	// =========================================================================

	/**
	 * Low-level action logging.
	 *
	 * Examples:
	 *
	 * Clicked Login button Entered username Selected country dropdown
	 *
	 * Usually disabled in large suites.
	 */
	public static void logAction(String message) {

		EnvConfig config = getConfig();

		if (config == null) {
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

	// =========================================================================
	// WAIT ACTIONS
	// =========================================================================

	/**
	 * Wait-related logging.
	 *
	 * Examples:
	 *
	 * Waiting for visibility Waiting for clickability Waiting for page load
	 */
	public static void logWait(String message) {

		EnvConfig config = getConfig();

		if (config == null) {
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

	// =========================================================================
	// INFORMATIONAL LOGGING
	// =========================================================================

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

	// =========================================================================
	// FAILURE LOGGING
	// =========================================================================

	/**
	 * Failure logging.
	 *
	 * Failures should ALWAYS be logged.
	 *
	 * Never controlled by configuration.
	 *
	 * Why? ---- A failed test without logs is useless.
	 */
	public static void logFailure(String message, Throwable throwable) {

		LOG.error(message, throwable);

		try {

			ReportManager.fail(message);

			if (throwable != null) {
				ReportManager.fail(throwable);
			}

		} catch (Exception ignored) {
			// Report may not be initialized yet
		}
	}

	// =========================================================================
	// CONFIG ACCESS
	// =========================================================================

	/**
	 * Reads current runtime configuration from ExecutionContext.
	 *
	 * Returns null during startup phases before context initialization.
	 */
	private static EnvConfig getConfig() {

		try {

			ExecutionContext context = ExecutionContextHolder.getContext();

			if (context == null) {
				return null;
			}

			return context.getConfig();

		} catch (Exception e) {

			return null;
		}
	}

//	This is sufficient for
//	your current
//	framework.I would
//	not add
//	log levels, adapters, appenders, event buses,
//	or logging
//	strategies yet.
//	Those are
//	useful in
//	enterprise frameworks
//	but unnecessary complexity for
//	your practice framework.
	
	
}