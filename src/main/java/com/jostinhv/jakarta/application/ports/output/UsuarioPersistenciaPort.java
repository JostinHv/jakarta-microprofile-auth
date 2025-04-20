package com.jostinhv.jakarta.application.ports.output;

import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.domain.annotations.Port;

import java.util.Optional;

@Port(type = Port.Type.OUTPUT)
public interface UsuarioPersistenciaPort {

    Optional<Usuario> crearConRolUsuario(Usuario usuario);

    Optional<Usuario> buscarPorUsername(String username);


}
