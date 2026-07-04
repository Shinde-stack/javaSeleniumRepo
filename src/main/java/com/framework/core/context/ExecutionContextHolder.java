package com.framework.core.context;

import com.framework.core.logging.TestLogger;

public final class ExecutionContextHolder {

    private ExecutionContextHolder() {
    }

    private static final ThreadLocal<ExecutionContext> CONTEXT =
            new ThreadLocal<>();

    public static void setContext(ExecutionContext context) {

    	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("TEMP---ExecutionContextHolder.setContext");

        if (context == null) {
            throw new IllegalArgumentException("ExecutionContext cannot be null");
        }

        CONTEXT.set(context);
    }

    /**
     * SAFE ACCESS LAYER
     *
     * DO NOT validate here.
     * Validation should happen in BaseTest or lifecycle manager.
     */
    public static ExecutionContext getContext() {
    	   // TEMP DEBUG LOG (REMOVE LATER)
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
    	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("TEMP---ExecutionContextHolder.removeContext");

        CONTEXT.remove();
    }
}