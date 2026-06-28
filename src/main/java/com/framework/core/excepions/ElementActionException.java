package com.framework.core.excepions;
/**
 * ============================================================================
 * Class Name : ElementActionException
 * ============================================================================
 *
 * ROLE:
 * -----
 * Custom runtime exception for all UI element interaction failures.
 *
 * WHY IT EXISTS:
 * --------------
 * Selenium exceptions are low-level and not readable in reports.
 *
 * This class:
 * - wraps Selenium exceptions
 * - adds framework context
 * - improves debugging clarity
 *
 * EXAMPLE USE CASES:
 * ------------------
 * - Click failure
 * - Element not found
 * - Timeout waiting for element
 * - Stale element reference
 */
public class ElementActionException extends RuntimeException {

    public ElementActionException(String message) {
        super(message);
    }

    public ElementActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
