package com.framework.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.core.driver.WebDriverManager;

public class LoginPage extends BasePage {

    private By username = By.id("user-name");
    private By password = By.id("password");
    private By loginBtn = By.id("login-button");

    public void open(String url) {
   //     actions.type(By.tagName("body"), ""); // force driver usage init (optional)
        com.framework.core.driver.WebDriverManager.getDriver().get(url);
    }

    public void login(String user, String pass) {
        actions.type(username, user);
        actions.type(password, pass);
        actions.click(loginBtn);
    }
    
    
}
