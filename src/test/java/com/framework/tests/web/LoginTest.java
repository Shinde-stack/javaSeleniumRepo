package com.framework.tests.web;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.core.logging.TestLogger;
import com.framework.core.validation.AssertionEngine;
import com.framework.core.validation.Severity;
import com.framework.orchestrator.base.BaseTest;
import com.framework.web.pages.LoginPage;
import com.framework.web.actions.WebActions;

/**
 * ============================================================================
 * Class Name : LoginTest
 * ============================================================================
 *
 * Purpose:
 * --------
 * Validates login functionality using Page Object model.
 *
 * Flow:
 * -----
 * BaseTest
 *   ↓
 * ExecutionContext initialized
 *   ↓
 * WebDriver created
 *   ↓
 * LoginPage used for actions
 * ============================================================================
 */
public class LoginTest extends BaseTest {

    @Test
    public void verifyValidLogin() {
 		TestLogger.logStep("=====================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>LoginTest - verifyValidLogin === start");

        // Page initialization (driver comes from ExecutionContext internally)
        LoginPage loginPage = new LoginPage();

        // Optional: navigation layer (recommended separation)
        WebActions webActions = new WebActions(
//                com.framework.core.context.ExecutionContextHolder
//                        .getContext()
//                        .driver()
//                        .getDriver()
        		
        		driver
        );

        // Step 1: open application
        webActions.openUrl("https://practicetestautomation.com/practice-test-login/");
		
        TestLogger.logStep(
		        "TestLogger in TEST -checking ---- google ");
		
        // Step 2: perform login action
        loginPage.login("testUser", "testPass");
 		TestLogger.logStep("login => testUser / testPass");

        // Step 3: validation (example placeholder)
        boolean isLoggedIn = true; // replace with real assertion logic

        Assert.assertTrue(isLoggedIn, "Login failed - user not redirected to home page");
        
        
        AssertionEngine assertionEngine = new AssertionEngine ();
        
        assertionEngine.assertTrue(
                false,
                "success msg-1",
                "failure msg -1",
                Severity.SOFT);
        
        assertionEngine.assertTrue(
                false,
                "success msg-2",
                "failure msg -2",
                Severity.SOFT);
        
        assertionEngine.assertTrue(
                false,
                "success msg-3",
                "failure msg -3",
                Severity.HARD);
        
        
    }
}
