package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.framework.web.actions.LoginActions;
import com.framework.core.driver.WebDriverManager;

public class LoginTest {

	
	
	
	  private LoginActions actions;

	  @BeforeMethod
	  @Parameters("browser")
	  public void setup(@Optional("chrome") String browser) {
	      WebDriverManager.initDriver(browser);
	      actions = new LoginActions();
	  }

	    @Test
	    public void testLogin() {
	        String title = actions.loginAndGetTitle(
	                "https://www.flipkart.com/",
	                "standard_user",
	                "secret_sauce"
	        );

	        System.out.println(title);

	        Assert.assertEquals(title, "Appliances");
	        
	    }

	    @AfterMethod
	    public void tearDown() {
	        WebDriverManager.quitDriver();
	    }
	    
	    
	    
}
