package com.framework.core.execution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.framework.core.constants.ExecutionConstants;
import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : ExecutionDirectoryManager
 * ============================================================================
 *
 * Creates the local directory structure for one automation execution.
 *
 * Responsibilities
 * ----------------
 * - Create the execution root directory.
 * - Create all artifact sub-directories.
 * - Return an immutable ExecutionDirectories object.
 *
 * This class does NOT:
 * - Generate execution IDs.
 * - Generate reports.
 * - Capture screenshots.
 * - Upload artifacts.
 * - Clean old executions.
 *
 * Flow
 * ----
 *
 * ExecutionWorkspaceManager
 *          │
 *          ▼
 * initialize(executionId)
 *          │
 *          ▼
 * target/
 *   automation/
 *      executions/
 *          <executionId>/
 *              report/
 *              screenshots/
 *              logs/
 *              downloads/
 *              videos/
 *              temp/
 *
 * ============================================================================
 */
public class ExecutionDirectoryManager {

    /**
     * Creates the complete execution workspace.
     *
     * @param executionId Unique execution identifier.
     *
     * @return Immutable ExecutionDirectories.
     */
    public ExecutionDirectories initialize(String executionId) {

        try {

            Path executionDirectory =
                    createExecutionDirectory(executionId);

            Path reportDirectory =
                    createSubDirectory(
                            executionDirectory,
                            ExecutionConstants.REPORT_DIRECTORY_NAME);

            Path screenshotDirectory =
                    createSubDirectory(
                            executionDirectory,
                            ExecutionConstants.SCREENSHOT_DIRECTORY_NAME);

            Path logDirectory =
                    createSubDirectory(
                            executionDirectory,
                            ExecutionConstants.LOG_DIRECTORY_NAME);

            Path downloadDirectory =
                    createSubDirectory(
                            executionDirectory,
                            ExecutionConstants.DOWNLOAD_DIRECTORY_NAME);

            Path videoDirectory =
                    createSubDirectory(
                            executionDirectory,
                            ExecutionConstants.VIDEO_DIRECTORY_NAME);

            Path tempDirectory =
                    createSubDirectory(
                            executionDirectory,
                            ExecutionConstants.TEMP_DIRECTORY_NAME);

            return new ExecutionDirectories(
                    executionDirectory,
                    reportDirectory,
                    screenshotDirectory,
                    logDirectory,
                    downloadDirectory,
                    videoDirectory,
                    tempDirectory);

        } catch (IOException ex) {

            throw new FrameworkException(
                    "Failed to create execution directory structure for execution: "
                            + executionId,
                    ex);
        }
    }

    /**
     * Creates the root directory for one execution.
     */
    private Path createExecutionDirectory(String executionId)
            throws IOException {

        Path executionDirectory =
                Paths.get(
                        ExecutionConstants.TARGET_DIRECTORY,
                        ExecutionConstants.AUTOMATION_DIRECTORY,
                        ExecutionConstants.EXECUTIONS_DIRECTORY,
                        executionId);

        Files.createDirectories(executionDirectory);

        return executionDirectory;
    }

    /**
     * Creates one child directory under the execution workspace.
     */
    private Path createSubDirectory(
            Path parentDirectory,
            String childDirectory)
            throws IOException {

        Path directory =
                parentDirectory.resolve(childDirectory);

        Files.createDirectories(directory);

        return directory;
    }
}