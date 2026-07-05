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
        

        // -----------------------------------------------------
        // STEP 1
        // Reporting FIRST
        // -----------------------------------------------------

        ExtentTest test = ReportManager.createTest(testName);

        ReportManager.setTest(test);

        // -----------------------------------------------------
        // STEP 2
        // Framework lifecycle
        // -----------------------------------------------------

//        ExecutionContext context =
//                lifecycle.initializeContext();
//
//        lifecycle.initDriver(context);
//
//        lifecycle.launchApplication(context);
        
        lifecycle.start();

        // -----------------------------------------------------
        // STEP 3
        // Framework ready
        // -----------------------------------------------------

        TestLogger.logStep("Framework initialization completed");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        TestLogger.logStep("onTestSuccess-Test Passed");

        ReportManager.pass(result.getName());
        
        lifecycle.cleanupContext();
        ReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {

        TestLogger.logFailure(
                "onTestFailure -Test Failed",
                result.getThrowable());

        ReportManager.fail(result.getThrowable());

        /*
         * Future:
         * ScreenshotService.capture()
         */
        
        TestLogger.logWarning(result.getTestName());
      String screenShotPath =  ScreenshotService.capture(result.getMethod().getMethodName());// ISSUE - name is always NULL
        ReportManager.addScreenshot(screenShotPath);
        
        lifecycle.cleanupContext();
        ReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        TestLogger.logStep("onTestSkipped-Test Skipped");

        ReportManager.info(result.getName());
        
        lifecycle.cleanupContext();
        ReportManager.removeTest();
    }

    @Override
    public void onFinish(ISuite suite) {

        log.info("onFinish-Suite Finished");

        ReportManager.flush();
    }
}