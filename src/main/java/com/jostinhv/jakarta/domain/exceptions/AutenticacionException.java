package com.jostinhv.jakarta.domain.exceptions;

import lombok.Getter;

@Getter
public class AutenticacionException extends RuntimeException {

    private final int statusCode;

    public AutenticacionException(String message) {
        this(message, 401);
    }

    public AutenticacionException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AutenticacionException(String message, Throwable cause) {
        this(message, cause, 401);
    }

    public AutenticacionException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}