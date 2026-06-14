package com.framework.core.validation;

import java.util.ArrayList;
import java.util.List;

import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ReportManager;

/**
 * ============================================================================
 * Class Name : AssertionEngine
 * ============================================================================
 *
 * PURPOSE:
 * --------
 * Centralized assertion framework for UI automation tests.
 *
 * This class replaces direct usage of:
 * - TestNG Assert
 * - JUnit Assert
 * - Manual if/else validation in tests
 *
 * It provides:
 * - Hard assertions (fail fast)
 * - Soft assertions (collect & fail at end)
 * - Integrated logging (console + report)
 *
 * ============================================================================
 *
 * DESIGN APPROACH:
 * ----------------
 * Pattern Used:
 * - Facade Pattern → Simplifies assertion usage for tests
 * - Centralized Control Flow → Single entry point for validation
 *
 * Why Facade?
 * ----------
 * Test classes should NOT handle:
 * - logging
 * - reporting
 * - exception management
 *
 * Instead they call:
 * AssertionEngine.assertTrue(...)
 *
 * ============================================================================
 *
 * FLOW OF EXECUTION:
 * ------------------
 *
 * Test Method
 *      ↓
 * AssertionEngine.assertTrue()
 *      ↓
 * Evaluate Condition
 *      ↓
 * ┌───────────────┬───────────────────┐
 * │ TRUE          │ FALSE             │
 * └───────────────┴───────────────────┘
 *        ↓                 ↓
 *   log success       log failure
 *   report PASS       report FAIL
 *                     ↓
 *             HARD → throw exception
 *             SOFT → store failure
 *
 * After Test:
 *      ↓
 * assertAll() called
 *      ↓
 * Fail test if soft assertions exist
 *
 * ============================================================================
 *
 * THREAD SAFETY NOTE:
 * -------------------
 * Current implementation uses instance-level list.
 * For parallel execution, this should later be upgraded to ThreadLocal.
 *
 * ============================================================================
 */
public class AssertionEngine {

    /**
     * Stores soft assertion failures.
     *
     * Why list?
     * ---------
     * Allows multiple validation failures
     * instead of stopping at first failure.
     */
    private final List<String> softFailures = new ArrayList<>();

    // =========================================================================
    // HARD + SOFT ASSERT ENTRY POINT
    // =========================================================================

    /**
     * Core assertion method for boolean validation.
     *
     * USE CASE:
     * ---------
     * Validate UI conditions, API responses, DB results.
     *
     * @param condition      actual boolean result
     * @param successMessage message logged on success
     * @param failureMessage message logged on failure
     * @param severity       HARD or SOFT
     */
    public void assertTrue(
            boolean condition,
            String successMessage,
            String failureMessage,
            Severity severity) {

        // ---------------------------------------------------------
        // STEP 1: Evaluate condition
        // ---------------------------------------------------------
        if (condition) {

            // SUCCESS PATH
            TestLogger.logStep(successMessage);
            ReportManager.pass(successMessage);
            return;
        }

        // ---------------------------------------------------------
        // STEP 2: FAILURE PATH (common logic)
        // ---------------------------------------------------------
        TestLogger.logFailure(
                failureMessage,
                new AssertionError(failureMessage));

        ReportManager.fail(failureMessage);

        // ---------------------------------------------------------
        // STEP 3: SOFT ASSERT HANDLING
        // ---------------------------------------------------------
        if (severity == Severity.SOFT) {

            softFailures.add(failureMessage);
            return;
        }

        // ---------------------------------------------------------
        // STEP 4: HARD ASSERT HANDLING
        // ---------------------------------------------------------
        throw new AssertionError(failureMessage);
    }

    // =========================================================================
    // SOFT ASSERT FINALIZATION
    // =========================================================================

    /**
     * Validates all collected soft assertions.
     *
     * MUST BE CALLED:
     * ---------------
     * At end of test method OR in AfterMethod listener.
     *
     * FAILURE STRATEGY:
     * -----------------
     * If any soft failures exist:
     * - Mark test as failed
     * - Attach aggregated failure message
     */
    public void assertAll() {

        // ---------------------------------------------------------
        // STEP 1: Check collected failures
        // ---------------------------------------------------------
        if (softFailures.isEmpty()) {
            TestLogger.logStep("All soft assertions passed");
            return;
        }

        // ---------------------------------------------------------
        // STEP 2: Build failure summary
        // ---------------------------------------------------------
        String message =
                "SOFT ASSERTION FAILURES:\n"
                        + String.join("\n", softFailures);

        // ---------------------------------------------------------
        // STEP 3: Log failure
        // ---------------------------------------------------------
        TestLogger.logFailure(message, new AssertionError(message));

        ReportManager.fail(message);

        // ---------------------------------------------------------
        // STEP 4: Fail test execution
        // ---------------------------------------------------------
        throw new AssertionError(message);
    }

    // =========================================================================
    // CLEANUP (IMPORTANT FOR REUSE)
    // =========================================================================

    /**
     * Clears stored soft failures.
     *
     * SHOULD BE CALLED:
     * -----------------
     * After each test execution to avoid memory leakage.
     */
    public void clear() {
        softFailures.clear();
    }
}