package com.framework.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.core.driver.DriverManager;
import com.framework.core.logging.TestLogger;
import com.framework.web.actions.JsActions;
import com.framework.web.actions.WaitActions;
import com.framework.web.waits.WaitUtils;

public class LoginPage extends BasePage {

    private By username = By.id("//input[@id='username']");
    private By password = By.id("//input[@id='password']");
    private By loginBtn = By.id("//button[@id='submit']");


    public void login(String user, String pass) {
    	
		TestLogger.logStep("LoginPage---login method start");

		WaitActions wa = new WaitActions(driver);
		JsActions ja = new JsActions(driver);
		WaitUtils wu = new WaitUtils(driver);
		wu.waitForPageLoad();
	//	ja.waitForPageLoad(driver, 60);
		ja.scrollTo(loginBtn);
		wa.waitForVisible(loginBtn);	
        actions.type(username, user);
        actions.type(password, pass);
        actions.click(loginBtn);
		TestLogger.logStep("LoginPage---login method end");

    }
    
    
}
