package com.jostinhv.jakarta.infrastructure.config;

import com.jostinhv.jakarta.application.ports.output.UsuarioPersistenciaPort;
import com.jostinhv.jakarta.application.usecases.UsuarioUseCaseImpl;
import com.jostinhv.jakarta.domain.service.UsuarioDomainService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioConfig {

    @Produces
    @ApplicationScoped
    @Transactional
    public UsuarioUseCaseImpl usuarioUseCase(
            UsuarioPersistenciaPort persistenciaPort,
            UsuarioDomainService domainService) {
        return new UsuarioUseCaseImpl(persistenciaPort, domainService);
    }

    @Produces
    @ApplicationScoped
    public UsuarioDomainService usuarioDomainService() {
        return new UsuarioDomainService();
    }
}