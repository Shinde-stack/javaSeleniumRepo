package com.framework.core.reporting;

import java.nio.file.Path;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.framework.core.excepions.FrameworkException;
import com.framework.core.execution.ExecutionWorkspace;

/**
 * ============================================================================
 * Class Name : ReportManager
 * ============================================================================
 *
 * Manages ExtentReports lifecycle.
 *
 * Responsibilities:
 * - Initialize report.
 * - Create tests.
 * - Maintain current test using ThreadLocal.
 * - Log test information.
 * - Flush report.
 *
 * Does NOT:
 * - Create directories.
 * - Manage screenshots.
 * - Manage WebDriver.
 *
 * Filesystem ownership belongs to ExecutionWorkspace.
 *
 * Dependency direction:
 *
 * ExecutionWorkspace
 *        ↓
 * ReportManager
 *
 * Never:
 *
 * ReportManager
 *        ↓
 * ExecutionWorkspaceManager
 *
 * ============================================================================
 */
public final class ReportManager {

    private static ExtentReports extentReports;

    private static final ThreadLocal<ExtentTest> CURRENT_TEST =
            new ThreadLocal<>();

    private ReportManager() {
        throw new UnsupportedOperationException(
                "Utility class");
    }

    public static synchronized void init(
            ExecutionWorkspace workspace) {

        if (workspace == null) {
            throw new FrameworkException(
                    "ExecutionWorkspace cannot be null.");
        }

        if (extentReports != null) {
            return;
        }

        Path reportFile =
                workspace
                        .getReportDirectory()
                        .resolve("extent-report.html");

        ExtentSparkReporter reporter =
                new ExtentSparkReporter(
                        reportFile.toString());

        extentReports =
                new ExtentReports();

        extentReports.attachReporter(reporter);
    }

    public static ExtentTest createTest(
            String testName) {

        ensureInitialized();

        return extentReports.createTest(testName);
    }

    public static void setTest(
            ExtentTest test) {

        CURRENT_TEST.set(test);
    }

    public static ExtentTest getTest() {

        ExtentTest test =
                CURRENT_TEST.get();

        if (test == null) {
            throw new IllegalStateException(
                    "ExtentTest not initialized for thread: "
                    + Thread.currentThread().getName());
        }

        return test;
    }

    public static void removeTest() {

        CURRENT_TEST.remove();
    }

    public static void info(String message) {

        getTest().info(message);
    }

    public static void pass(String message) {

        getTest().pass(message);
    }

    public static void fail(Throwable throwable) {

        getTest().fail(throwable);
    }
    
    public static void fail(String message) {

        getTest().fail(message);
    }
    
    public static void warn(String message) {

        getTest().warning(message);
    }

    public static void flush() {

        if (extentReports != null) {
            extentReports.flush();
        }
    }

    private static void ensureInitialized() {

        if (extentReports == null) {

            throw new IllegalStateException(
                    "ReportManager has not been initialized.");
        }
    }
}