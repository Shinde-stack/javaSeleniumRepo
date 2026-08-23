package com.framework.core.excepions;

/**
 * ApiException
 *
 * Unchecked exception for API-layer failures (planned).
 *
 * Flow (planned):
 *   REST client call fails → wrap as ApiException → listener/reporting captures failure
 *
 * API automation layer is not implemented yet.
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
