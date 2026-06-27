package com.framework.core.assertion;

import java.util.ArrayList;
import java.util.List;

import com.framework.core.logging.TestLogger;

/**
 * ============================================================================
 * Class Name : AssertionEngine
 * ============================================================================
 *
 * Purpose:
 * --------
 * Centralized assertion layer.
 *
 * Supports:
 * - Hard assertions
 * - Soft assertions
 * - Integrated framework logging
 *
 * Design Pattern:
 * ---------------
 * Facade Pattern
 *
 * Test classes interact only with AssertionEngine.
 *
 * ============================================================================
 */
public class AssertionEngine {

    /**
     * Thread-safe storage for soft assertion failures.
     *
     * One failure list per execution thread.
     */
    private final ThreadLocal<List<String>> softFailures =
            ThreadLocal.withInitial(ArrayList::new);

    /**
     * Core boolean assertion.
     *
     * @param condition       actual validation result
     * @param successMessage  message when validation passes
     * @param failureMessage  message when validation fails
     * @param severity        HARD or SOFT
     */
    public void assertTrue(
            boolean condition,
            String successMessage,
            String failureMessage,
            Severity severity) {

        // ---------------------------------------------------------
        // PASS
        // ---------------------------------------------------------
        if (condition) {

            TestLogger.logPass(successMessage);
            return;
        }

        // ---------------------------------------------------------
        // FAIL
        // ---------------------------------------------------------
        TestLogger.logFailure(
                failureMessage,
                new AssertionError(failureMessage));

        // ---------------------------------------------------------
        // SOFT ASSERT
        // ---------------------------------------------------------
        if (severity == Severity.SOFT) {

            softFailures.get().add(failureMessage);
            return;
        }

        // ---------------------------------------------------------
        // HARD ASSERT
        // ---------------------------------------------------------
        throw new AssertionError(failureMessage);
    }

    /**
     * Evaluates collected soft assertions.
     *
     * Usually executed automatically
     * by AssertionListener.
     */
    public void assertAll() {

        if (softFailures.get().isEmpty()) {

            TestLogger.logPass(
                    "All soft assertions passed");

            return;
        }

        String summary =
                "Soft assertion failures:\n"
                        + String.join(
                                System.lineSeparator(),
                                softFailures.get());

        TestLogger.logFailure(
                summary,
                new AssertionError(summary));

        throw new AssertionError(summary);
    }

    /**
     * Clears thread-local assertion state.
     *
     * Must execute after every test.
     */
    public void clear() {

        softFailures.get().clear();

        softFailures.remove();
    }
}