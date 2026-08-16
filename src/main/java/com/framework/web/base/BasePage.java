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

        this.context = ExecutionContextHolder.get();

        this.driver = context.getDriverContext().getDriver();

        this.waits = new WaitManager(driver);

        this.actions = new ElementActions(driver, waits);
    }

    public void navigateTo(String urlToOpen) {
        TestLogger.logAction("Navigate To :"+urlToOpen);
        driver.get(urlToOpen);
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

    public void navigateBack() {
        TestLogger.logAction("Navigate Back");
        driver.navigate().back();
    }
    
    
//    
//    1. Browser Navigation ✅
//    refreshPage()
//
//    goBack()
//
//    goForward()
//
//    navigateTo(String url)
//
//    navigateToRelative(String path)
//
//    Example
//
//    public void refreshPage() {
//        driver.navigate().refresh();
//    }
//    2. Browser Information ✅
//    getTitle()
//
//    getCurrentUrl()
//
//    getPageSource()
//
//    Example
//
//    public String getTitle() {
//        return driver.getTitle();
//    }
//    3. Window Operations ✅
//    switchToWindow(String title)
//
//    switchToLatestWindow()
//
//    closeCurrentWindow()
//
//    getWindowHandles()
//
//    switchToParentWindow()
//
//    These are browser operations, not page-specific logic.
//
//    4. Frame Operations ✅
//    switchToFrame(By locator)
//
//    switchToFrame(int index)
//
//    switchToFrame(String name)
//
//    switchToDefaultContent()
//
//    switchToParentFrame()
//    5. Alert Operations ✅
//    acceptAlert()
//
//    dismissAlert()
//
//    getAlertText()
//
//    sendAlertText()
//    6. Common Wait Helpers (Optional) ✅
//
//    Normally these belong in WaitManager.
//
//    But convenience wrappers are acceptable.
//
//    waitUntilPageLoaded()
//
//    waitUntilAjaxComplete()
//
//    Internally they call WaitManager.
//
//    7. Screenshot Helpers (Optional)
//
//    If every page occasionally needs them.
//
//    captureScreenshot(String name)
//
//    Internally calls
//
//    ScreenshotService.capture(...)
//    8. JavaScript Page Helpers
//
//    Not element-specific.
//
//    scrollToTop()
//
//    scrollToBottom()
//
//    zoom()
//
//    executeScript()
//    9. Logging Helpers (Optional)
//    logStep()
//
//    logInfo()
//
//    Personally I'd keep logging in TestLogger, not BasePage.
}
