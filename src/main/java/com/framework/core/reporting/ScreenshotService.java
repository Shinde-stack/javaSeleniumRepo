package com.framework.core.reporting;

import com.framework.core.constants.ReportConstants;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * Class Name : ScreenshotService
 * ============================================================================
 *
 * Purpose:
 * --------
 * Captures browser screenshots in a thread-safe manner
 * for failure analysis and reporting.
 *
 * Responsibilities:
 * -----------------
 * 1. Capture screenshot from WebDriver
 * 2. Store file using execution metadata
 * 3. Return file path for reporting attachment
 *
 * Design:
 * -------
 * - Uses ExecutionContext (thread-safe source of truth)
 * - Avoids static WebDriver references
 * - Generates unique file names per thread
 *
 * ============================================================================
 */
public final class ScreenshotService {

    private ScreenshotService() {}

    public static String capture(String testName) {

        try {

            ExecutionContext context =
                    ExecutionContextHolder.getContext();

            WebDriver driver =
                    context.getDriverContext().getDriver();

            if (driver == null) {
                return null;
            }

            File src =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            String timestamp =
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            long threadId =
                    Thread.currentThread().getId();

            String fileName =
                    testName + "_T" + threadId + "_" + timestamp + ".png";

            Path destPath =
                    Paths.get(
                            ReportConstants.SCREENSHOT_DIR + fileName);

            Files.createDirectories(destPath.getParent());

            Files.copy(src.toPath(), destPath);

            return destPath.toString();

        } catch (Exception e) {
            return null;
        }
    }
}