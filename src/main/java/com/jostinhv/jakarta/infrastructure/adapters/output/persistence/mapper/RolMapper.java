package com.jostinhv.jakarta.infrastructure.adapters.output.persistence.mapper;

import com.jostinhv.jakarta.application.dto.response.RolResponse;
import com.jostinhv.jakarta.domain.model.Rol;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.entities.RolEntity;
import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Dependent
public class RolMapper {

    public static RolEntity toEntity(Rol rol) {
        return new RolEntity(rol.getId(), rol.getNombre());
    }

    public static Rol toDomain(RolEntity rolEntity) {
        return new Rol(rolEntity.getId(), rolEntity.getNombre());
    }

    public static Set<RolEntity> toEntitySet(Set<Rol> roles) {
        return roles.stream().map(RolMapper::toEntity).collect(Collectors.toSet());
    }

    public static Set<Rol> toDomainSet(Set<RolEntity> roles) {
        return roles.stream().map(RolMapper::toDomain).collect(Collectors.toSet());
    }

    public static List<RolResponse> toResponseList(Set<Rol> roles) {
        return roles.stream().map(rol -> new RolResponse(rol.getId(), rol.getNombre())).collect(Collectors.toList());
    }

}
