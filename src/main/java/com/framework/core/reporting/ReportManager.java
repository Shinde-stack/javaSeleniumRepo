package com.framework.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.framework.core.constants.ReportConstants;

/**
 * ============================================================================
 * Class Name : ReportManager
 * ============================================================================
 *
 * Thread-safe wrapper around ExtentReports.
 *
 * Responsibilities
 * ----------------
 * - Initialize ExtentReports once per execution.
 * - Maintain one ExtentTest per execution thread.
 * - Provide framework-wide reporting APIs.
 * - Flush the report after execution.
 *
 * This class intentionally contains no:
 * - Selenium logic
 * - TestNG logic
 * - Screenshot capture logic
 * - Execution directory logic
 *
 * Flow
 * ----
 *
 * TestListener
 *      │
 *      ▼
 * initReport(reportPath)
 *      │
 *      ▼
 * createTest()
 *      │
 *      ▼
 * setTest()
 *      │
 *      ▼
 * pass()/fail()/info()/warn()
 *      │
 *      ▼
 * flush()
 *
 * ============================================================================
 */
public final class ReportManager {

    private static ExtentReports extentReports;

    /**
     * One ExtentTest per execution thread.
     */
    private static final ThreadLocal<ExtentTest> CURRENT_TEST =
            new ThreadLocal<>();

    private ReportManager() {
        throw new UnsupportedOperationException(
                "Utility class should not be instantiated.");
    }

    /**
     * Initializes ExtentReports.
     *
     * Safe to invoke multiple times.
     */
    public static synchronized void initReport(String reportPath) {

        if (extentReports != null) {
            return;
        }

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(reportPath);

        sparkReporter.config()
                .setReportName(
                        ReportConstants.REPORT_NAME);

        sparkReporter.config()
                .setDocumentTitle(
                        ReportConstants.REPORT_TITLE);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    /**
     * Creates a new ExtentTest.
     */
    public static ExtentTest createTest(String testName) {

        if (extentReports == null) {
            throw new IllegalStateException(
                    "ExtentReports has not been initialized.");
        }

        return extentReports.createTest(testName);
    }

    /**
     * Associates the supplied ExtentTest with the current thread.
     */
    public static void setTest(ExtentTest test) {

        if (test == null) {
            throw new IllegalArgumentException(
                    "ExtentTest cannot be null.");
        }

        CURRENT_TEST.set(test);
    }

    /**
     * Returns the current thread's ExtentTest.
     */
    public static ExtentTest getTest() {

        ExtentTest test =
                CURRENT_TEST.get();

        if (test == null) {
            throw new IllegalStateException(
                    "ExtentTest not initialized for current thread.");
        }

        return test;
    }

    public static void info(String message) {
        getTest().info(message);
    }

    public static void pass(String message) {
        getTest().pass(message);
    }

    public static void warn(String message) {
        getTest().warning(message);
    }

    public static void fail(String message) {
        getTest().fail(message);
    }

    public static void fail(Throwable throwable) {
        getTest().fail(throwable);
    }

    /**
     * Attaches a screenshot to the current test.
     */
    public static void addScreenshot(String screenshotPath) {

        if (screenshotPath == null || screenshotPath.isBlank()) {
            return;
        }

        try {

            getTest().addScreenCaptureFromPath(screenshotPath);

        } catch (Exception ex) {

            getTest().warning(
                    "Unable to attach screenshot : "
                            + ex.getMessage());
        }
    }

    /**
     * Marks the test as failed and attaches a screenshot.
     */
    public static void failWithScreenshot(
            String message,
            String screenshotPath) {

        getTest().fail(message);

        if (screenshotPath == null || screenshotPath.isBlank()) {
            return;
        }

        try {

            getTest().fail(
                    "Screenshot",
                    MediaEntityBuilder
                            .createScreenCaptureFromPath(screenshotPath)
                            .build());

        } catch (Exception ex) {

            getTest().warning(
                    "Unable to attach screenshot : "
                            + ex.getMessage());
        }
    }

    /**
     * Removes the ExtentTest associated with the current thread.
     */
    public static void removeTest() {
        CURRENT_TEST.remove();
    }

    /**
     * Writes the report to disk.
     */
    public static synchronized void flush() {

        if (extentReports != null) {
            extentReports.flush();
        }
    }
}