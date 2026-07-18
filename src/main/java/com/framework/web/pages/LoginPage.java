package com.framework.web.pages;

import org.openqa.selenium.By;

import com.framework.core.logging.TestLogger;
import com.framework.web.base.BasePage;
import com.framework.web.waits.WaitManager;
import com.framework.web.waits.WaitManager;

/**
 * LoginPage
 *
 * Page object for the login screen at the configured base URL.
 *
 * Flow:
 *   open() navigates and waits for page load → enterUsername/enterPassword/clickLogin via ElementActions
 *   → login() composes the full happy-path → getErrorMessage/isErrorDisplayed for negative checks
 */
public class LoginPage extends BasePage {

    private By usernameInput = By.xpath("//input[@id='userName']");
    private By passwordInput = By.id("password");
    private By loginButton = By.xpath("//button[contains(text(),'Login')]");

    private final By errorMessage   = By.id("errorMsg");
    
    /**
     * Navigates to base URL and waits for document ready state.
     */
    public LoginPage open() {
        driver.get(context.getConfig().getBaseUrl());
        waits.waitForPageLoad();
        return this;
    }

    public void enterUsername(String username) {

        TestLogger.logStep("Entering username");

        actions.sendKeys(usernameInput, username, "Username field");
    }

    public void enterPassword(String password) {

        TestLogger.logStep("Entering password");

        actions.sendKeys(passwordInput, password, "Password field");
    }

    public void clickLogin() {

        TestLogger.logStep("Clicking login button");

        actions.click(loginButton, "Login button");
    }

    /**
     * High-level login flow intended for test use.
     */
    public void login(String username, String password) {

        TestLogger.logStep("Login flow started");
        
        enterUsername(username);
        enterPassword(password);
        clickLogin();

        TestLogger.logStep("Login flow completed");
    }

    public String getErrorMessage() {

        TestLogger.logStep("Fetching login error message");

        return actions.getText(errorMessage, "Login error message");
    }

    public boolean isErrorDisplayed() {

        TestLogger.logStep("Checking if error is displayed");

        return actions.isDisplayed(errorMessage, "Error message");
    }
}
