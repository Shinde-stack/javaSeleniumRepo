package com.framework.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.core.logging.TestLogger;
import com.framework.web.actions.ElementActions;
import com.framework.web.actions.WaitActions;

/**
 * Handles dropdown interaction logic.
 */
public class DropdownComponent {

    private final ElementActions actions;

    public DropdownComponent(WebDriver driver) {
        this.actions = new ElementActions(driver);
    }

    public void select(By dropdown, By option) {
		TestLogger.logStep("DropdownComponent CLASS - select method");
		
		TestLogger.logStep("DropdownComponent - click dropdown");
        actions.click(dropdown);
		TestLogger.logStep("DropdownComponent - click option");
        actions.click(option);
    }
}
