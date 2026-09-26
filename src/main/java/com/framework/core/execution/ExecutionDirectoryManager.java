package com.framework.core.execution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : ExecutionDirectoryManager
 * ============================================================================
 *
 * Creates physical directories for one framework execution.
 *
 * Directory structure:
 *
 * target/
 *   framework-runs/
 *      yyyyMMdd-HHmmss-xxxxxxxx/
 *          reports/
 *          screenshots/
 *          logs/
 *          videos/
 *
 * ============================================================================
 */
public class ExecutionDirectoryManager {

    private static final DateTimeFormatter RUN_FORMAT =
            DateTimeFormatter.ofPattern(
                    "yyyyMMdd-HHmmss");


    public ExecutionWorkspace createWorkspace() {

        try {

            // =================================================================
            // Root execution directory
            // =================================================================

            Path rootDirectory =
                    Paths.get(
                            "target",
                            "framework-runs",
                            createRunDirectoryName());


            // =================================================================
            // Artifact directories
            // =================================================================

            Path reportDirectory =
                    rootDirectory.resolve(
                            "reports");

            Path screenshotDirectory =
                    rootDirectory.resolve(
                            "screenshots");

            Path logDirectory =
                    rootDirectory.resolve(
                            "logs");

            Path videoDirectory =
                    rootDirectory.resolve(
                            "videos");


            // =================================================================
            // Create directories
            // =================================================================

            Files.createDirectories(
                    reportDirectory);

            Files.createDirectories(
                    screenshotDirectory);

            Files.createDirectories(
                    logDirectory);

            Files.createDirectories(
                    videoDirectory);


            // =================================================================
            // Return workspace
            // =================================================================

            return new ExecutionWorkspace(
                    rootDirectory,
                    reportDirectory,
                    screenshotDirectory,
                    logDirectory,
                    videoDirectory);


        } catch (IOException e) {

            throw new FrameworkException(
                    "Failed to create execution workspace.",
                    e);
        }
    }


    private String createRunDirectoryName() {

        String timestamp =
                LocalDateTime.now()
                        .format(RUN_FORMAT);


        String uniqueId =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);


        return timestamp
                + "-"
                + uniqueId;
    }
}