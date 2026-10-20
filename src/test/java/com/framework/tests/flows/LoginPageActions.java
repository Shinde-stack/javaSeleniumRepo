package com.framework.tests.flows;

import org.openqa.selenium.WebDriver;

import com.framework.core.assertion.AssertionEngine;
import com.framework.core.assertion.Severity;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.execution.ExecutionWorkspace;
import com.framework.core.logging.TestLogger;
import com.framework.web.pages.room.Room_homePage;
import com.framework.web.pages.room.Room_loginPage;

public class LoginPageActions {

	
	public void login(String loginBtnText, String mobNoToLogin) throws InterruptedException {
		TestLogger.logStep("Login to site");

		// Step 1: page object resolves driver from ExecutionContext via BasePage
		Room_loginPage room_loginPage = new Room_loginPage();
		Room_homePage room_homePage = new Room_homePage();
		AssertionEngine assertionEngine = new AssertionEngine();

//	    ExecutionContext context =
//	            ExecutionContextHolder.get();
//
//	    WebDriver driver =
//	            context.getDriverContext().getDriver();
//
//	    ExecutionWorkspace workspace =
//	            context.getExecutionWorkspace();

	    
		// Step 3: execute login business flow
		boolean isBtnFound = room_loginPage.clickActionOpetion(loginBtnText);
		assertionEngine.assertTrue(isBtnFound, "Login Btn. found", "Login Btn. not found", Severity.HARD);

		TestLogger.logStep("Enter Details at login page");
		room_loginPage.enterMobNo(mobNoToLogin);
		room_loginPage.checkTAndC();
		room_loginPage.clickSendOtpBtn();
		TestLogger.logStep("Clicked OTP Sent");
		
	boolean isNotificationIconDisp =	room_homePage.isNotificationIconDisplayed();		
	assertionEngine.assertTrue(isNotificationIconDisp, "Login Success", "Login failed", Severity.HARD);

	}
	
}
