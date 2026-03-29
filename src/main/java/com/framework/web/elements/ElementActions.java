package com.framework.web.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.core.driver.WebDriverManager;
import com.framework.core.utils.WaitUtils;

public class ElementActions {

	
	
	   private WebDriver driver;

	    public ElementActions() {
	        this.driver = WebDriverManager.getDriver();
	    }

	    public void click(By locator) {
	        WaitUtils.waitForClick(locator);
	        driver.findElement(locator).click();
	    }

	    public void type(By locator, String text) {
	        WebElement element = WaitUtils.waitForElement(locator);
	        element.clear();
	        element.sendKeys(text);
	    }

	    public String getText(By locator) {
	        return WaitUtils.waitForElement(locator).getText();
	    }
	
	
	
	
}
