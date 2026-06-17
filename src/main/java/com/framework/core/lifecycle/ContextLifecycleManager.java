package com.framework.core.lifecycle;

import com.framework.core.config.ExecutionContext;
import com.framework.core.context.ContextState;

/**
 * Controls context lifecycle transitions.
 *
 * Single place responsible for
 * changing context states.
 */
public class ContextLifecycleManager {

    /**
     * Context initialized.
     */
    public void initialize(ExecutionContext context) {

        context.setState(ContextState.INITIALIZED);
    }

    /**
     * Test execution started.
     */
    public void startExecution(ExecutionContext context) {

        context.setState(ContextState.RUNNING);
    }

    /**
     * Cleanup started.
     */
    public void startCleanup(ExecutionContext context) {

        context.setState(ContextState.CLEANING_UP);
    }

    /**
     * Context destroyed.
     */
    public void destroy(ExecutionContext context) {

        context.driver().quitDriver();

        context.db().closeConnection();

        context.setState(ContextState.DESTROYED);
    }
}
