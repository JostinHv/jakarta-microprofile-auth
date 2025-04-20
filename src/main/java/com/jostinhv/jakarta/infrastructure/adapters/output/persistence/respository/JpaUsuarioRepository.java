package com.jostinhv.jakarta.infrastructure.adapters.output.persistence.respository;

import com.jostinhv.jakarta.application.ports.output.UsuarioPersistenciaPort;
import com.jostinhv.jakarta.domain.annotations.Adapter;
import com.jostinhv.jakarta.domain.model.Usuario;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.entities.RolEntity;
import com.jostinhv.jakarta.infrastructure.adapters.output.persistence.entities.UsuarioEntity;
import com.jostinhv.jakarta.infrastructure.mapper.UsuarioMapper;
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
    public Optional<Usuario> crearConRolUsuario(Usuario usuario) {
        try {
            // Buscar rol USER usando Criteria
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<RolEntity> cq = cb.createQuery(RolEntity.class);
            Root<RolEntity> root = cq.from(RolEntity.class);
            cq.select(root).where(cb.equal(root.get("nombre"), "user"));

            RolEntity rolUsuario;
            try {
                rolUsuario = entityManager.createQuery(cq).getSingleResult();
            } catch (Exception e) {
                // Si no existe el rol, lo creamos
                rolUsuario = new RolEntity();
                rolUsuario.setNombre("user");
                entityManager.persist(rolUsuario);
            }

            // Crear usuario con rol
            UsuarioEntity usuarioEntity = new UsuarioEntity(
                    usuario.getUsername(),
                    usuario.getPassword(),
                    Set.of(rolUsuario)
            );

            entityManager.persist(usuarioEntity);
            entityManager.flush();

            return Optional.of(UsuarioMapper.toDomain(usuarioEntity));

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