package com.jostinhv.jakarta.infrastructure.adapters.output.persistence.mapper;

import com.jostinhv.jakarta.application.dto.response.UsuarioResponse;
import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.entities.UsuarioEntity;
import jakarta.enterprise.context.Dependent;

@Dependent
public class UsuarioMapper {

    public static Usuario toDomain(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            return null;
        }
        return new Usuario(usuarioEntity.getId(), usuarioEntity.getUsername(), usuarioEntity.getPassword(), RolMapper.toDomainSet(usuarioEntity.getRoles()));
    }

    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioEntity(usuario.getId(), usuario.getUsername(), usuario.getPassword(), RolMapper.toEntitySet(usuario.getRoles()));
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), RolMapper.toResponseList(usuario.getRoles()));
    }

}
