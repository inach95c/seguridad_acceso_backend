package com.seguridad.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    // =========================
    // Eliminación por usuario
    // =========================
    void deleteByUsuarioId(Long usuarioId);

    // =========================
    // Métodos antiguos (compatibilidad)
    // =========================

    // Cuenta intentos fallidos sin tenant (para sistemas antiguos)
    @Query("SELECT COUNT(b) FROM Bitacora b WHERE b.descripcion = 'INTENTO_FALLIDO'")
    Long contarIntentosFallidos();

    @Query("SELECT COUNT(b) FROM Bitacora b WHERE b.descripcion = :evento")
    Long contarPorEvento(@Param("evento") String evento);

    List<Bitacora> findTop5ByOrderByFechaHoraDesc();

    // =========================
    // Métodos multi‑tenant (nuevos)
    // =========================

    @Query("""
        SELECT COUNT(b)
        FROM Bitacora b
        WHERE b.descripcion = 'INTENTO_FALLIDO'
          AND b.tenant = :tenant
    """)
    Long contarIntentosFallidos(@Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(b)
        FROM Bitacora b
        WHERE b.descripcion = :evento
          AND b.tenant = :tenant
    """)
    Long contarPorEvento(@Param("evento") String evento,
                         @Param("tenant") String tenant);
}
