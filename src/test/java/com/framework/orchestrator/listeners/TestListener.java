//package com.framework.orchestrator.listeners;
//
//import com.aventstack.extentreports.ExtentTest;
//import com.framework.core.logging.TestLogger;
//import com.framework.core.reporting.ReportManager;
//import com.framework.core.reporting.ReportConstants;
//import com.framework.core.reporting.ScreenshotService;
//import org.testng.ITestListener;
//import org.testng.ITestResult;
//
///**
// * ============================================================================
// * Class Name : TestListener
// * ============================================================================
// *
// * Purpose:
// * --------
// * Central TestNG lifecycle controller for:
// * - Report creation
// * - Logging lifecycle events
// * - Failure handling
// * - Screenshot capture
// *
// * Responsibilities:
// * -----------------
// * 1. Create ExtentTest per test
// * 2. Bind logging context
// * 3. Capture failure screenshots
// * 4. Clean ThreadLocal state
// *
// * ============================================================================
// * EXECUTION FLOW
// * 
// * BaseTest
//   ↓
//ExecutionContext created
//   ↓
//Driver initialized
//   ↓
//TestListener.onTestStart()
//   ↓
//ExtentTest created
//   ↓
//ReportManager.setTest(test)   ← CRITICAL
//   ↓
//Test execution
//   ↓
//TestLogger → ReportManager.getTest()
//   ↓
//HTML report updated safely per thread
// * 
// */
//
////5. How everything connects (execution flow)
////TestNG
////  ↓
////TestListener.onTestStart
////  ↓
////ReportManager.createTest
////  ↓
////TestLogger binds ExtentTest
////  ↓
////Test executes
////  ↓
////TestLogger logs steps
////  ↓
////Failure?
////   ↓
////ScreenshotService.capture
////   ↓
////ReportManager.attachScreenshot
////  ↓
////onTestFinish → removeTest()
//
//
//public class TestListener implements ITestListener {
//
//    @Override
//    public void onTestStart(ITestResult result) {
//
//        String testName = result.getMethod().getMethodName();
//
//        ReportManager.createTest(testName);
//
//        ExtentTest test = ReportManager.getTest();
//
//        ReportManager.setTest(test);   // ONLY THIS
//        
//        TestLogger.logStep("Test Started: " + testName);
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//
//        TestLogger.logStep("Test Passed");
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//
//        String testName = result.getMethod().getMethodName();
//
//        TestLogger.logFailure("Test Failed", result.getThrowable());
//
//        String path =
//                ScreenshotService.capture(testName);
//
//        ReportManager.addScreenshot(path);
//    }
//
// //   @Override
//    public void onTestFinish(ITestResult result) {
//
//        ReportManager.removeTest();
//    }
//}



package com.framework.orchestrator.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.framework.core.reporting.ReportManager;
import com.framework.core.assertion.AssertionEngine;
import com.framework.core.logging.TestLogger;

/**
 * ============================================================================
 * Class Name : TestExecutionListener
 * ============================================================================
 *
 * PURPOSE:
 * --------
 * Central lifecycle controller for test execution.
 *
 * RESPONSIBILITIES:
 * -----------------
 * 1. Initialize test reporting (ExtentTest)
 * 2. Capture test start/end lifecycle events
 * 3. Trigger AssertionEngine.assertAll() automatically
 * 4. Handle final test status (PASS/FAIL/SKIP)
 * 5. Attach logs + screenshots on failure
 *
 * ============================================================================
 *
 * DESIGN PATTERN:
 * ---------------
 * Observer Pattern (TestNG Listener Model)
 *
 * WHY THIS EXISTS:
 * ---------------
 * Removes test-level lifecycle boilerplate:
 *
 * ❌ No more assertAll() in test
 * ❌ No manual report flush per test
 *
 * ============================================================================
 */
public class TestListener implements ITestListener {

    private final AssertionEngine assertionEngine =
            new AssertionEngine();

    // ------------------------------------------------------------
    // TEST START
    // ------------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
 		TestLogger.logStep("onTestStart");

        String testName =
                result.getMethod().getMethodName();

        ReportManager.createTest(testName);

        TestLogger.logStep("STARTING TEST: " + testName);
    }

    // ------------------------------------------------------------
    // TEST SUCCESS
    // ------------------------------------------------------------
    @Override
    public void onTestSuccess(ITestResult result) {
 		TestLogger.logStep("onTestSuccess");

        TestLogger.logStep("TEST PASSED");

        cleanupAssertions();
        ReportManager.pass("Test Passed");
    }

    // ------------------------------------------------------------
    // TEST FAILURE
    // ------------------------------------------------------------
    @Override
    public void onTestFailure(ITestResult result) {
 		TestLogger.logStep("onTestFailure");

        TestLogger.logFailure(
                "TEST FAILED",
                result.getThrowable());

        cleanupAssertions();
    }

    // ------------------------------------------------------------
    // TEST SKIP
    // ------------------------------------------------------------
    @Override
    public void onTestSkipped(ITestResult result) {
 		TestLogger.logStep("onTestSkipped");

        TestLogger.logStep("TEST SKIPPED");

        cleanupAssertions();
    }

    // ------------------------------------------------------------
    // CENTRALIZED ASSERTION FINALIZATION
    // ------------------------------------------------------------
    private void cleanupAssertions() {
 		TestLogger.logStep("cleanupAssertions");

        try {
            assertionEngine.assertAll();
     		TestLogger.logStep("===assertAll=== done");

        } catch (AssertionError e) {

            TestLogger.logFailure(
                    "SOFT ASSERTION FAILURE",
                    e);

            ReportManager.fail(e.getMessage());
        }

        assertionEngine.clear();

        ReportManager.removeTest();
    }

    // ------------------------------------------------------------
    // SUITE LEVEL
    // ------------------------------------------------------------
    @Override
    public void onFinish(ITestContext context) {
 		TestLogger.logStep("onFinish");

        ReportManager.flush();
    }
}