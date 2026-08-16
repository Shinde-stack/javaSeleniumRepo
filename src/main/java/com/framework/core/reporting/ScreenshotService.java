package com.framework.core.reporting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : ScreenshotService
 * ============================================================================
 *
 * Central service responsible for capturing browser screenshots.
 *
 * Responsibilities
 * ----------------
 * - Capture screenshots from the active WebDriver.
 * - Generate readable screenshot names.
 * - Store screenshots on disk.
 * - Attach screenshots to reports.
 *
 * This class does NOT:
 * - create WebDriver
 * - manage browser lifecycle
 * - depend on TestNG
 *
 * Driver is obtained from the current ExecutionContext.
 *
 * ============================================================================
 *
 * Flow
 * ----
 *
 * Test
 *   │
 *   ▼
 * ScreenshotService.capture("Before Login")
 *   │
 *   ▼
 * ExecutionContextHolder
 *   │
 *   ▼
 * DriverContext
 *   │
 *   ▼
 * TakesScreenshot
 *   │
 *   ▼
 * screenshots/
 *   │
 *   ▼
 * ReportManager.addScreenshot(path)
 *
 * ============================================================================
 *
 * Example
 * -------
 *
 * capture();
 *
 * capture("Login Page");
 *
 * capture("Before Submit");
 *
 * ============================================================================
 */
public final class ScreenshotService {

    private static final String SCREENSHOT_DIRECTORY = "screenshots";

    private ScreenshotService() {
        throw new UnsupportedOperationException(
                "Utility class should not be instantiated.");
    }

    /**
     * Captures a screenshot using a default title.
     *
     * @return absolute screenshot path
     */
    public static String capture() {

        return capture("Screenshot");
    }

    /**
     * Captures a screenshot using the supplied title.
     *
     * The screenshot is automatically attached to the report.
     *
     * @param title business friendly screenshot title
     *
     * @return absolute screenshot path
     */
    public static String capture(String title) {

        ExecutionContext context =
                ExecutionContextHolder.get();

        WebDriver driver =
                context.getDriverContext().getDriver();

        if (driver == null) {
            throw new FrameworkException(
                    "Cannot capture screenshot. Driver is null.");
        }

        if (!(driver instanceof TakesScreenshot)) {
            throw new FrameworkException(
                    "Current driver does not support screenshots.");
        }

        try {

            Path directory =
                    Paths.get(SCREENSHOT_DIRECTORY);

            Files.createDirectories(directory);

            String fileName =
                    buildFileName(
                            title,
                            context.getMetadataContext().getTestName());

            Path destination =
                    directory.resolve(fileName);

            File source =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            Files.copy(
                    source.toPath(),
                    destination);

            ReportManager.addScreenshot(
                    destination.toString());

            return destination.toString();

        } catch (IOException ex) {

            throw new FrameworkException(
                    "Unable to capture screenshot. "+ex);
        }
    }

    /**
     * Creates a readable screenshot filename.
     */
    private static String buildFileName(
            String title,
            String testName) {

        String safeTitle =
                sanitize(title);

        String safeTestName =
                sanitize(testName);

        long timestamp =
                System.currentTimeMillis();

        return safeTestName
                + "_"
                + safeTitle
                + "_"
                + timestamp
                + ".png";
    }

    /**
     * Removes characters that are illegal in filenames.
     */
    private static String sanitize(String text) {

        if (text == null || text.isBlank()) {
            return "Unknown";
        }

        return text
                .trim()
                .replaceAll("[^a-zA-Z0-9-_]", "_");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

/**
 * Captures a screenshot and saves it to the target directory for reporting.
 *
 * @param driver       The active WebDriver instance.
 * @param testName     The name of the current test case (used for naming the file).
 * @param screenshotDir The target directory path where screenshots should be stored.
 * @return String      The absolute path of the saved screenshot for report linking.
 * @throws FrameworkException If driver is null, or if a physical I/O failure occurs.
 */
public static String attachScreenshotToReport(String testName, String screenshotDir) {
   
	  ExecutionContext context =
              ExecutionContextHolder.get();

    WebDriver driver =
            context.getDriverContext().getDriver();

	if (driver == null) {
        throw new FrameworkException("Failed to capture screenshot: WebDriver instance is null.");
    }

    try {
        // Generate a unique file name using a timestamp to prevent overwriting
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
        String fileName = String.format("%s_%s.png", testName, timestamp);
        String targetPath = Paths.get(screenshotDir, fileName).toString();

        // Ensure directories exist
        Files.createDirectories(Paths.get(screenshotDir));

        // Capture screenshot using Selenium's TakesScreenshot interface
        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.move(sourceFile.toPath(), Paths.get(targetPath));

        return targetPath; // Return path to embed directly into ExtentReports/Allure

    } catch (WebDriverException e) {
        // Catch driver-specific issues (e.g., browser crashed, session closed)
        throw new FrameworkException("WebDriver failed to capture screenshot for test: " + testName, e);
    } catch (IOException e) {
        // Catch file system infrastructure issues
        throw new FrameworkException("File system failure while saving screenshot for test: " + testName, e);
    }
}
}