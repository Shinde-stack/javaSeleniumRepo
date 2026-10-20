package com.framework.web.pages.room;

import org.openqa.selenium.By;

import com.framework.core.logging.TestLogger;
import com.framework.web.base.BasePage;
import com.framework.web.waits.WaitManager;

public class Room_homePage extends BasePage {

	private By notificationIcon = By.xpath("//a[contains(@class,'otification')]");

	public boolean isNotificationIconDisplayed() {
		TestLogger.logStep("Chicking  NOTIFICATION icon");
		return actions.isDisplayed(notificationIcon, "Notification icon at HOME page");
	}

}
