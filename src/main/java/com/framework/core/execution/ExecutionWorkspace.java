package com.framework.core.execution;

import java.time.Instant;

/**
 * ============================================================================
 * Class Name : ExecutionWorkspace
 * ============================================================================
 *
 * Represents one complete automation execution.
 *
 * Responsibilities
 * ----------------
 * - Hold execution metadata.
 * - Hold execution directories.
 *
 * This object is immutable and shared through the ExecutionContext.
 *
 * ============================================================================
 */
public final class ExecutionWorkspace {

    /**
     * Unique identifier for the current execution.
     *
     * Example:
     * 20260816_193245
     */
    private final String executionId;

    /**
     * Timestamp when execution started.
     */
    private final Instant startTime;

    /**
     * Local execution artifact directories.
     */
    private final ExecutionDirectories executionDirectories;

    public ExecutionWorkspace(
            String executionId,
            Instant startTime,
            ExecutionDirectories executionDirectories) {

        this.executionId = executionId;
        this.startTime = startTime;
        this.executionDirectories = executionDirectories;
    }

    public String getExecutionId() {
        return executionId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    /**
     * Returns all directories belonging to this execution.
     */
    public ExecutionDirectories getExecutionDirectories() {
        return executionDirectories;
    }

    @Override
    public String toString() {

        return "ExecutionWorkspace{" +
                "executionId='" + executionId + '\'' +
                ", startTime=" + startTime +
                ", executionDirectories=" + executionDirectories +
                '}';
    }
}