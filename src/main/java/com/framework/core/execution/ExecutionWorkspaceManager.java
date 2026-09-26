package com.framework.core.execution;

/**
 * ============================================================================
 * Class Name : ExecutionWorkspaceManager
 * ============================================================================
 *
 * Coordinates creation of execution workspaces.
 *
 * Keeps TestListener independent from the physical directory creation logic.
 *
 * ============================================================================
 */
public class ExecutionWorkspaceManager {

    private final ExecutionDirectoryManager directoryManager;


    public ExecutionWorkspaceManager() {

        this.directoryManager =
                new ExecutionDirectoryManager();
    }


    public ExecutionWorkspace createWorkspace() {

        return directoryManager.createWorkspace();
    }
}