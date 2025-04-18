package com.jostinhv.jakarta.infrastructure.adapters.output.persistence.respository;

import com.jostinhv.jakarta.application.ports.output.UsuarioPersistenciaPort;
import com.jostinhv.jakarta.domain.annotations.Adapter;
import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.entities.RolEntity;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.entities.UsuarioEntity;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.mapper.UsuarioMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;

@Adapter(type = Adapter.AdapterType.JPA)
@ApplicationScoped
@Transactional
public class JpaUsuarioRepository implements UsuarioPersistenciaPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> crear(Usuario usuario) {
        try {
            RolEntity rol = new RolEntity();
            rol.setNombre("user");
            if (entityManager.find(RolEntity.class, 1) == null) {
                entityManager.persist(rol);
            } else {
                rol = entityManager.find(RolEntity.class, 1);
            }
            Set<RolEntity> roles = Set.of(rol);
            UsuarioEntity user = new UsuarioEntity(usuario.getUsername(), usuario.getPassword(), roles);
            entityManager.persist(user);
            return Optional.of(UsuarioMapper.toDomain(user));
        } catch (Exception e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
            Root<UsuarioEntity> root = cq.from(UsuarioEntity.class);
            cq.select(root).where(cb.equal(root.get("username"), username));
            UsuarioEntity entity = entityManager.createQuery(cq).getSingleResult();
            return Optional.of(UsuarioMapper.toDomain(entity));
        } catch (Exception e) {
            System.out.println("Error al buscar el usuario: " + e.getMessage());
            return Optional.empty();
        }
    }
}