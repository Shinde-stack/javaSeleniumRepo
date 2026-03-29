package com.framework.core.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.core.driver.WebDriverManager;

public class WaitUtils {

	
	   private static final int TIMEOUT = 10;

	    public static WebElement waitForElement(By locator) {
	        WebDriverWait wait = new WebDriverWait(
	                WebDriverManager.getDriver(),
	                Duration.ofSeconds(TIMEOUT)
	        );
	        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	    }

	    public static void waitForClick(By locator) {
	        WebDriverWait wait = new WebDriverWait(
	                WebDriverManager.getDriver(),
	                Duration.ofSeconds(TIMEOUT)
	        );
	        wait.until(ExpectedConditions.elementToBeClickable(locator));
	    }
	    
}
