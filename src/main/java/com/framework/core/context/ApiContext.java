package com.framework.core.context;

/**
 * Stores API execution state like:
 * - auth tokens
 * - headers
 * - session info
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
