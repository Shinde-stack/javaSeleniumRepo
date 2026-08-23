package com.framework.core.execution;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * Class Name : ExecutionWorkspaceManager
 * ============================================================================
 *
 * Factory responsible for creating one ExecutionWorkspace.
 *
 * Responsibilities
 * ----------------
 * - Generate a unique execution identifier.
 * - Create the execution directory structure.
 * - Build an immutable ExecutionWorkspace.
 *
 * This class acts as the composition root for execution storage.
 *
 * Future Enhancements
 * -------------------
 * - ExecutionIdGenerator
 * - Cloud artifact storage
 * - ArtifactStorage abstraction
 * - Build/Pipeline execution IDs
 *
 * Flow
 * ----
 *
 * createWorkspace()
 *      │
 *      ▼
 * Generate Execution Id
 *      │
 *      ▼
 * ExecutionDirectoryManager
 *      │
 *      ▼
 * ExecutionDirectories
 *      │
 *      ▼
 * ExecutionWorkspace
 *
 * ============================================================================
 */
public class ExecutionWorkspaceManager {

    /**
     * Timestamp format used to generate execution identifiers.
     *
     * Example:
     * 20260816_194512
     */
    private static final DateTimeFormatter EXECUTION_ID_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Creates the execution directory structure.
     */
    private final ExecutionDirectoryManager executionDirectoryManager;

    public ExecutionWorkspaceManager(
            ExecutionDirectoryManager executionDirectoryManager) {

        this.executionDirectoryManager = executionDirectoryManager;
    }

    /**
     * Creates one execution workspace.
     *
     * @return Immutable ExecutionWorkspace representing the current execution.
     */
    public ExecutionWorkspace createWorkspace() {

        String executionId =
                generateExecutionId();

        ExecutionDirectories executionDirectories =
                executionDirectoryManager.initialize(executionId);

        return new ExecutionWorkspace(
                executionId,
                Instant.now(),
                executionDirectories);
    }

    /**
     * Generates a unique execution identifier.
     *
     * Future:
     * This logic can be delegated to an ExecutionIdGenerator without changing
     * callers of this class.
     */
    private String generateExecutionId() {

        return LocalDateTime.now()
                .format(EXECUTION_ID_FORMATTER);
    }
}