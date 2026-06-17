package com.framework.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.framework.web.actions.ElementActions;

/**
 * Reusable UI fragment across pages.
 */
public class HeaderComponent {

    private final ElementActions actions;
    private final By logoutBtn = By.id("logout");

    public HeaderComponent(WebDriver driver) {
        this.actions = new ElementActions(driver);
    }

    public void logout() {
        actions.click(logoutBtn);
    }
}
