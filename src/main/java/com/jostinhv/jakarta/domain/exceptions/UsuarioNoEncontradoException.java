package com.jostinhv.jakarta.domain.exceptions;


public class UsuarioNoEncontradoException extends AutenticacionException {

    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado", 404);
    }

    public UsuarioNoEncontradoException(String username) {
        super("Usuario no encontrado: " + username, 404);
    }
}