package com.framework.web.exceptions;

/**
 * ElementActionException
 *
 * Framework-level wrapper for UI element operation failures.
 *
 * Flow:
 *   ElementActions (click/sendKeys/getText) → catch Selenium exception → throw this → listener reports failure
 *
 * Preserves the original Selenium cause for stack trace debugging.
 */
public class ElementActionException extends RuntimeException {

    public ElementActionException(String message) {
        super(message);
    }

    public ElementActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
