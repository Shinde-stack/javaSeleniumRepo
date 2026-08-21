package com.framework.core.reporting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
 * Responsibilities ---------------- - Capture screenshots from the active
 * WebDriver. - Generate readable screenshot names. - Store screenshots on disk.
 * - Attach screenshots to reports.
 *
 * This class does NOT: - create WebDriver - manage browser lifecycle - depend
 * on TestNG
 *
 * Driver is obtained from the current ExecutionContext.
 *
 * ============================================================================
 *
 * Flow ----
 *
 * Test │ ▼ ScreenshotService.capture("Before Login") │ ▼ ExecutionContextHolder
 * │ ▼ DriverContext │ ▼ TakesScreenshot │ ▼ screenshots/ │ ▼
 * ReportManager.addScreenshot(path)
 *
 * ============================================================================
 *
 * Example -------
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

	private static final String SCREENSHOT_DIRECTORY = "SCREENSHOT_DIRECTORY";

	private ScreenshotService() {
		throw new UnsupportedOperationException("Utility class should not be instantiated.");
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

		ExecutionContext context = ExecutionContextHolder.get();

		WebDriver driver = context.getDriverContext().getDriver();

		if (driver == null) {
			throw new FrameworkException("Cannot capture screenshot. Driver is null.");
		}

		if (!(driver instanceof TakesScreenshot)) {
			throw new FrameworkException("Current driver does not support screenshots.");
		}

		try {

			Path directory = Paths.get(SCREENSHOT_DIRECTORY);

			Files.createDirectories(directory);

			String fileName = buildFileName(title, context.getMetadataContext().getTestName());

			Path destination = directory.resolve(fileName);

			File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			Files.copy(source.toPath(), destination);

//            ReportManager.addScreenshot(
//                    destination.toString());

			return destination.toString();

		} catch (IOException ex) {

			throw new FrameworkException("Unable to capture screenshot. " + ex);
		}
	}

	/**
	 * Creates a readable screenshot filename.
	 */
	private static String buildFileName(String title, String testName) {

		String safeTitle = sanitize(title);

		String safeTestName = sanitize(testName);

		long timestamp = System.currentTimeMillis();

		return safeTestName + "_" + safeTitle + "_" + timestamp + ".png";
	}

	/**
	 * Removes characters that are illegal in filenames.
	 */
	private static String sanitize(String text) {

		if (text == null || text.isBlank()) {
			return "Unknown";
		}

		return text.trim().replaceAll("[^a-zA-Z0-9-_]", "_");
	}
	
	public static String capture1(WebElement element, String elementName) {
		
		ExecutionContext context = ExecutionContextHolder.get();

		WebDriver driver = context.getDriverContext().getDriver();

		if (driver == null) {
			throw new FrameworkException("Cannot capture screenshot. Driver is null.");
		}

		if (!(driver instanceof TakesScreenshot)) {
			throw new FrameworkException("Current driver does not support screenshots.");
		}
	     JavascriptExecutor js = (JavascriptExecutor) driver;

	        // Save original style to revert it later
	        String originalStyle = element.getAttribute("style");

	        // Apply a thick red border background highlight
	        js.executeScript("arguments[0].setAttribute('style', 'border: 3px solid red; background: yellow;');", element);

		return capture(elementName);
	}

}