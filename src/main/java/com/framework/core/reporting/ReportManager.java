package com.framework.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.framework.core.logging.TestLogger;

/**
 * ReportManager
 *
 * Thread-safe wrapper around ExtentReports for HTML test reporting.
 *
 * Flow:
 *   suite onStart     → initReport(path)
 *   test onStart      → createTest(name) → setTest() on ThreadLocal
 *   during test       → info/pass/fail/warn via TestLogger or direct calls
 *   test onFailure    → addScreenshot(path)
 *   test end          → removeTest()
 *   suite onFinish    → flush()
 */
public final class ReportManager {

	private static ExtentReports extentReports;

	private static final ThreadLocal<ExtentTest> extentTest_thread = new ThreadLocal<>();

	private ReportManager() {
	}

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

	public static ExtentTest createTest(String testName) {

		if (extentReports == null) {
			throw new IllegalStateException("ExtentReports not initialized. " + "Call initReport() first.");
		}

		return extentReports.createTest(testName);
	}

	public static void setTest(ExtentTest test) {

		if (test == null) {
			throw new IllegalArgumentException("ExtentTest cannot be null. 'createTest' need to be called before seatTest method");
		}

		extentTest_thread.set(test);
	}

	public static ExtentTest getTest() {

		ExtentTest test = extentTest_thread.get();

		if (test == null) {
			throw new IllegalStateException("ExtentTest not initialized " + "for current thread.");
		}

		return test;
	}

	public static void info(String message) {
		getTest().info(message);
	}

	public static void pass(String message) {
		getTest().pass(message);
	}

	public static void fail(String message) {
		getTest().fail(message);
	}

	public static void fail(Throwable throwable) {
		getTest().fail(throwable);
	}
	
	public static void warn (String message) {
		getTest().warning(message);
	}

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

	public static void removeTest() {
 		TestLogger.logStep("Report manager class - removeTest method");

		extentTest_thread.remove();
	}

	public static void flush() {

 		TestLogger.logStep("Report manager class - flush method");

		if (extentReports != null) {
			extentReports.flush();
		}
	}
}
