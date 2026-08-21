package com.framework.web.pages.room;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ScreenshotService;
import com.framework.web.base.BasePage;
import com.framework.web.pages.LoginPage;

public class Room_loginPage extends BasePage {

	private By actionsWeList_by = By.xpath("//nav[contains(@class,'landing')]//a");
	private By login_by = By.xpath("//a[contains(text(),'Log in')]");

	private By xBtn = By.xpath("//section//button[contains(@class,'close')]");
	private By mobileInput = By.xpath("//form//input[@id='phone']");
	private By tAncC_checkbox = By.xpath("//form//input[@type='checkbox']");
	private By otpBtn = By.xpath("//form//button[@type='submit']");
	private By errorMsg = By.xpath("//form//p");

	/**
	 * Navigates to base URL and waits for document ready state.
	 */
	public Room_loginPage openBaseUrl() {
		TestLogger.logStep("Opening Base Url -" + context.getConfig().getBaseUrl());
		navigateTo(context.getConfig().getBaseUrl());
		waits.waitForPageLoad();
		return this;
	}

	public void clickLogin() {
		TestLogger.logStep("Click Login btn");
		waits.waitForPageLoad();
		actions.click(login_by, "Login Tab");
	}

	public void clickActionOpetion(String text) {
		TestLogger.logStep("Start to click -" + text);
		waits.waitForPageLoad();

		List<WebElement> actionsWeList = driver.findElements(actionsWeList_by);

		boolean isFound = false;
		List foundTexts = new ArrayList();

		for (int a = 0; a < actionsWeList.size(); a++) {

			String weText = actionsWeList.get(a).getText();

			if (!weText.isBlank()) {

				if (weText.trim().toLowerCase().contains(text.trim().toLowerCase())) {

					isFound = true;

					actions.click(actionsWeList.get(a), text);

					TestLogger.logStep("Clicked -" + text);
					return;
				} else {
					foundTexts.add(weText);
					System.out.println(weText);
				}

			}

		}

		if (!isFound) {
			TestLogger.logFailure("Not found -" + text + " But found texts are ->" + foundTexts, null);

		}

	}

	public void enterMobNo(String mobNo) {
		TestLogger.logStep("Enter Mob. No.");
		waits.waitForVisible(mobileInput, "Mob. No.");
		actions.sendKeys(mobileInput, mobNo, "Mob. No.");
	}

	public void checkTAndC() {
		TestLogger.logStep("Check t and c");
		actions.click(tAncC_checkbox, "tAndC chackbox");
	}

	public void clickSendOtpBtn() {
		TestLogger.logStep("Click Send OTP btn");
		actions.click(otpBtn, "Send OTP Btn");
	}

	public boolean isMsgDisplayed() {
		TestLogger.logStep("Check msg.");

		boolean isDisp = false;

		try {
			waits.waitForVisible(errorMsg, "Msg..");
			WebElement msg = driver.findElement(errorMsg);

			isDisp = msg.isDisplayed();

		} catch (Exception e) {

		}
		return isDisp;

	}

	public String getMsgDisplayed() {
		TestLogger.logStep("Get msg.");
		
		ScreenshotService.capture1(driver.findElement(errorMsg), "Msg. in box");

		return actions.getText(errorMsg, "get msg displayed");

	}

}
