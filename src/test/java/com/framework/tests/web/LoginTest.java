package com.framework.tests.web;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.framework.core.assertion.AssertionEngine;
import com.framework.core.assertion.Severity;
import com.framework.core.constants.ConfigConstants;
import com.framework.core.lifecycle.ContextLifecycleManager;
import com.framework.core.listeners.TestListener;
import com.framework.core.logging.TestLogger;
import com.framework.web.pages.LoginPage;
import com.framework.web.actions.WebActions;

/**
 * ============================================================================
 * Class Name : LoginTest
 * ============================================================================
 *
 * Purpose: -------- Validates login functionality using Page Object model.
 *
 * Flow: ----- BaseTest ↓ ExecutionContext initialized ↓ WebDriver created ↓
 * LoginPage used for actions
 * ============================================================================
 */

//Option 2: annotation (not recommended for enterprise scale)
@Listeners(TestListener.class)
public class LoginTest {

	@Test
	public void verifyValidLogin() {
		TestLogger.logStep(
				">>>>>>>>>>>>>>>>>>>LoginTest - verifyValidLogin>>>>>>>>>>>>>>>>>>>>>>>");

		// Page initialization (driver comes from ExecutionContext internally)
		LoginPage loginPage = new LoginPage();

//        // Step 1: open application
		loginPage.open();
		// Step 2: perform login action
		loginPage.login("testUser", "testPass");
		TestLogger.logStep("login STEP => testUser / testPass");

		// Step 3: validation (example placeholder)
		boolean isLoggedIn = true; // replace with real assertion logic

		Assert.assertTrue(isLoggedIn, "---------------Login failed - user not redirected to home page");
		
		AssertionEngine assertionEngine = new AssertionEngine();

		assertionEngine.assertTrue(false, "success msg-1", "failure msg -1", Severity.SOFT);

		assertionEngine.assertTrue(true, "success msg-2", "failure msg -2", Severity.SOFT);

		assertionEngine.assertTrue(true, "success msg-3", "failure msg -3", Severity.HARD);

		assertionEngine.assertTrue(false, "success msg-3", "failure msg -3", Severity.HARD);
		
		assertionEngine.assertTrue(false, "success msg-4 after hard fail", "failure msg -4 after hard fail", Severity.HARD);


		
	}
}
