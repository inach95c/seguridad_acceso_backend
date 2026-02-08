

package com.seguridad.registros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seguridad.metricas.dto.UsuariosActividadDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    // =========================
    // Búsquedas básicas
    // =========================
    Optional<Registro> findByFolio(String folio);

    List<Registro> findByEstado(Registro.Estado estado);

    List<Registro> findByPlacaAndEstado(String placa, Registro.Estado estado);

    List<Registro> findByIngresoFechaHoraBetween(Instant inicio, Instant fin);

    // =========================
    // Métricas
    // =========================

    long countByIngresoFechaHoraBetween(Instant inicio, Instant fin);

    // Permanencia (horas) — MySQL
    @Query(value = "SELECT AVG(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) " +
                   "FROM registros r WHERE r.salida_fecha_hora IS NOT NULL " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Double avgPermanenciaHoras(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    @Query(value = "SELECT MAX(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) " +
                   "FROM registros r WHERE r.salida_fecha_hora IS NOT NULL " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long maxPermanenciaHoras(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    @Query(value = "SELECT MIN(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) " +
                   "FROM registros r WHERE r.salida_fecha_hora IS NOT NULL " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long minPermanenciaHoras(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    // Registros sin salida sobre umbral
    @Query(value = "SELECT COUNT(*) FROM registros r " +
                   "WHERE r.salida_fecha_hora IS NULL " +
                   "AND TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, NOW()) > :umbralHoras " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long countSinSalidaOverUmbral(@Param("inicio") Instant inicio, @Param("fin") Instant fin, @Param("umbralHoras") int umbralHoras);

    // Turnos
    @Query(value = "SELECT COUNT(*) FROM registros r WHERE HOUR(r.ingreso_fecha_hora) BETWEEN 6 AND 13 " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long turnoManana(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    @Query(value = "SELECT COUNT(*) FROM registros r WHERE HOUR(r.ingreso_fecha_hora) BETWEEN 14 AND 21 " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long turnoTarde(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    @Query(value = "SELECT COUNT(*) FROM registros r WHERE (HOUR(r.ingreso_fecha_hora) >= 22 OR HOUR(r.ingreso_fecha_hora) < 6) " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long turnoNoche(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    // Registros sin salida
    @Query(value = "SELECT COUNT(*) FROM registros r WHERE r.estado = 'SALIDA_PENDIENTE' " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long countSinSalida(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    // Incompletos
    @Query(value = "SELECT COUNT(*) FROM registros r " +
                   "WHERE (r.placa IS NULL OR r.destino_id IS NULL) " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long countIncompletos(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    // =========================
    // Conteo por tipo de visitante
    // =========================
    @Query(value = "SELECT COUNT(*) FROM registros r WHERE r.tipo_visitante = 'VISITANTE' " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long countVisitantes(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    @Query(value = "SELECT COUNT(*) FROM registros r WHERE r.tipo_visitante = 'PROVEEDOR' " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long countProveedores(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    @Query(value = "SELECT COUNT(*) FROM registros r WHERE r.tipo_visitante = 'OTRO' " +
                   "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin", nativeQuery = true)
    Long countOtros(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    // =========================
    // Reincidencias por placa
    // =========================
    @Query(value = "SELECT COUNT(*) FROM registros r WHERE r.placa = :placa", nativeQuery = true)
    Long countByPlaca(@Param("placa") String placa);

    // Ranking de destinos más visitados
    @Query(value = "SELECT d.nombre, COUNT(*) as total " +
                   "FROM registros r JOIN destinos d ON r.destino_id = d.id " +
                   "WHERE r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
                   "GROUP BY d.nombre ORDER BY total DESC", nativeQuery = true)
    List<Object[]> rankingDestinos(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

    // Actividad de usuarios
    @Query(value = "SELECT u.username, COUNT(*) as total " +
                   "FROM registros r JOIN usuarios u ON r.usuario_entrada_id = u.id " +
                   "WHERE r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
                   "GROUP BY u.username ORDER BY total DESC", nativeQuery = true)
    List<Object[]> actividadUsuarios(@Param("inicio") Instant inicio, @Param("fin") Instant fin);
    
    
 // =========================
 // Permanencia por chapa
 // =========================

 // Promedio, máximo y mínimo de horas de permanencia agrupado por placa
  /*  @Query(value = "SELECT r.placa, r.tipo_visitante, " +
            "AVG(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS promedio, " +
            "MAX(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS maximo, " +
            "MIN(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS minimo " +
            "FROM registros r " +
            "WHERE r.salida_fecha_hora IS NOT NULL " +
            "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
            "GROUP BY r.placa, r.tipo_visitante", nativeQuery = true)
List<Object[]> permanenciaPorChapa(@Param("inicio") Instant inicio, @Param("fin") Instant fin);
*/
    
    @Query(value = "SELECT r.placa, r.tipo_visitante, " +
            "AVG(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS promedio, " +
            "MAX(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS maximo, " +
            "MIN(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS minimo, " +
            "r.sospechoso " + // 👈 añadimos la columna
            "FROM registros r " +
            "WHERE r.salida_fecha_hora IS NOT NULL " +
            "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
            "GROUP BY r.placa, r.tipo_visitante, r.sospechoso", nativeQuery = true)
List<Object[]> permanenciaPorChapa(@Param("inicio") Instant inicio, @Param("fin") Instant fin);


 // Conteo de casos largos por placa (permanencias mayores al umbral)
 @Query(value = "SELECT COUNT(*) FROM registros r " +
                "WHERE r.placa = :placa " +
                "AND r.salida_fecha_hora IS NOT NULL " +
                "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
                "AND TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora) > :umbralHoras", nativeQuery = true)
 Long countPermanenciaOverUmbralByPlaca(@Param("placa") String placa,
                                        @Param("inicio") Instant inicio,
                                        @Param("fin") Instant fin,
                                        @Param("umbralHoras") int umbralHoras);

    


//Promedio, máximo y mínimo de horas de permanencia agrupado por tipo_visitante
@Query(value = "SELECT r.tipo_visitante, " +
             "AVG(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS promedio, " +
             "MAX(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS maximo, " +
             "MIN(TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora)) AS minimo " +
             "FROM registros r " +
             "WHERE r.salida_fecha_hora IS NOT NULL " +
             "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
             "GROUP BY r.tipo_visitante", nativeQuery = true)
List<Object[]> permanenciaPorTipo(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

@Query(value = "SELECT COUNT(*) FROM registros r " +
             "WHERE r.tipo_visitante = :tipo " +
             "AND r.salida_fecha_hora IS NOT NULL " +
             "AND r.ingreso_fecha_hora BETWEEN :inicio AND :fin " +
             "AND TIMESTAMPDIFF(HOUR, r.ingreso_fecha_hora, r.salida_fecha_hora) > :umbralHoras", nativeQuery = true)
Long countPermanenciaOverUmbralByTipo(@Param("tipo") String tipo,
                                    @Param("inicio") Instant inicio,
                                    @Param("fin") Instant fin,
                                    @Param("umbralHoras") int umbralHoras);



long countByPlacaAndIngresoFechaHoraBetween(String placa, Instant inicio, Instant fin);

//Obtener todos los registros de una placa en un rango 
List<Registro> findAllByPlacaAndIngresoFechaHoraBetween(String placa, Instant inicio, Instant fin);


// para destinosReport
    @Query("SELECT DISTINCT r.placa FROM Registro r")
    List<String> findDistinctPlacas();
    
    
   

        // para usuarioReport

        // Ingresos por usuario (todos los registros)
        @Query("SELECT new com.seguridad.metricas.dto.UsuariosActividadDTO(" +
               "r.usuarioEntrada.username, COUNT(r), 0L) " +
               "FROM Registro r " +
               "WHERE r.usuarioEntrada IS NOT NULL " +
               "GROUP BY r.usuarioEntrada.username")
        List<UsuariosActividadDTO> contarIngresos();

        // Salidas por usuario (todos los registros)
        @Query("SELECT new com.seguridad.metricas.dto.UsuariosActividadDTO(" +
               "r.usuarioSalida.username, 0L, COUNT(r)) " +
               "FROM Registro r " +
               "WHERE r.usuarioSalida IS NOT NULL " +
               "GROUP BY r.usuarioSalida.username")
        List<UsuariosActividadDTO> contarSalidas();
        
        
        // para anomalias
    
            /**
             * Registros sin salida cuya fecha de ingreso es anterior a la fecha límite.
             * Se pasa la fecha límite desde el servicio (ej: ayer).
             */
        @Query("SELECT COUNT(r) FROM Registro r WHERE r.salidaFechaHora IS NULL AND r.ingresoFechaHora < :fechaLimite")
        Long contarSinSalidaOverUmbral(@Param("fechaLimite") Instant fechaLimite);


            /**
             * Registros incompletos (sin ingreso o sin salida).
             */
            @Query("SELECT COUNT(r) FROM Registro r WHERE r.ingresoFechaHora IS NULL OR r.salidaFechaHora IS NULL")
            Long contarIncompletos();

            /**
             * Registros duplicados por placa (ejemplo de criterio).
             * ⚠️ Usamos SQL nativo porque JPQL no soporta bien HAVING en subqueries.
             * Ajusta según el criterio que defina "duplicado" en tu negocio (folio, placa, etc.).
             */
            @Query(value = "SELECT COUNT(*) FROM registros r " +
                           "WHERE r.placa IN (" +
                           "SELECT placa FROM registros GROUP BY placa HAVING COUNT(*) > 1)", 
                   nativeQuery = true)
            Long contarDuplicados();

            /**
             * Registros fuera de horario permitido (ejemplo: antes de las 6am o después de las 10pm).
             * ⚠️ Ajusta según tu motor de BD:
             * - MySQL: HOUR(r.ingreso_fecha_hora)
             * - PostgreSQL: EXTRACT(HOUR FROM r.ingreso_fecha_hora)
             */
            @Query(value = "SELECT COUNT(*) FROM registros r " +
                           "WHERE HOUR(r.ingreso_fecha_hora) < 6 OR HOUR(r.ingreso_fecha_hora) > 22", 
                   nativeQuery = true)
            Long contarFueraDeHorario();

            /**
             * Registros sin usuario asociado (ni entrada ni salida).
             */
            @Query("SELECT COUNT(r) FROM Registro r WHERE r.usuarioEntrada IS NULL AND r.usuarioSalida IS NULL")
            Long contarRegistrosSinUsuario();

            /**
             * Eventos críticos: aquí usamos el campo sospechoso como criterio.
             * Si sospechoso = true, lo consideramos crítico.
             */
            @Query("SELECT COUNT(r) FROM Registro r WHERE r.sospechoso = true")
            Long contarEventosCriticos();
            
            
            //  para cortes
          

                @Query("SELECT COUNT(r) FROM Registro r WHERE r.ingresoFechaHora >= :inicio AND r.ingresoFechaHora < :fin")
                Long contarPorRango(@Param("inicio") Instant inicio, @Param("fin") Instant fin);

                @Query("SELECT COUNT(r) FROM Registro r WHERE r.salidaFechaHora IS NOT NULL")
                Long contarConSalida();

                @Query("SELECT COUNT(r) FROM Registro r WHERE r.salidaFechaHora IS NULL")
                Long contarSinSalida();

                @Query("SELECT r.tipoVisitante, COUNT(r) FROM Registro r GROUP BY r.tipoVisitante")
                List<Object[]> contarPorTipoVisitante();

                @Query("SELECT r.estado, COUNT(r) FROM Registro r GROUP BY r.estado")
                List<Object[]> contarPorEstado();
            

                boolean existsByUsuarioEntradaId(Long usuarioId);
        

}// fin
