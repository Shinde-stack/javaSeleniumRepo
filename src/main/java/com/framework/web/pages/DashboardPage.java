package com.framework.web.pages;

import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

	
	
	private By dashboardTitle = By.className("//img[@alt='Cart']");

	    public String getTitle() {
	        return actions.getText(dashboardTitle);
	    }
}
