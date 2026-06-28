package com.framework.core.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.framework.core.context.ExecutionContext;
import com.framework.core.lifecycle.ContextLifecycleManager;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ReportConstants;
import com.framework.core.reporting.ReportManager;

public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log =
            LoggerFactory.getLogger(TestListener.class);

    private final ContextLifecycleManager lifecycle =
            new ContextLifecycleManager();

    @Override
    public void onStart(ISuite suite) {

        log.info("Suite Started");

        String reportPath =ReportConstants.REPORT_DIR + ReportConstants.REPORT_FILE_NAME;

        ReportManager.initReport(reportPath);
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        log.info("Starting Test : {}", testName);

        /*
         * STEP 1
         * Create Extent Test FIRST.
         */

        ExtentTest extentTest =
                ReportManager.createTest(testName);

        ReportManager.setTest(extentTest);

        /*
         * STEP 2
         * Initialize framework.
         */

        ExecutionContext context =
                lifecycle.initializeContext();

        lifecycle.initDriver(context);

        /*
         * STEP 3
         * Safe to use TestLogger now.
         */

        TestLogger.logStep("Framework initialized");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        TestLogger.logStep("Test Passed");

        ReportManager.pass(result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        TestLogger.logFailure(
                "Test Failed",
                result.getThrowable());

        ReportManager.fail(result.getThrowable());

        /*
         * Future:
         * ScreenshotService.capture()
         */
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        TestLogger.logStep("Test Skipped");

        ReportManager.info(result.getName());
    }

    @Override
    public void onFinish(ISuite suite) {

        log.info("Suite Finished");

        ReportManager.flush();
    }
}