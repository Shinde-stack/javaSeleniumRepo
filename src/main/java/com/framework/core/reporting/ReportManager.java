package com.framework.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.framework.core.logging.TestLogger;

/**
 * ============================================================================
 * Class Name : ReportManager
 * ============================================================================
 *
 * Purpose: -------- Centralized reporting utility built on top of
 * ExtentReports.
 *
 * This class is responsible for:
 *
 * 1. Initializing ExtentReports 2. Creating ExtentTest instances 3. Managing
 * thread-safe test storage 4. Providing reporting helper methods 5. Attaching
 * screenshots 6. Flushing final report
 *
 * ============================================================================
 *
 * Design Principles: ------------------
 *
 * 1. Single ExtentReports instance per execution 2. One ExtentTest per test
 * method 3. ThreadLocal isolation for parallel execution 4. Listener owns
 * lifecycle 5. ReportManager owns report operations only
 *
 * ============================================================================
 *
 * Execution Flow: ---------------
 *
 * BeforeSuite ↓ initReport()
 *
 * BeforeMethod ↓ createTest() ↓ setTest()
 *
 * Test Execution ↓ info() pass() fail()
 *
 * Failure ↓ addScreenshot()
 *
 * AfterMethod ↓ removeTest()
 *
 * AfterSuite ↓ flush()
 *
 * ============================================================================
 *
 * Parallel Execution Model: -------------------------
 *
 * Thread-1 ↓ LoginTest ↓ ExtentTest-A
 *
 * Thread-2 ↓ PaymentTest ↓ ExtentTest-B
 *
 * Each thread writes only to its own ExtentTest.
 *
 * Prevents: - Log corruption - Cross-thread reporting issues - Parallel
 * execution conflicts
 *
 * ============================================================================
 */
public final class ReportManager {

	/**
	 * Singleton ExtentReports instance.
	 *
	 * One report file per execution.
	 */
	private static ExtentReports extentReports;

	/**
	 * Thread-local ExtentTest storage.
	 *
	 * Each test thread gets its own ExtentTest instance.
	 */
	private static final ThreadLocal<ExtentTest> extentTest_thread = new ThreadLocal<>();

	/**
	 * Utility class.
	 */
	private ReportManager() {
	}

	// =========================================================================
	// REPORT INITIALIZATION
	// =========================================================================

	/**
	 * Initializes ExtentReports.
	 *
	 * Must be executed once before suite execution.
	 *
	 * Example:
	 *
	 * BeforeSuite ↓ ReportManager.initReport(path)
	 *
	 * @param reportPath report output path
	 */
	public static synchronized void initReport(String reportPath) {

		if (extentReports != null) {
			return;
		}

		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

		spark.config().setReportName("Automation Execution Report");

		spark.config().setDocumentTitle("Automation Results");

		extentReports = new ExtentReports();
		extentReports.attachReporter(spark);
	}

	// =========================================================================
	// TEST CREATION
	// =========================================================================

	/**
	 * Creates ExtentTest.
	 *
	 * Does NOT bind to ThreadLocal.
	 *
	 * Listener should:
	 *
	 * createTest() ↓ setTest()
	 *
	 * @param testName test method name
	 * @return ExtentTest instance
	 */
	public static ExtentTest createTest(String testName) {

		if (extentReports == null) {
			throw new IllegalStateException("ExtentReports not initialized. " + "Call initReport() first.");
		}

		return extentReports.createTest(testName);
	}

	/**
	 * Binds ExtentTest to current thread.
	 *
	 * Required for: - Parallel execution - Thread-safe reporting
	 *
	 * @param test ExtentTest instance
	 */
	public static void setTest(ExtentTest test) {

		if (test == null) {
			throw new IllegalArgumentException("ExtentTest cannot be null. 'createTest' need to be called before seatTest method");
		}

		extentTest_thread.set(test);
	}

	// =========================================================================
	// THREAD ACCESS
	// =========================================================================

	/**
	 * Returns current thread's ExtentTest.
	 *
	 * @return ExtentTest
	 */
	public static ExtentTest getTest() {

		ExtentTest test = extentTest_thread.get();

		if (test == null) {
			throw new IllegalStateException("ExtentTest not initialized " + "for current thread.");
		}

		return test;
	}

	// =========================================================================
	// LOGGING
	// =========================================================================

	/**
	 * Logs informational message.
	 *
	 * @param message report message
	 */
	public static void info(String message) {
		getTest().info(message);
	}

	/**
	 * Logs passed step.
	 *
	 * @param message success message
	 */
	public static void pass(String message) {
		getTest().pass(message);
	}

	/**
	 * Logs failed step.
	 *
	 * @param message failure message
	 */
	public static void fail(String message) {
		getTest().fail(message);
	}

	/**
	 * Logs exception.
	 *
	 * @param throwable failure exception
	 */
	public static void fail(Throwable throwable) {
		getTest().fail(throwable);
	}
	
	public static void warn (String message) {
		getTest().warning(message);
	}

	// =========================================================================
	// SCREENSHOTS
	// =========================================================================

	/**
	 * Attaches screenshot to current test.
	 *
	 * @param screenshotPath screenshot file path
	 */
	public static void addScreenshot(String screenshotPath) {
 		TestLogger.logStep("Report manager class - addScreenshot method");

		if (screenshotPath == null || screenshotPath.isBlank()) {
			return;
		}

		try {

			getTest().addScreenCaptureFromPath(screenshotPath);

		} catch (Exception e) {

			getTest().warning("Failed to attach screenshot: " + e.getMessage());
		}
	}

	/**
	 * Logs failure and attaches screenshot.
	 *
	 * Common usage:
	 *
	 * Listener ↓ onTestFailure()
	 *
	 * @param message        failure message
	 * @param screenshotPath screenshot path
	 */
	public static void failWithScreenshot(String message, String screenshotPath) {
 		TestLogger.logStep("Report manager class - failWithScreenshot method");

		getTest().fail(message);

		if (screenshotPath == null || screenshotPath.isBlank()) {
			return;
		}

		try {

			getTest().fail("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		} catch (Exception e) {

			getTest().warning("Failed to attach screenshot: " + e.getMessage());
		}
	}

	// =========================================================================
	// THREAD CLEANUP
	// =========================================================================

	/**
	 * Removes current thread's ExtentTest.
	 *
	 * MUST be called after every test.
	 *
	 * Prevents: - Memory leaks - Thread contamination - Parallel execution issues
	 */
	public static void removeTest() {
 		TestLogger.logStep("Report manager class - removeTest method");

		extentTest_thread.remove();
	}

	// =========================================================================
	// REPORT FINALIZATION
	// =========================================================================

	/**
	 * Flushes report to disk.
	 *
	 * Must execute once after suite completion.
	 *
	 * Example:
	 *
	 * AfterSuite ↓ ReportManager.flush()
	 */
	public static void flush() {

 		TestLogger.logStep("Report manager class - flush method");

		if (extentReports != null) {
			extentReports.flush();
		}
	}
}