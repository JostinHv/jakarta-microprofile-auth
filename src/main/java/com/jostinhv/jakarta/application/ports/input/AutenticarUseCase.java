package com.jostinhv.jakarta.application.ports.input;

import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.domain.annotations.Port;

import java.util.Optional;

@Port(type = Port.Type.INPUT)
public interface AutenticarUseCase {
    Optional<Usuario> login(String username, String password);

}
