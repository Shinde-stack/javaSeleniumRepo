package com.framework.core.constants;

import java.io.File;

/**
 * Reporting constants.
 */
public final class ReportConstants {

    private ReportConstants() {}

    public static final String TARGET_DIR =
            "target";

    public static final String REPORT_DIR =
            TARGET_DIR + File.separator + "reports1"+ File.separator;

    public static final String SCREENSHOT_DIR =
            TARGET_DIR + File.separator + "screenshots1"+ File.separator;

    public static final String LOG_DIR =
            TARGET_DIR + File.separator + "logs1";

    public static final String REPORT_FILE_NAME =
            "ExtentReport.html";

    public static final String SCREENSHOT_EXTENSION =
            ".png";
}