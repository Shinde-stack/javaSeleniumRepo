package com.framework.web.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.logging.TestLogger;

/**
 * ============================================================================
 * ElementActions (FINAL ORCHESTRATION LAYER)
 * ============================================================================
 *
 * Responsibility:
 * - Coordinates waits + actions + fallback
 * - Handles stale element recovery centrally
 * - Provides stable API to Page Objects
 *
 * IMPORTANT RULES:
 * - Pages must ONLY use this class
 * - Pages must NEVER use WebActions/JsActions directly
 * ============================================================================
 */
public class ElementActions {

    private final WebActions webActions;
    private final JsActions jsActions;
    private final WaitActions waitActions;

    public ElementActions(WebDriver driver) {
        this.webActions = new WebActions(driver);
        this.jsActions = new JsActions(driver);
        this.waitActions = new WaitActions(driver);
    }

//    // ---------------------------------------------------------------------
//    // PRIMARY ACTIONS (stale-safe)
//    // ---------------------------------------------------------------------
//
//    /**
//     * Click with automatic stale protection.
//     */
//    public void click(By by) {
//        waitActions.waitForRefreshedClickable(by);
//        webActions.click(by);
//    }
//
//    /**
//     * Type with visibility sync.
//     */
//    public void type(By by, String text) {
//        waitActions.waitForVisible(by);
//        webActions.clear(by);
//        webActions.type(by, text);
//    }
//
//    /**
//     * Safe text extraction.
//     */
//    public String getText(By by) {
//        waitActions.waitForVisible(by);
//        return webActions.getText(by);
//    }
//
//    // ---------------------------------------------------------------------
//    // FALLBACK ACTIONS
//    // ---------------------------------------------------------------------
//
//    /**
//     * JS click used only when normal click fails.
//     */
//    public void jsClick(By by) {
//        waitActions.waitForRefreshedClickable(by);
//        jsActions.click(by);
//    }
//
//    public void scrollTo(By by) {
//        jsActions.scrollTo(by);
//    }
    
    
//    //2------------
//    
//    // ---------------------------------------------------------------------
//    // CLICK (stale-safe)
//    // ---------------------------------------------------------------------
//
//    public void click(By by) {
//
//        RetryExecutor.execute(() -> {
//
//            waitActions.waitForRefreshedClickable(by);
//
//            WebElement element = driver.findElement(by);
//
//            element.click();
//
//            return null;
//        });
//    }
//
//    // ---------------------------------------------------------------------
//    // TYPE (stale-safe)
//    // ---------------------------------------------------------------------
//
//    public void type(By by, String text) {
//
//        RetryExecutor.execute(() -> {
//
//            waitActions.waitForVisible(by);
//
//            WebElement element = driver.findElement(by);
//
//            element.clear();
//            element.sendKeys(text);
//
//            return null;
//        });
//    }
//
//    // ---------------------------------------------------------------------
//    // TEXT
//    // ---------------------------------------------------------------------
//
//    public String getText(By by) {
//
//        final String[] result = new String[1];
//
//        RetryExecutor.execute(() -> {
//
//            waitActions.waitForVisible(by);
//
//            WebElement element = driver.findElement(by);
//
//            result[0] = element.getText();
//
//            return null;
//        });
//
//        return result[0];
//    }
//
//    // ---------------------------------------------------------------------
//    // JS CLICK (fallback path)
//    // ---------------------------------------------------------------------
//
//    public void jsClick(By by) {
//
//        RetryExecutor.execute(() -> {
//
//            WebElement element = driver.findElement(by);
//
//            jsActions.click(by);
//
//            return null;
//        });
//    }
    
    
    
    
    public void click(By by) {

        RetryExecutor.execute(() -> {

            waitActions.waitForRefreshedClickable(by);

            WebDriver driver = ExecutionContextHolder
                    .getContext()
                    .driver()
                    .getDriver();

            WebElement element = driver.findElement(by);

            TestLogger.logAction( "element actions - Click element: "+element.getText());

            element.click();

            return null;
        });
    }

    public void type(By by, String text) {

        RetryExecutor.execute(() -> {

            waitActions.waitForVisible(by);

            WebDriver driver = ExecutionContextHolder
                    .getContext()
                    .driver()
                    .getDriver();

            WebElement element = driver.findElement(by);

            element.clear();
            
            TestLogger.logAction( "element actions - Send keys: "+text);

            element.sendKeys(text);

            return null;
        });
    }
    
    
    
    
    
}






