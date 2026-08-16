package com.framework.core.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.framework.core.constants.ReportConstants;
import com.framework.core.context.ExecutionContext;
import com.framework.core.lifecycle.ContextLifecycleManager;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ReportManager;
import com.framework.core.reporting.ScreenshotService;

/**
 * ============================================================================
 * Class Name : TestListener
 * ============================================================================
 *
 * Coordinates the framework lifecycle with TestNG execution.
 *
 * Responsibilities
 * ----------------
 * - Initialize reporting.
 * - Start framework lifecycle.
 * - Populate test metadata.
 * - Update execution report.
 * - Capture screenshots on failures.
 * - Release framework resources.
 *
 * This class acts only as an orchestration layer.
 *
 * ============================================================================
 */
public class TestListener
        implements ITestListener, ISuiteListener {

    private static final Logger log =
            LoggerFactory.getLogger(TestListener.class);

    private final ContextLifecycleManager lifecycle =
            new ContextLifecycleManager();

    @Override
    public void onStart(ISuite suite) {

        log.info("Suite Started : {}", suite.getName());

        /*
         * Temporary.
         *
         * Will be replaced by:
         *
         * ExecutionWorkspace
         *      -> ExecutionDirectories
         *      -> Report File
         */
        String reportPath =
                "target/AutomationExecutionReport/"
                        + ReportConstants.REPORT_FILE_NAME;

        ReportManager.initReport(reportPath);
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        log.info("Starting Test : {}", testName);

        ExtentTest extentTest =
                ReportManager.createTest(testName);

        ReportManager.setTest(extentTest);

        ExecutionContext context =
                lifecycle.start();

        context.getMetadataContext()
                .setTestName(testName);

        log.info("Framework initialized successfully.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        try {

            TestLogger.logStep("Test Passed");

            ReportManager.pass(result.getName());

        } finally {

            lifecycle.destroyContext();

            ReportManager.removeTest();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        try {

            TestLogger.logFailure(
                    "Test Failed",
                    result.getThrowable());

            ReportManager.fail(result.getThrowable());

            ScreenshotService.capture("Failure");

        } finally {

            lifecycle.destroyContext();

            ReportManager.removeTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        try {

            TestLogger.logWarning("Test Skipped");

            ReportManager.warn(result.getName());

        } finally {

            lifecycle.destroyContext();

            ReportManager.removeTest();
        }
    }

    @Override
    public void onFinish(ISuite suite) {

        log.info("Suite Finished : {}", suite.getName());

        ReportManager.flush();
    }
}