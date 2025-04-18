package com.jostinhv.jakarta.domain.service;

import com.jostinhv.jakarta.domain.exceptions.CredencialesInvalidasException;
import com.jostinhv.jakarta.domain.model.Usuario;

public class UsuarioDomainService {
    public Usuario validarCredenciales(Usuario usuario, String password) {
        if (!usuario.getPassword().equals(password)) {
            throw new CredencialesInvalidasException();
        }
        return usuario;
    }

    public Usuario crearNuevoUsuario(String username, String password) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(encriptarPassword(password));
        return usuario;
    }


    private String encriptarPassword(String password) {
        return password;
    }
}