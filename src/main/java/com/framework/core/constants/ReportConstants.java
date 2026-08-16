package com.framework.core.constants;

/**
 * ============================================================================
 * Class Name : ReportConstants
 * ============================================================================
 *
 * Defines report specific constants.
 *
 * Responsibilities
 * ----------------
 * - Report file names.
 * - Report titles.
 * - Report metadata.
 *
 * Directory locations belong to ExecutionConstants.
 *
 * ============================================================================
 */
public final class ReportConstants {

    private ReportConstants() {
        throw new UnsupportedOperationException(
                "Utility class should not be instantiated.");
    }

    /**
     * Default Extent report filename.
     */
    public static final String REPORT_FILE_NAME =
            "AutomationExecutionReport.html";

    /**
     * Report display name.
     */
    public static final String REPORT_NAME =
            "Automation Execution Report";

    /**
     * Browser title.
     */
    public static final String REPORT_TITLE =
            "Automation Results";
}