package com.jostinhv.jakarta.domain.exceptions;

public class UsuarioYaExisteException extends AutenticacionException {

    public UsuarioYaExisteException() {
        super("El usuario ya existe", 409);
    }

    public UsuarioYaExisteException(String username) {
        super("El usuario " + username + " ya existe", 409);
    }
}