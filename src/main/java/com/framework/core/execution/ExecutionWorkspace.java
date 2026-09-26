package com.framework.core.execution;

import java.nio.file.Path;

/**
 * ============================================================================
 * Class Name : ExecutionWorkspace
 * ============================================================================
 *
 * Represents the filesystem workspace for ONE framework execution.
 *
 * Example:
 *
 * target/
 *   framework-runs/
 *      20260823-213500-abc123/
 *          reports/
 *          screenshots/
 *          logs/
 *          videos/
 *
 * Responsibilities:
 * - Store root execution directory.
 * - Provide standard artifact directories.
 *
 * This class does NOT create directories.
 * Directory creation is handled by ExecutionDirectoryManager.
 *
 * ============================================================================
 */
public class ExecutionWorkspace {

    private final Path rootDirectory;

    private final Path reportDirectory;

    private final Path screenshotDirectory;

    private final Path logDirectory;

    private final Path videoDirectory;

    public ExecutionWorkspace(
            Path rootDirectory,
            Path reportDirectory,
            Path screenshotDirectory,
            Path logDirectory,
            Path videoDirectory) {

        this.rootDirectory = rootDirectory;
        this.reportDirectory = reportDirectory;
        this.screenshotDirectory = screenshotDirectory;
        this.logDirectory = logDirectory;
        this.videoDirectory = videoDirectory;
    }

    public Path getRootDirectory() {
        return rootDirectory;
    }

    public Path getReportDirectory() {
        return reportDirectory;
    }

    public Path getScreenshotDirectory() {
        return screenshotDirectory;
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Path getVideoDirectory() {
        return videoDirectory;
    }

    @Override
    public String toString() {

        return "ExecutionWorkspace{" +
                "rootDirectory=" + rootDirectory +
                ", reportDirectory=" + reportDirectory +
                ", screenshotDirectory=" + screenshotDirectory +
                ", logDirectory=" + logDirectory +
                ", videoDirectory=" + videoDirectory +
                '}';
    }
}