package com.framework.tests.web.room;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.framework.core.assertion.AssertionEngine;
import com.framework.core.assertion.Severity;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.execution.ExecutionWorkspace;
import com.framework.core.listeners.TestListener;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ScreenshotService;
import com.framework.tests.flows.LoginPageActions;
import com.framework.web.pages.LoginPage;
import com.framework.web.pages.room.Room_homePage;
import com.framework.web.pages.room.Room_loginPage;

//Option 2: annotation (not recommended for enterprise scale)
@Listeners(TestListener.class)
public class Room_loginTest {

	
	String loginBtnText = "Login";
	String loginNumber = "1234512345";
	
	
	
	
	
	
	//temporary
    private final ScreenshotService screenshotService =
            new ScreenshotService();
    
	@Test
	public void verifyMsg_otpSendToInvalidMobNo() throws InterruptedException {
		TestLogger.logStep("Login to site");

		// Step 1: page object resolves driver from ExecutionContext via BasePage
		Room_loginPage room_loginPage = new Room_loginPage();
		AssertionEngine assertionEngine = new AssertionEngine();

	    ExecutionContext context =
	            ExecutionContextHolder.get();

	    WebDriver driver =
	            context.getDriverContext().getDriver();

	    ExecutionWorkspace workspace =
	            context.getExecutionWorkspace();

	//    driver.get("https://example.com");

	 

//			// Step 2: navigate to login URL and wait for page load
		room_loginPage.openBaseUrl();
		
		// Step 3: execute login business flow
		boolean isBtnFound = room_loginPage.clickActionOpetion("Login");
		assertionEngine.assertTrue(isBtnFound, "Login Btn. found", "Login Btn. not found", Severity.HARD);

		TestLogger.logStep("Enter Details at login page");
		room_loginPage.enterMobNo("1234512345");
		room_loginPage.checkTAndC();
		room_loginPage.clickSendOtpBtn();
		
		TestLogger.logStep("Wait For msg.");
				
		   screenshotService.captureAndAttach(
		            driver,
		            workspace,
		            "sc page");
		
		boolean isMsgDisp = room_loginPage.isMsgDisplayed();
		String msgText = room_loginPage.getMsgDisplayed();
		TestLogger.logPass("msgText ->"+msgText);
		TestLogger.logPass("isMsgDisp ->"+isMsgDisp);

	//	ScreenshotService.capture("Msg. displayed or not after otp send to invalid mob. no.");

		
		// Step 5: exercise AssertionEngine hard/soft modes (demo only)

		assertionEngine.assertTrue(!isMsgDisp, "Msg. displayed after otp send to invalid mob. no. as expected", "Msg. NOT displayed after otp send to invalid mob. no.", Severity.SOFT);
		
		assertionEngine.assertAll();
	}
	
	
	@Test
	public void verifyMsg_otpSendToInvalidMobNo1() throws InterruptedException {
		TestLogger.logStep("Login to site");

		// Step 1: page object resolves driver from ExecutionContext via BasePage
		Room_loginPage room_loginPage = new Room_loginPage();
		AssertionEngine assertionEngine = new AssertionEngine();
		LoginPageActions loginPageActions = new LoginPageActions();
		Room_homePage room_homePage = new Room_homePage();
		
	    ExecutionContext context =
	            ExecutionContextHolder.get();

	    WebDriver driver =
	            context.getDriverContext().getDriver();

	    ExecutionWorkspace workspace =
	            context.getExecutionWorkspace();


//			// Step 2: navigate to login URL and wait for page load
		room_loginPage.openBaseUrl();
		loginPageActions.login(loginBtnText, loginNumber);
		
		boolean isIcoDisplayed = room_homePage.isNotificationIconDisplayed();

		   screenshotService.captureAndAttach(
		            driver,
		            workspace,
		            "Expected to navigate to HOME page after login");
		assertionEngine.assertTrue(isIcoDisplayed, "Login Success", "Login Failed", Severity.HARD);

	
	}

}
