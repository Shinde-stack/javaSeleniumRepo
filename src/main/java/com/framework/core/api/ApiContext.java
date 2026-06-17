package com.framework.core.api;

/**
 * Stores API-related execution data.
 *
 * Future examples:
 * - Access token
 * - Refresh token
 * - Base URL
 * - Session identifiers
 *
 * Currently minimal because API framework
 * is not implemented yet.
 */
public class ApiContext {

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
