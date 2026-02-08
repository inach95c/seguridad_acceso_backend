package com.seguridad.metricas;

import com.seguridad.destinos.DestinoRepository;
import com.seguridad.metricas.dto.*;
import com.seguridad.registros.RegistroRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/metricas")
@PreAuthorize("hasAnyRole('GERENTE_ADMIN','MASTER_ADMIN')")
//@PreAuthorize("hasAnyAuthority('ROLE_GERENTE_ADMIN','ROLE_MASTER_ADMIN')")
public class MetricasController {

    private final MetricasService metricasService;
    private final RegistroRepository registroRepository; // 👈 nuevo
    private final DestinoRepository destinoRepository; // 👈 nuevo

    public MetricasController(MetricasService metricasService, RegistroRepository registroRepository, DestinoRepository destinoRepository) {
        this.metricasService = metricasService;
        this.registroRepository = registroRepository; // 👈 inyección
        this.destinoRepository = destinoRepository;
    }

    // =========================
    // Permanencia por chapa
    // =========================
    @GetMapping("/permanencia-por-chapa")
    public ResponseEntity<List<PermanenciaPorChapaDTO>> getPermanenciaPorChapa(
            @RequestParam Instant inicio,
            @RequestParam Instant fin,
            @RequestParam(defaultValue = "24") int umbralHoras) {
        return ResponseEntity.ok(metricasService.calcularPermanenciaPorChapa(inicio, fin, umbralHoras));
    }

    // =========================
    // Reincidencias
    // =========================
   /* @GetMapping("/reincidencias")
    public ResponseEntity<ReincidenciasDTO> getReincidencias(
            @RequestParam List<String> placas,
            @RequestParam int umbral,
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return ResponseEntity.ok(metricasService.calcularReincidencias(placas, umbral, inicio, fin));
    }*/
     
        @GetMapping("/reincidencias-por-placa")
        public ResponseEntity<ReincidenciaPorPlacaDTO> getReincidenciaPorPlaca(
                @RequestParam String placa,
                @RequestParam int umbral,
                @RequestParam Instant inicio,
                @RequestParam Instant fin) {
            return ResponseEntity.ok(metricasService.calcularReincidenciaPorPlaca(placa, umbral, inicio, fin));
        }
    


    // =========================
    // Turnos
    // =========================
    @GetMapping("/turnos")
    public ResponseEntity<TurnosDTO> getTurnos(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return ResponseEntity.ok(metricasService.calcularTurnos(inicio, fin));
    }

    // =========================
    // Visitantes por tipo
    // =========================
    @GetMapping("/visitantes")
    public ResponseEntity<ComparativoDTO> getVisitantesPorTipo(
            @RequestParam Instant inicio,
            @RequestParam Instant fin,
            @RequestParam(defaultValue = "mensual") String granularidad) {
        return ResponseEntity.ok(metricasService.calcularVisitantesPorTipo(inicio, fin, granularidad));
    }

    // =========================
    // Ranking de destinos
    // =========================
    @GetMapping("/ranking-destinos")
    public ResponseEntity<List<RankingItemDTO>> getRankingDestinos(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return ResponseEntity.ok(metricasService.rankingDestinos(inicio, fin));
    }

    // =========================
    // Actividad de usuarios
    // =========================
    @GetMapping("/actividad-usuarios")
    public List<UsuariosActividadDTO> getActividadUsuarios() {
    	// Spring Boot convierte automáticamente esta lista en JSON plano
    	return metricasService.actividadUsuarios();
        }
       

    // =========================
    // Anomalías
    // =========================
    
        @GetMapping("/anomalias")
        public AnomaliasDTO obtenerAnomalias() {
            return metricasService.obtenerAnomalias();
        }
    

    // =========================
    // Cortes
    // =========================

            @GetMapping("/cortes")
            public CortesDTO obtenerCortes() {
                return metricasService.obtenerCortes();
            }
    
    
    // otros
    
    @GetMapping("/placas")
    public ResponseEntity<List<String>> getPlacasRegistradas() {
        return ResponseEntity.ok(registroRepository.findDistinctPlacas());
    }
    
    @GetMapping("/destinos")
    public ResponseEntity<List<String>> getDestinosRegistrados() {
        return ResponseEntity.ok(destinoRepository.findDistinctDestinos());
    }


}
