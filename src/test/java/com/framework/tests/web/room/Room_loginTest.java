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
	public void verifyValidLogin() {
		TestLogger.logStep("Login to site");

		// Step 1: page object resolves driver from ExecutionContext via BasePage
		Room_loginPage room_loginPage = new Room_loginPage();

//			// Step 2: navigate to login URL and wait for page load
		room_loginPage.openBaseUrl();
		
		// Step 3: execute login business flow
		room_loginPage.clickActionOpetion("Login");
		
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	//	room_loginPage.clickLogin();
		
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ScreenshotService.capture("Clicked login");


		// Step 5: exercise AssertionEngine hard/soft modes (demo only)
		AssertionEngine assertionEngine = new AssertionEngine();

		assertionEngine.assertTrue(true, "success msg-1", "failure msg -1", Severity.SOFT);


	}

}
