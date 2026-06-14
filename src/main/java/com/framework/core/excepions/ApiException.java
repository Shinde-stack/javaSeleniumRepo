package com.framework.core.excepions;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}