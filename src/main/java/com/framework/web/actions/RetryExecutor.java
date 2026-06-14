package com.framework.web.actions;

import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * ============================================================================
 * RetryExecutor
 * ============================================================================
 *
 * Responsibility:
 * - Executes actions with retry support
 * - Handles transient UI failures (stale element, DOM refresh)
 * ============================================================================
 */
public class RetryExecutor {

    private static final int MAX_RETRY = 3;
    private static final long BACKOFF_MS = 300;

    /**
     * Executes a Selenium action safely with retry.
     */
    public static void execute(Supplier<Void> action) {

        int attempt = 0;

        while (attempt < MAX_RETRY) {
            try {
                action.get();
                return;
            }
            catch (StaleElementReferenceException e) {
                attempt++;

                if (attempt == MAX_RETRY) {
                    throw e;
                }

                sleep(BACKOFF_MS * attempt);
            }
        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}