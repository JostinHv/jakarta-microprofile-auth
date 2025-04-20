package com.jostinhv.jakarta.application.usecases;
import com.jostinhv.jakarta.application.ports.input.AutenticarUseCase;
import com.jostinhv.jakarta.application.ports.input.RegistrarUseCase;
import com.jostinhv.jakarta.application.ports.output.UsuarioPersistenciaPort;
import com.jostinhv.jakarta.domain.service.UsuarioDomainService;
import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.domain.exceptions.*;
import com.jostinhv.jakarta.domain.annotations.UseCase;

import java.util.Optional;

@UseCase("Usuario")
public class UsuarioUseCaseImpl implements RegistrarUseCase, AutenticarUseCase {

    private final UsuarioPersistenciaPort persistenciaPort;
    private final UsuarioDomainService domainService;

    public UsuarioUseCaseImpl(UsuarioPersistenciaPort persistenciaPort,
                              UsuarioDomainService domainService) {
        this.persistenciaPort = persistenciaPort;
        this.domainService = domainService;
    }

    @Override
    public Optional<Usuario> login(String username, String password) {
        return persistenciaPort.buscarPorUsername(username)
                .map(usuario -> domainService.validarCredenciales(usuario, password));
    }

    @Override
    public Optional<Usuario> registrar(String username, String password) {
        if (persistenciaPort.buscarPorUsername(username).isPresent()) {
            throw new UsuarioYaExisteException(username);
        }

        Usuario nuevoUsuario = domainService.crearNuevoUsuario(username, password);
        return persistenciaPort.crearConRolUsuario(nuevoUsuario);
    }
}