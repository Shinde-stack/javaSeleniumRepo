package com.framework.core.reporting;

/**
 * ============================================================================
 * Class Name : ReportConstants
 * ============================================================================
 *
 * Purpose:
 * --------
 * Centralized constants for reporting subsystem.
 *
 * Responsibilities:
 * -----------------
 * 1. Define report output paths
 * 2. Define screenshot storage paths
 * 3. Define file naming conventions
 * 4. Define report configuration keys
 *
 * Why separate:
 * -------------
 * Prevents hardcoding paths across:
 * - ReportManager
 * - ScreenshotService
 * - Listener
 *
 * ============================================================================
 */
public final class ReportConstants {

    private ReportConstants() {}

    public static final String REPORT_DIR = //"target/reports/"
    		System.getProperty("user.dir")
    		;
    public static final String SCREENSHOT_DIR = //"target/reports/screenshots/"
    		System.getProperty("user.dir")

    		;

    public static final String REPORT_FILE_NAME = "execution-report.html";

    public static final String SCREENSHOT_EXTENSION = ".png";
}