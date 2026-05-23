package com.seguridad.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);

    // =========================
    // Versión antigua (compatibilidad)
    // =========================
    Long countByActivoFalse();

    Long countByRol(Usuario.Rol rol);

    List<Usuario> findByRolAndTelegramIdIsNotNull(Usuario.Rol rol);

    List<Usuario> findByRolInAndTelegramIdIsNotNull(Iterable<Usuario.Rol> roles);

    List<Usuario> findByTelegramIdIsNotNull();

    List<Usuario> findByActivoTrue();

    List<Usuario> findByLastSeenAfter(Instant time);

    // =========================
    // Versión multi‑tenant
    // =========================
    Long countByActivoFalseAndTenant(String tenant);
}
