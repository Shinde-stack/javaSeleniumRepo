package com.framework.core.context;

import com.framework.core.config.EnvConfig;
import com.framework.core.execution.ExecutionWorkspace;

/**
 * ============================================================================
 * Class Name : ExecutionContext
 * ============================================================================
 *
 * Represents all runtime state for a single test execution.
 *
 * Responsibilities
 * ----------------
 * - Hold DriverContext.
 * - Hold TestMetadataContext.
 * - Hold framework configuration.
 * - Hold ExecutionWorkspace.
 * - Maintain execution lifecycle state.
 *
 * This class is a pure state container.
 *
 * One ExecutionContext exists per executing test thread.
 *
 * ============================================================================
 */
public class ExecutionContext {

    /**
     * Current lifecycle state.
     */
    private ContextState state;

    /**
     * Driver state for this execution.
     */
    private final DriverContext driverContext;

    /**
     * Test metadata.
     */
    private final TestMetadataContext metadataContext;

    /**
     * Framework configuration.
     */
    private EnvConfig config;

    /**
     * Shared execution workspace.
     *
     * Every test executed within the same suite references the same workspace.
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

    public ContextState getState() {
        return state;
    }

    public void setState(ContextState state) {
        this.state = state;
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

    public void setConfig(EnvConfig config) {
        this.config = config;
    }

    public ExecutionWorkspace getExecutionWorkspace() {
        return executionWorkspace;
    }

    public boolean hasDriver() {
        return driverContext.getDriver() != null;
    }

    @Override
    public String toString() {

        return "ExecutionContext{" +
                "state=" + state +
                ", executionId=" + executionWorkspace.getExecutionId() +
                ", testName=" + metadataContext.getTestName() +
                ", correlationId=" + metadataContext.getCorrelationId() +
                '}';
    }
}