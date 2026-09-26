package com.framework.core.listeners;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.context.TestMetadataContext;
import com.framework.core.execution.ExecutionWorkspace;
import com.framework.core.execution.ExecutionWorkspaceManager;
import com.framework.core.lifecycle.ContextLifecycleManager;
import com.framework.core.reporting.ReportManager;
import com.framework.core.reporting.ScreenshotService;

/**
 * ============================================================================
 * Class Name : TestListener
 * ============================================================================
 *
 * PURPOSE
 * -------
 * TestNG integration layer.
 *
 * Responsibilities:
 *
 * Suite level:
 * ----------------
 * - Create ExecutionWorkspace.
 * - Initialize reporting.
 * - Flush reporting.
 *
 * Test level:
 * ----------------
 * - Start framework lifecycle.
 * - Populate test metadata.
 * - Create ExtentTest.
 * - Mark test result.
 * - Cleanup framework resources.
 *
 * IMPORTANT
 * ---------
 * This is the ONLY class that knows about:
 *
 * - TestNG
 * - ExecutionWorkspace lifecycle
 * - Reporting lifecycle
 *
 * ContextLifecycleManager does NOT know about TestNG or reporting.
 *
 * ============================================================================
 */
public class TestListener
        implements ITestListener, ISuiteListener {


    private static final Logger log =
            LoggerFactory.getLogger(
                    TestListener.class);


    /**
     * Framework lifecycle manager.
     *
     * Handles ExecutionContext + WebDriver.
     */
    private final ContextLifecycleManager lifecycle =
            new ContextLifecycleManager();


    /**
     * Creates the physical execution directories.
     */
    private final ExecutionWorkspaceManager workspaceManager =
            new ExecutionWorkspaceManager();


    /**
     * One workspace for the complete suite/run.
     *
     * Example:
     *
     * target/framework-runs/
     *     20260823-213500-a81f42c1/
     *
     * All tests in this suite use this workspace.
     */
    private ExecutionWorkspace workspace;


    // =========================================================================
    // SUITE START
    // =========================================================================

    @Override
    public void onStart(ISuite suite) {

        log.info(
                "Suite started: {}",
                suite.getName());


        // =====================================================================
        // STEP 1: Create execution workspace
        // =====================================================================

        workspace =
                workspaceManager.createWorkspace();


        log.info(
                "Execution workspace created: {}",
                workspace.getRootDirectory());


        // =====================================================================
        // STEP 2: Initialize reporting
        // =====================================================================

        ReportManager.init(workspace);


        log.info(
                "Reporting initialized.");
    }


    // =========================================================================
    // TEST START
    // =========================================================================

    @Override
    public void onTestStart(
            ITestResult result) {

        String methodName =
                result.getMethod()
                        .getMethodName();


        log.info(
                "Test started: {} | thread={}",
                methodName,
                Thread.currentThread().getName());


        // =====================================================================
        // STEP 1: Start framework
        // =====================================================================

        /*
         * Creates:
         *
         * ExecutionContext
         * Driver
         * Application session
         */
        ExecutionContext context =
                lifecycle.start(workspace);


        // =====================================================================
        // STEP 2: Populate TestNG metadata
        // =====================================================================

        populateMetadata(
                result,
                context);


        // =====================================================================
        // STEP 3: Create ExtentTest
        // =====================================================================

        ExtentTest extentTest =
                ReportManager.createTest(
                        buildTestName(result));


        // =====================================================================
        // STEP 4: Bind ExtentTest to current thread
        // =====================================================================

        ReportManager.setTest(
                extentTest);


        // =====================================================================
        // STEP 5: Log test start
        // =====================================================================

        ReportManager.info(
                "Test started"
                + " | method=" + methodName
                + " | thread=" + Thread.currentThread().getName()
                + " | threadId=" + context
                        .getMetadataContext()
                        .getThreadId()
                + " | correlationId=" + context
                        .getMetadataContext()
                        .getCorrelationId());


        log.info(
                "Test framework initialization completed: {}",
                methodName);
    }


    // =========================================================================
    // TEST SUCCESS
    // =========================================================================

    @Override
    public void onTestSuccess(
            ITestResult result) {

        try {

            ReportManager.pass(
                    "Test Passed: "
                    + buildTestName(result));

        } finally {

            cleanup();
        }
    }


    // =========================================================================
    // TEST FAILURE
    // =========================================================================

    @Override
    public void onTestFailure(
            ITestResult result) {

        try {

            ReportManager.fail(
                    result.getThrowable());
            
        	//temporary
             ScreenshotService screenshotService =
                    new ScreenshotService();
            
             ExecutionContext context =
     	            ExecutionContextHolder.get();

     	    WebDriver driver =
     	            context.getDriverContext().getDriver();

     	    ExecutionWorkspace workspace =
     	            context.getExecutionWorkspace();

            screenshotService.captureAndAttach(
		            driver,
		            workspace,
		            "sc uuuuuu page");
		

        } finally {

            cleanup();
        }
    }


    // =========================================================================
    // TEST SKIPPED
    // =========================================================================

    @Override
    public void onTestSkipped(
            ITestResult result) {

        try {

            ReportManager.info(
                    "Test Skipped: "
                    + buildTestName(result));

        } finally {

            cleanup();
        }
    }


    // =========================================================================
    // TEST FINISH
    // =========================================================================

    @Override
    public void onTestFailedButWithinSuccessPercentage(
            ITestResult result) {

        /*
         * No special handling required for now.
         *
         * Keep this method intentionally empty.
         */
    }


    // =========================================================================
    // SUITE FINISH
    // =========================================================================

    @Override
    public void onFinish(
            ISuite suite) {

        log.info(
                "Suite finished: {}",
                suite.getName());


        /*
         * Flush the complete ExtentReports instance.
         */
        ReportManager.flush();


        /*
         * ThreadLocal inside ReportManager must also be cleaned.
         */
        ReportManager.removeTest();


        log.info(
                "Reporting flushed successfully.");
    }


    // =========================================================================
    // CLEANUP
    // =========================================================================

    /**
     * Cleans framework resources after every test.
     *
     * IMPORTANT:
     *
     * lifecycle.destroyContext()
     * is responsible for:
     *
     * - WebDriver cleanup
     * - ExecutionContext cleanup
     * - ThreadLocal cleanup
     *
     * ReportManager.removeTest()
     * is responsible for:
     *
     * - ExtentTest ThreadLocal cleanup
     */
    private void cleanup() {

        try {

            lifecycle.destroyContext();

        } finally {

            /*
             * Always remove ExtentTest from ThreadLocal.
             *
             * TestNG worker threads can be reused in parallel execution.
             */
            ReportManager.removeTest();
        }
    }


    // =========================================================================
    // METADATA
    // =========================================================================

    /**
     * Copies TestNG information into framework metadata.
     */
    private void populateMetadata(
            ITestResult result,
            ExecutionContext context) {

        TestMetadataContext metadata =
                context.getMetadataContext();


        /*
         * TestNG <test name="...">
         */
        metadata.setTestngTestName(
                result.getTestContext()
                        .getCurrentXmlTest()
                        .getName());


        /*
         * Java test class.
         */
        metadata.setClassName(
                result.getTestClass()
                        .getName());


        /*
         * Java test method.
         */
        metadata.setMethodName(
                result.getMethod()
                        .getMethodName());


        /*
         * Environment.
         *
         * This requires:
         *
         * EnvConfig.getEnvironmentType()
         *
         * and:
         *
         * TestMetadataContext.setEnvironment()
         */
        metadata.setEnvironment(
                context.getConfig()
                        .getEnvironmentType());
    }


    // =========================================================================
    // TEST NAME
    // =========================================================================

    /**
     * Creates a useful report test name.
     *
     * Example:
     *
     * LoginTest.verifyValidLogin
     */
    private String buildTestName(
            ITestResult result) {

        return result.getTestClass()
                .getName()
                + "."
                + result.getMethod()
                        .getMethodName();
    }
}