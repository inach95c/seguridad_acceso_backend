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
public class MetricasController {

    private final MetricasService metricasService;
    private final RegistroRepository registroRepository;
    private final DestinoRepository destinoRepository;

    public MetricasController(MetricasService metricasService,
                              RegistroRepository registroRepository,
                              DestinoRepository destinoRepository) {
        this.metricasService = metricasService;
        this.registroRepository = registroRepository;
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

        return ResponseEntity.ok(
                metricasService.calcularPermanenciaPorChapa(inicio, fin, umbralHoras)
        );
    }

    // =========================
    // Reincidencias por placa
    // =========================
    @GetMapping("/reincidencias-por-placa")
    public ResponseEntity<ReincidenciaPorPlacaDTO> getReincidenciaPorPlaca(
            @RequestParam String placa,
            @RequestParam int umbral,
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {

        return ResponseEntity.ok(
                metricasService.calcularReincidenciaPorPlaca(placa, umbral, inicio, fin)
        );
    }

    // =========================
    // Turnos
    // =========================
    @GetMapping("/turnos")
    public ResponseEntity<TurnosDTO> getTurnos(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {

        return ResponseEntity.ok(
                metricasService.calcularTurnos(inicio, fin)
        );
    }

    // =========================
    // Visitantes por tipo
    // =========================
    @GetMapping("/visitantes")
    public ResponseEntity<ComparativoDTO> getVisitantesPorTipo(
            @RequestParam Instant inicio,
            @RequestParam Instant fin,
            @RequestParam(defaultValue = "mensual") String granularidad) {

        return ResponseEntity.ok(
                metricasService.calcularVisitantesPorTipo(inicio, fin, granularidad)
        );
    }

    // =========================
    // Ranking de destinos
    // =========================
    @GetMapping("/ranking-destinos")
    public ResponseEntity<List<RankingItemDTO>> getRankingDestinos(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {

        return ResponseEntity.ok(
                metricasService.rankingDestinos(inicio, fin)
        );
    }

    // =========================
    // Actividad de usuarios
    // =========================
    @GetMapping("/actividad-usuarios")
    public ResponseEntity<List<UsuariosActividadDTO>> getActividadUsuarios(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {

        return ResponseEntity.ok(
                metricasService.actividadUsuarios(inicio, fin)
        );
    }

    // =========================
    // Anomalías
    // =========================
    @GetMapping("/anomalias")
    public ResponseEntity<AnomaliasDTO> obtenerAnomalias() {
        return ResponseEntity.ok(metricasService.obtenerAnomalias());
    }

    // =========================
    // Cortes
    // =========================
    @GetMapping("/cortes")
    public ResponseEntity<CortesDTO> obtenerCortes() {
        return ResponseEntity.ok(metricasService.obtenerCortes());
    }

    // =========================
    // Otros
    // =========================
    @GetMapping("/placas")
    public ResponseEntity<List<String>> getPlacasRegistradas(
            @RequestHeader("X-Tenant-ID") String tenant) {
        return ResponseEntity.ok(registroRepository.findDistinctPlacas(tenant));
    }

    @GetMapping("/destinos")
    public ResponseEntity<List<String>> getDestinosRegistrados(
            @RequestHeader("X-Tenant-ID") String tenant) {
        return ResponseEntity.ok(destinoRepository.findDistinctDestinos(tenant));
    }

}
