package com.seguridad.registros;

import com.seguridad.metricas.dto.UsuariosActividadDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    // =========================
    // Búsquedas multi‑tenant
    // =========================
    Optional<Registro> findByFolioAndTenant(String folio, String tenant);

    List<Registro> findByEstadoAndTenant(Registro.Estado estado, String tenant);

    List<Registro> findByIngresoFechaHoraBetweenAndTenant(Instant inicio, Instant fin, String tenant);

    List<Registro> findByPlacaAndEstadoAndTenant(String placa, Registro.Estado estado, String tenant);

    List<Registro> findAllByPlacaAndIngresoFechaHoraBetweenAndTenant(String placa, Instant inicio, Instant fin, String tenant);

    // =========================
    // Búsquedas sin tenant (compatibilidad)
    // =========================
    Optional<Registro> findByFolio(String folio);

    List<Registro> findByEstado(Registro.Estado estado);

    List<Registro> findByIngresoFechaHoraBetween(Instant inicio, Instant fin);
    
 // =========================
 // Permanencia por chapa
 // =========================
 @Query(value = "SELECT r.placa, r.tipo_visitante, " +
         "AVG(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS promedio, " +
         "MAX(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS maximo, " +
         "MIN(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS minimo, " +
         "r.sospechoso " +
         "FROM registros r " +
         "WHERE r.salida_fecha_hora IS NOT NULL " +
         "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
         "AND r.tenant = :tenant " +
         "GROUP BY r.placa, r.tipo_visitante, r.sospechoso", nativeQuery = true)
 List<Object[]> permanenciaPorChapa(@Param("inicio") Instant inicio,
                                    @Param("fin") Instant fin,
                                    @Param("tenant") String tenant);

 @Query(value = "SELECT COUNT(*) FROM registros r " +
         "WHERE r.placa = :placa " +
         "AND r.salida_fecha_hora IS NOT NULL " +
         "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
         "AND TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora) > :umbralHoras " +
         "AND r.tenant = :tenant", nativeQuery = true)
 Long countPermanenciaOverUmbralByPlaca(@Param("placa") String placa,
                                        @Param("inicio") Instant inicio,
                                        @Param("fin") Instant fin,
                                        @Param("umbralHoras") int umbralHoras,
                                        @Param("tenant") String tenant);

 // =========================
 // Permanencia por tipo
 // =========================
 @Query(value = "SELECT r.tipo_visitante, " +
         "AVG(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS promedio, " +
         "MAX(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS maximo, " +
         "MIN(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS minimo " +
         "FROM registros r " +
         "WHERE r.salida_fecha_hora IS NOT NULL " +
         "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
         "AND r.tenant = :tenant " +
         "GROUP BY r.tipo_visitante", nativeQuery = true)
 List<Object[]> permanenciaPorTipo(@Param("inicio") Instant inicio,
                                   @Param("fin") Instant fin,
                                   @Param("tenant") String tenant);

 @Query(value = "SELECT COUNT(*) FROM registros r " +
         "WHERE r.tipo_visitante = :tipo " +
         "AND r.salida_fecha_hora IS NOT NULL " +
         "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
         "AND TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora) > :umbralHoras " +
         "AND r.tenant = :tenant", nativeQuery = true)
 Long countPermanenciaOverUmbralByTipo(@Param("tipo") String tipo,
                                       @Param("inicio") Instant inicio,
                                       @Param("fin") Instant fin,
                                       @Param("umbralHoras") int umbralHoras,
                                       @Param("tenant") String tenant);

 // =========================
 // Ranking destinos
 // =========================
 @Query(value = "SELECT d.nombre, COUNT(*) as total " +
         "FROM registros r JOIN destinos d ON r.destino_id = d.id " +
         "WHERE r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
         "AND r.tenant = :tenant " +
         "GROUP BY d.nombre ORDER BY total DESC", nativeQuery = true)
 List<Object[]> rankingDestinos(@Param("inicio") Instant inicio,
                                @Param("fin") Instant fin,
                                @Param("tenant") String tenant);

 // =========================
 // Actividad usuarios
 // =========================
 @Query(value = "SELECT u.username, COUNT(*) as total " +
         "FROM registros r JOIN usuarios u ON r.usuario_entrada_id = u.id " +
         "WHERE r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
         "AND r.tenant = :tenant " +
         "GROUP BY u.username ORDER BY total DESC", nativeQuery = true)
 List<Object[]> actividadUsuarios(@Param("inicio") Instant inicio,
                                  @Param("fin") Instant fin,
                                  @Param("tenant") String tenant);


    // =========================
    // Turnos (multi‑tenant)
    // =========================
 
//=========================
//Turnos (multi‑tenant)
//=========================
@Query("""
  SELECT COUNT(r)
  FROM Registro r
  WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
    AND EXTRACT(HOUR FROM r.ingresoFechaHora) BETWEEN 6 AND 13
    AND r.tenant = :tenant
""")
Long turnoManana(@Param("inicio") Instant inicio,
               @Param("fin") Instant fin,
               @Param("tenant") String tenant);

@Query("""
  SELECT COUNT(r)
  FROM Registro r
  WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
    AND EXTRACT(HOUR FROM r.ingresoFechaHora) BETWEEN 14 AND 20
    AND r.tenant = :tenant
""")
Long turnoTarde(@Param("inicio") Instant inicio,
              @Param("fin") Instant fin,
              @Param("tenant") String tenant);

