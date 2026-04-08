package com.seguridad.destinos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DestinoRepository extends JpaRepository<Destino, Long> {

    // Destinos activos por tenant
    List<Destino> findByActivoTrueAndTenant(String tenant);

    // Lista de nombres de destinos (global)
    @Query("SELECT DISTINCT d.nombre FROM Destino d")
    List<String> findDistinctDestinos();

    // Lista de nombres de destinos por tenant
    @Query("SELECT DISTINCT d.nombre FROM Destino d WHERE d.tenant = :tenant")
    List<String> findDistinctDestinos(@Param("tenant") String tenant);
}
