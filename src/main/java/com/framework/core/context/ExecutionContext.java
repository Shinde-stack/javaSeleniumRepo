package com.framework.core.context;

import com.framework.core.config.EnvConfig;
import com.framework.core.execution.ExecutionWorkspace;

/**
 * ============================================================================
 * Class Name : ExecutionContext
 * ============================================================================
 *
 * Runtime state for ONE test-method execution.
 *
 * One ExecutionContext is bound to one executing thread through
 * ExecutionContextHolder.
 *
 * Responsibilities:
 *
 * - DriverContext
 * - TestMetadataContext
 * - EnvConfig
 * - ExecutionWorkspace
 * - Lifecycle state
 *
 * This is a pure framework state container.
 *
 * It must NOT depend on:
 * - TestNG
 * - ExtentReports
 * - Selenium-specific lifecycle logic
 *
 * ============================================================================
 */
public class ExecutionContext {

    /**
     * Current lifecycle state.
     */
    private ContextState state;

    /**
     * Driver state belonging to this execution.
     */
    private final DriverContext driverContext;

    /**
     * Metadata belonging to this execution.
     */
    private final TestMetadataContext metadataContext;

    /**
     * Environment/framework configuration.
     */
    private EnvConfig config;

    /**
     * Shared execution workspace.
     *
     * This may be shared across tests depending on the execution model.
     */
    private final ExecutionWorkspace executionWorkspace;

    public ExecutionContext(
            ExecutionWorkspace executionWorkspace) {

        if (executionWorkspace == null) {
            throw new IllegalArgumentException(
                    "ExecutionWorkspace cannot be null.");
        }

        this.state = ContextState.CREATED;
        this.driverContext = new DriverContext();
        this.metadataContext = new TestMetadataContext();
        this.executionWorkspace = executionWorkspace;
    }

    public void setState(ContextState state) {
        this.state = state;
    }

    public void setConfig(EnvConfig config) {
        this.config = config;
    }

    public ContextState getState() {
        return state;
    }

    public DriverContext getDriverContext() {
        return driverContext;
    }

    public TestMetadataContext getMetadataContext() {
        return metadataContext;
    }

    public EnvConfig getConfig() {
        return config;
    }

    public ExecutionWorkspace getExecutionWorkspace() {
        return executionWorkspace;
    }

    public boolean hasDriver() {
        return driverContext.getDriver() != null;
    }
}