@Query("""
  SELECT COUNT(r)
  FROM Registro r
  WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
    AND (
      EXTRACT(HOUR FROM r.ingresoFechaHora) >= 21
      OR EXTRACT(HOUR FROM r.ingresoFechaHora) <= 5
    )
    AND r.tenant = :tenant
""")
Long turnoNoche(@Param("inicio") Instant inicio,
              @Param("fin") Instant fin,
              @Param("tenant") String tenant);

   /* @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND HOUR(FROM_UNIXTIME(EXTRACT(EPOCH FROM r.ingresoFechaHora))) BETWEEN 6 AND 13
          AND r.tenant = :tenant
    """)
    Long turnoManana(@Param("inicio") Instant inicio,
                     @Param("fin") Instant fin,
                     @Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND HOUR(FROM_UNIXTIME(EXTRACT(EPOCH FROM r.ingresoFechaHora))) BETWEEN 14 AND 20
          AND r.tenant = :tenant
    """)
    Long turnoTarde(@Param("inicio") Instant inicio,
                    @Param("fin") Instant fin,
                    @Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND (
                HOUR(FROM_UNIXTIME(EXTRACT(EPOCH FROM r.ingresoFechaHora))) >= 21
                OR HOUR(FROM_UNIXTIME(EXTRACT(EPOCH FROM r.ingresoFechaHora))) <= 5
              )
          AND r.tenant = :tenant
    """)
    Long turnoNoche(@Param("inicio") Instant inicio,
                    @Param("fin") Instant fin,
                    @Param("tenant") String tenant);
*/
    // =========================
    // Visitantes por tipo (multi‑tenant)
    // =========================
    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.tipoVisitante = com.seguridad.registros.Registro$TipoVisitante.VISITANTE
          AND r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND r.tenant = :tenant
    """)
    Long countVisitantes(@Param("inicio") Instant inicio,
                         @Param("fin") Instant fin,
                         @Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.tipoVisitante = com.seguridad.registros.Registro$TipoVisitante.PROVEEDOR
          AND r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND r.tenant = :tenant
    """)
    Long countProveedores(@Param("inicio") Instant inicio,
                          @Param("fin") Instant fin,
                          @Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.tipoVisitante = com.seguridad.registros.Registro$TipoVisitante.OTRO
          AND r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND r.tenant = :tenant
    """)
    Long countOtros(@Param("inicio") Instant inicio,
                    @Param("fin") Instant fin,
                    @Param("tenant") String tenant);

    // =========================
    // Cortes (multi‑tenant)
    // =========================
    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.ingresoFechaHora BETWEEN :inicio AND :fin
          AND r.tenant = :tenant
    """)
    Long contarPorRango(@Param("inicio") Instant inicio,
                        @Param("fin") Instant fin,
                        @Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.salidaFechaHora IS NOT NULL
          AND r.tenant = :tenant
    """)
    Long contarConSalida(@Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.salidaFechaHora IS NULL
          AND r.tenant = :tenant
    """)
    Long contarSinSalida(@Param("tenant") String tenant);

    @Query("""
        SELECT r.tipoVisitante, COUNT(r)
        FROM Registro r
        WHERE r.tenant = :tenant
        GROUP BY r.tipoVisitante
    """)
    List<Object[]> contarPorTipoVisitante(@Param("tenant") String tenant);

    @Query("""
        SELECT r.estado, COUNT(r)
        FROM Registro r
        WHERE r.tenant = :tenant
        GROUP BY r.estado
    """)
    List<Object[]> contarPorEstado(@Param("tenant") String tenant);

    // =========================
    // Métricas multi‑tenant faltantes
    // =========================
    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.salidaFechaHora IS NULL
          AND r.ingresoFechaHora < :fechaLimite
          AND r.tenant = :tenant
    """)
    Long contarSinSalidaOverUmbral(@Param("fechaLimite") Instant fechaLimite,
                                   @Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.estado = com.seguridad.registros.Registro$Estado.SALIDA_PENDIENTE
          AND r.tenant = :tenant
    """)
    Long contarIncompletos(@Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.sospechoso = true
          AND r.tenant = :tenant
    """)
    Long contarDuplicados(@Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.estado = com.seguridad.registros.Registro$Estado.ABIERTO
          AND r.tenant = :tenant
    """)
    Long contarFueraDeHorario(@Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.usuarioEntrada IS NULL
          AND r.tenant = :tenant
    """)
    Long contarRegistrosSinUsuario(@Param("tenant") String tenant);

    @Query("""
        SELECT COUNT(r)
        FROM Registro r
        WHERE r.estado = com.seguridad.registros.Registro$Estado.COMPLETADO
          AND r.sospechoso = true
          AND r.tenant = :tenant
    """)
    Long contarEventosCriticos(@Param("tenant") String tenant);

 // =========================
 // Otros
 // =========================
 @Query("SELECT DISTINCT r.placa FROM Registro r WHERE r.tenant = :tenant")
 List<String> findDistinctPlacas(@Param("tenant") String tenant);

 boolean existsByUsuarioEntradaId(Long usuarioId);


}
