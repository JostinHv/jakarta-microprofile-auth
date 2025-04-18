package com.jostinhv.jakarta.domain.exceptions;

public class CredencialesInvalidasException extends AutenticacionException {

    public CredencialesInvalidasException() {
        super("Credenciales inválidas");
    }

    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
