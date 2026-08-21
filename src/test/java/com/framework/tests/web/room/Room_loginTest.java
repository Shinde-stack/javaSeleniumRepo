package com.framework.tests.web.room;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.framework.core.assertion.AssertionEngine;
import com.framework.core.assertion.Severity;
import com.framework.core.listeners.TestListener;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ScreenshotService;
import com.framework.web.pages.LoginPage;
import com.framework.web.pages.room.Room_loginPage;

//Option 2: annotation (not recommended for enterprise scale)
@Listeners(TestListener.class)
public class Room_loginTest {

	@Test
	public void verifyMsg_otpSendToInvalidMobNo() throws InterruptedException {
		TestLogger.logStep("Login to site");

		// Step 1: page object resolves driver from ExecutionContext via BasePage
		Room_loginPage room_loginPage = new Room_loginPage();

//			// Step 2: navigate to login URL and wait for page load
		room_loginPage.openBaseUrl();
		
		// Step 3: execute login business flow
		room_loginPage.clickActionOpetion("Login");
		

		TestLogger.logStep("Enter Details at login page");
		room_loginPage.enterMobNo("1234512345");
		room_loginPage.checkTAndC();
		room_loginPage.clickSendOtpBtn();
		
		TestLogger.logStep("Wait For msg.");
		
		Thread.sleep(5000);
		
		boolean isMsgDisp = room_loginPage.isMsgDisplayed();
		String msgText = room_loginPage.getMsgDisplayed();
		TestLogger.logPass("msgText ->"+msgText);
		TestLogger.logPass("isMsgDisp ->"+isMsgDisp);

		ScreenshotService.capture("Msg. displayed or not after otp send to invalid mob. no.");

		
		// Step 5: exercise AssertionEngine hard/soft modes (demo only)
		AssertionEngine assertionEngine = new AssertionEngine();
		assertionEngine.assertTrue(!isMsgDisp, "Msg. displayed after otp send to invalid mob. no. as expected", "Msg. NOT displayed after otp send to invalid mob. no.", Severity.SOFT);
		

	}

}
