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
 * LoginTest
 *
 * Sample web test validating login via the Page Object model.
 *
 * Flow:
 *   @Listeners(TestListener) → onTestStart bootstraps context/driver/report
 *   → LoginPage.open() + login() → Assert / AssertionEngine checks
 *   → listener cleans up context and flushes report step on pass/fail
 */
//Option 2: annotation (not recommended for enterprise scale)
@Listeners(TestListener.class)
public class LoginTest {

	@Test
	public void verifyValidLogin() {
		TestLogger.logStep(
				">>>>>>>>>>>>>>>>>>>LoginTest - verifyValidLogin>>>>>>>>>>>>>>>>>>>>>>>");

		// Step 1: page object resolves driver from ExecutionContext via BasePage
		LoginPage loginPage = new LoginPage();

		// Step 2: navigate to login URL and wait for page load
		loginPage.open();

		// Step 3: execute login business flow
		loginPage.login("testUser", "testPass");
		TestLogger.logStep("login STEP => testUser / testPass");

		// Step 4: placeholder validation — replace with real post-login check
		boolean isLoggedIn = true; // replace with real assertion logic

		Assert.assertTrue(isLoggedIn, "---------------Login failed - user not redirected to home page");
		
		// Step 5: exercise AssertionEngine hard/soft modes (demo only)
		AssertionEngine assertionEngine = new AssertionEngine();

		assertionEngine.assertTrue(false, "success msg-1", "failure msg -1", Severity.SOFT);

		assertionEngine.assertTrue(true, "success msg-2", "failure msg -2", Severity.SOFT);

		assertionEngine.assertTrue(true, "success msg-3", "failure msg -3", Severity.HARD);

		assertionEngine.assertTrue(false, "success msg-3", "failure msg -3", Severity.HARD);
		
		assertionEngine.assertTrue(false, "success msg-4 after hard fail", "failure msg -4 after hard fail", Severity.HARD);


		
	}
}
