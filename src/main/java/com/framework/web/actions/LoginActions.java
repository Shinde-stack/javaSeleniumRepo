package com.framework.web.actions;

import com.framework.web.pages.DashboardPage;
import com.framework.web.pages.LoginPage;

public class LoginActions {

	

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public LoginActions() {
        this.loginPage = new LoginPage();
        this.dashboardPage = new DashboardPage();
    }

    public String loginAndGetTitle(String url, String user, String pass) {
        loginPage.open(url);
    //    loginPage.login(user, pass);
        return dashboardPage.getTitle();
    }
	    
	    
}
