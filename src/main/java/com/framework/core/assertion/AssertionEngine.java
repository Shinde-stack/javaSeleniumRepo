package com.framework.core.assertion;

import java.util.ArrayList;
import java.util.List;

import com.framework.core.logging.TestLogger;

/**
 * AssertionEngine
 *
 * Central assertion layer supporting hard stop and soft collect-then-fail modes.
 *
 * Flow:
 *   assertTrue(condition, ..., HARD)  → fail immediately with AssertionError
 *   assertTrue(condition, ..., SOFT)  → record failure, continue test
 *   assertAll() at end                → throw if any soft failures were collected
 *   clear() after test                → reset ThreadLocal state (should be called by listener)
 */
public class AssertionEngine {

    private final ThreadLocal<List<String>> softFailures =
            ThreadLocal.withInitial(ArrayList::new);

    public void assertTrue(
            boolean condition,
            String successMessage,
            String failureMessage,
            Severity severity) {

        if (condition) {

            TestLogger.logPass(successMessage);
            return;
        }

        TestLogger.logFailure(
                failureMessage,
                new AssertionError(failureMessage));

        if (severity == Severity.SOFT) {

            softFailures.get().add(failureMessage);
            return;
        }

        throw new AssertionError(failureMessage);
    }

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

    public void clear() {

        softFailures.get().clear();

        softFailures.remove();
    }
}
