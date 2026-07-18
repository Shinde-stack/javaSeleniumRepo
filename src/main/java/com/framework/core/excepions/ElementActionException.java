package com.framework.core.excepions;

/**
 * ElementActionException
 *
 * Wraps Selenium failures from UI interactions with a readable framework message.
 *
 * Flow:
 *   ElementActions catches Selenium exception → throw ElementActionException → TestListener.onTestFailure
 *
 * Note: a duplicate exists under com.framework.web.exceptions; ElementActions uses the web package copy.
 */
public class ElementActionException extends RuntimeException {

    public ElementActionException(String message) {
        super(message);
    }

    public ElementActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
