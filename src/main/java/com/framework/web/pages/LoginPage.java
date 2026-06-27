package com.framework.web.pages;

import org.openqa.selenium.By;

import com.framework.core.logging.TestLogger;
import com.framework.web.base.BasePage;

public class LoginPage extends BasePage {

    // =========================================================================
    // LOCATORS
    // =========================================================================

    private By usernameInput = By.id("//input[@id='username']");
    private By passwordInput = By.id("//input[@id='password']");
    private By loginButton = By.id("//button[@id='submit']");

    private final By errorMessage   = By.id("errorMsg");
    
 // =========================================================================
    // PAGE ACTIONS (LOW LEVEL)
    // =========================================================================

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

    // =========================================================================
    // BUSINESS FLOW (HIGH LEVEL METHOD)
    // =========================================================================

    /**
     * COMPLETE LOGIN FLOW
     *
     * This is what tests SHOULD call.
     */
    public void login(String username, String password) {

        TestLogger.logStep("Login flow started");

        enterUsername(username);
        enterPassword(password);
        clickLogin();

        TestLogger.logStep("Login flow completed");
    }

    // =========================================================================
    // VALIDATION METHODS
    // =========================================================================

    public String getErrorMessage() {

        TestLogger.logStep("Fetching login error message");

        return actions.getText(errorMessage, "Login error message");
    }

    public boolean isErrorDisplayed() {

        TestLogger.logStep("Checking if error is displayed");

        return actions.isDisplayed(errorMessage, "Error message");
    }

    // =========================================================================
    // FUTURE IMPROVEMENTS
    // =========================================================================
    /*
     * Planned upgrades:
     * -----------------
     * 1. Return type chaining:
     *      login() → DashboardPage
     *
     * 2. Optional builder-style login:
     *      new LoginPage().withUser().withPass().submit()
     *
     * 3. Component extraction:
     *      LoginFormComponent (if reused across apps)
     *
     * 4. Negative test helpers:
     *      loginExpectFailure()
     */
    
    
}
