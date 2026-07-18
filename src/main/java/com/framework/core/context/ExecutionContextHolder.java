package com.framework.core.context;

import com.framework.core.logging.TestLogger;

/**
 * ExecutionContextHolder
 *
 * ThreadLocal registry for the active ExecutionContext on the current test thread.
 *
 * Flow:
 *   ContextLifecycleManager.initializeContext() → setContext()
 *   → BasePage / TestLogger / ScreenshotService → getContext()
 *   → cleanupContext() → removeContext()
 *
 * Throws IllegalStateException if getContext() is called before initialization.
 */
public final class ExecutionContextHolder {

    private ExecutionContextHolder() {
    }

    private static final ThreadLocal<ExecutionContext> CONTEXT =
            new ThreadLocal<>();

    public static void setContext(ExecutionContext context) {

        TestLogger.logStep("TEMP---ExecutionContextHolder.setContext");

        if (context == null) {
            throw new IllegalArgumentException("ExecutionContext cannot be null");
        }

        CONTEXT.set(context);
    }

    public static ExecutionContext getContext() {
    //    TestLogger.logStep("TEMP---ExecutionContextHolder.getContext");

        ExecutionContext context = CONTEXT.get();

        if (context == null) {
            throw new IllegalStateException(
                    "ExecutionContext not initialized for thread: "
                            + Thread.currentThread().getName());
        }

        return context;
    }

    public static void removeContext() {
        TestLogger.logStep("TEMP---ExecutionContextHolder.removeContext");

        CONTEXT.remove();
    }
}
