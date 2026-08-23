package com.framework.core.excepions;

/**
 * FrameworkException
 *
 * Base unchecked exception for non-domain framework failures.
 *
 * Flow:
 *   framework component detects unrecoverable error → throw FrameworkException → TestListener logs failure
 *
 * Domain-specific subclasses (DriverException, ApiException) should be preferred when applicable.
 */
public class FrameworkException extends RuntimeException {
	

	public FrameworkException(String message) {
        super(message);
    }
	
	public FrameworkException(String message, Throwable exception) {
        super(message,exception);
    }
}
