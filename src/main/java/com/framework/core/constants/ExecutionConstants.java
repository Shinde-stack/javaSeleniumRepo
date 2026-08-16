package com.framework.core.constants;

/**
 * ============================================================================
 * Class Name : ExecutionConstants
 * ============================================================================
 *
 * Defines the directory names used for one automation execution.
 *
 * Responsibilities
 * ----------------
 * - Execution workspace directory names.
 * - Artifact directory names.
 *
 * This class contains no report specific constants.
 *
 * Example
 * -------
 *
 * target/
 *   automation/
 *      executions/
 *          20260816_194512/
 *              report/
 *              screenshots/
 *              logs/
 *              downloads/
 *              videos/
 *              temp/
 *
 * ============================================================================
 */
public final class ExecutionConstants {

    private ExecutionConstants() {
        throw new UnsupportedOperationException(
                "Utility class should not be instantiated.");
    }

    /**
     * Root build directory.
     */
    public static final String TARGET_DIRECTORY =
            "target";

    /**
     * Framework root directory.
     */
    public static final String AUTOMATION_DIRECTORY =
            "automation";

    /**
     * Parent folder containing all executions.
     */
    public static final String EXECUTIONS_DIRECTORY =
            "executions";

    /**
     * Artifact directories.
     */
    public static final String REPORT_DIRECTORY_NAME =
            "report";

    public static final String SCREENSHOT_DIRECTORY_NAME =
            "screenshots";

    public static final String LOG_DIRECTORY_NAME =
            "logs";

    public static final String DOWNLOAD_DIRECTORY_NAME =
            "downloads";

    public static final String VIDEO_DIRECTORY_NAME =
            "videos";

    public static final String TEMP_DIRECTORY_NAME =
            "temp";
}