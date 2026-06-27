package com.framework.web.exceptions;
/**
 * ============================================================================
 * Class Name : ElementActionException
 * ============================================================================
 *
 * Purpose:
 * ----------------------------------------------------------------------------
 * Framework-level exception representing failures during UI element operations.
 *
 * This class wraps Selenium exceptions and exposes a cleaner exception to the
 * framework.
 *
 * Instead of leaking Selenium-specific exceptions throughout the framework,
 * ElementActions converts them into this exception.
 *
 * Example:
 *
 * Selenium throws
 * ----------------
 * NoSuchElementException
 *
 * Framework converts
 * ------------------
 * ElementActionException
 *      ↓
 * Root Cause : NoSuchElementException
 *
 * ============================================================================
 *
 * Why not expose Selenium exceptions?
 * ----------------------------------------------------------------------------
 *
 * Selenium exceptions describe HOW the failure occurred.
 *
 * The framework should describe WHAT operation failed.
 *
 * Example:
 *
 * BAD
 * ----
 * ElementClickInterceptedException
 *
 * GOOD
 * ----
 * Failed to click element:
 * Login Button
 *
 * Root Cause:
 * ElementClickInterceptedException
 *
 * ============================================================================
 *
 * Responsibilities
 * ----------------------------------------------------------------------------
 *
 * ✔ Represent UI action failures
 * ✔ Preserve original root cause
 * ✔ Provide meaningful messages
 *
 * Non Responsibilities
 * ----------------------------------------------------------------------------
 *
 * ✘ Logging
 * ✘ Screenshot capture
 * ✘ Retry
 * ✘ Reporting
 * ✘ Waiting
 *
 * Those belong to:
 *
 * ElementActions
 * TestLogger
 * ScreenshotManager
 *
 * ============================================================================
 *
 * Usage Flow
 * ----------------------------------------------------------------------------
 *
 * ElementActions
 *      │
 * Selenium Exception
 *      │
 * catch(Exception)
 *      │
 * throw new ElementActionException(...)
 *      │
 * BaseTest / Listener
 *      │
 * Reporting
 *
 * ============================================================================
 */
public class ElementActionException extends RuntimeException {

    /**
     * Creates framework exception with message only.
     *
     * Use when there is no underlying exception.
     *
     * Example:
     * Invalid locator strategy.
     *
     * @param message Failure description.
     */
    public ElementActionException(String message) {
        super(message);
    }

    /**
     * Creates framework exception preserving original exception.
     *
     * Recommended constructor.
     *
     * Example:
     *
     * Click failed on Login Button
     *
     * Root Cause:
     * ElementClickInterceptedException
     *
     * @param message Failure description
     * @param cause   Original Selenium exception
     */
    public ElementActionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    Future Enhancements (Do NOT implement now)
//
//    Later this class can carry additional debugging information.
//
//    private final By locator;
//
//    private final String elementName;
//
//    private final String pageName;
//
//    private final String screenshotPath;
//
//    private final long timeout;
//
//    private final String browser;
//
//    Then reports become:
//
//    Failed Action
//
//    Page:
//    LoginPage
//
//    Element:
//    Login Button
//
//    Locator:
//    id=loginBtn
//
//    Timeout:
//    20 seconds
//
//    Browser:
//    Chrome
//
//    Screenshot:
//    ...
//
//    Root Cause:
//    TimeoutException
//
//    This is a common enterprise enhancement, but it's unnecessary until the framework is stable.
}
