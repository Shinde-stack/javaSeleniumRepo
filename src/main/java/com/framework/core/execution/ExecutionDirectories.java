package com.framework.core.execution;

import java.nio.file.Path;

import com.framework.core.constants.ReportConstants;

/**
 * ============================================================================
 * Class Name : ExecutionDirectories
 * ============================================================================
 *
 * Immutable value object representing the local directory structure for a
 * single automation execution.
 *
 * Responsibilities
 * ----------------
 * - Hold execution artifact directories.
 * - Provide helper methods for commonly used artifact locations.
 *
 * This class contains no business logic and is immutable.
 *
 * One instance exists per ExecutionWorkspace.
 *
 * ============================================================================
 */
public final class ExecutionDirectories {

    private final Path executionDirectory;

    private final Path reportDirectory;

    private final Path screenshotDirectory;

    private final Path logDirectory;

    private final Path downloadDirectory;

    private final Path videoDirectory;

    private final Path tempDirectory;

    public ExecutionDirectories(
            Path executionDirectory,
            Path reportDirectory,
            Path screenshotDirectory,
            Path logDirectory,
            Path downloadDirectory,
            Path videoDirectory,
            Path tempDirectory) {

        this.executionDirectory = executionDirectory;
        this.reportDirectory = reportDirectory;
        this.screenshotDirectory = screenshotDirectory;
        this.logDirectory = logDirectory;
        this.downloadDirectory = downloadDirectory;
        this.videoDirectory = videoDirectory;
        this.tempDirectory = tempDirectory;
    }

    public Path getExecutionDirectory() {
        return executionDirectory;
    }

    public Path getReportDirectory() {
        return reportDirectory;
    }

    /**
     * Returns the full path of the Extent report.
     */
    public Path getReportFile() {

        return reportDirectory.resolve(
                ReportConstants.REPORT_FILE_NAME);
    }

    public Path getScreenshotDirectory() {
        return screenshotDirectory;
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Path getDownloadDirectory() {
        return downloadDirectory;
    }

    public Path getVideoDirectory() {
        return videoDirectory;
    }

    public Path getTempDirectory() {
        return tempDirectory;
    }

    @Override
    public String toString() {

        return "ExecutionDirectories{" +
                "executionDirectory=" + executionDirectory +
                ", reportDirectory=" + reportDirectory +
                ", screenshotDirectory=" + screenshotDirectory +
                ", logDirectory=" + logDirectory +
                ", downloadDirectory=" + downloadDirectory +
                ", videoDirectory=" + videoDirectory +
                ", tempDirectory=" + tempDirectory +
                '}';
    }
}