package com.framework.core.reporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.framework.core.excepions.FrameworkException;
import com.framework.core.execution.ExecutionWorkspace;

/**
 * ============================================================================
 * Class Name : ScreenshotService
 * ============================================================================
 *
 * Captures screenshots and stores them inside the current execution workspace.
 *
 * Responsibility:
 * - Capture screenshot.
 * - Save screenshot.
 * - Optionally attach it to current report.
 *
 * It does NOT create execution directories.
 *
 * ============================================================================
 */
public class ScreenshotService {

    public Path capture(
            WebDriver driver,
            ExecutionWorkspace workspace,
            String name) {

        if (driver == null) {
            throw new FrameworkException(
                    "Cannot capture screenshot. WebDriver is null.");
        }

        if (workspace == null) {
            throw new FrameworkException(
                    "ExecutionWorkspace cannot be null.");
        }

        try {

            Path destination =
                    workspace
                            .getScreenshotDirectory()
                            .resolve(
                                    sanitize(name)
                                    + ".png");

            Path source =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(
                                    OutputType.FILE)
                            .toPath();

            Files.copy(
                    source,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING);

            return destination;

        } catch (IOException e) {

            throw new FrameworkException(
                    "Failed to save screenshot: " + name,
                    e);
        }
    }

    public void captureAndAttach(
            WebDriver driver,
            ExecutionWorkspace workspace,
            String name) {

        Path screenshot =
                capture(
                        driver,
                        workspace,
                        name);

        ReportManager.getTest()
                .addScreenCaptureFromPath(
                        screenshot.toString());
        
        MediaEntityBuilder.createScreenCaptureFromBase64String( ((TakesScreenshot) driver)
                .getScreenshotAs(
                        OutputType.BASE64)).build();
    }

    private String sanitize(String name) {

        return name
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}