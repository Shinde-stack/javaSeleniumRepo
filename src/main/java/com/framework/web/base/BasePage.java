package com.framework.web.base;

import org.openqa.selenium.WebDriver;

import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.logging.TestLogger;
import com.framework.web.actions.ElementActions;
import com.framework.web.waits.WaitManager;

/**
 * BasePage
 *
 * Parent for all page objects. Resolves driver and action helpers from ExecutionContext.
 *
 * Flow:
 *   TestListener starts lifecycle → page constructor reads ExecutionContextHolder
 *   → builds WaitManager + ElementActions → subclass methods interact via actions/waits
 */
public abstract class BasePage {

    protected final ExecutionContext context;
    protected final WebDriver driver;

    protected final ElementActions actions;
    protected final WaitManager waits;

    protected BasePage() {

        this.context = ExecutionContextHolder.getContext();

        this.driver = context.getDriverContext().getDriver();

        this.waits = new WaitManager(driver);

        this.actions = new ElementActions(driver, waits);
    }

    public void refreshPage() {
        TestLogger.logAction("Refreshing page");
        driver.navigate().refresh();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void goBack() {
        TestLogger.logAction("Navigate Back");
        driver.navigate().back();
    }
}
