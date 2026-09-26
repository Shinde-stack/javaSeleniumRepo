package com.framework.core.excepions;

public class ContextValidationException extends RuntimeException{
	public ContextValidationException(String message) {
        super(message);
    }
	public ContextValidationException(String message,Throwable cause) {
        super(message,cause);
    }
}

