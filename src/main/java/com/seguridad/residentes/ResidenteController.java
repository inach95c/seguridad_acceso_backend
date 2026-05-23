package com.seguridad.residentes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seguridad.residentes.dto.SolicitudDTO;

import java.util.List;

@RestController
@RequestMapping("/residentes")
public class ResidenteController {

    private final ResidenteService residenteService;

    public ResidenteController(ResidenteService residenteService) {
        this.residenteService = residenteService;
    }

    // ============================================================
    // ✅ Crear solicitud de acceso
    // POST /residentes/{tenant}/{username}/solicitudes
    // ============================================================
    @PostMapping("/{tenant}/{username}/solicitudes")
    public ResponseEntity<SolicitudResidente> crearSolicitud(
            @PathVariable String tenant,
            @PathVariable String username,
            @RequestBody SolicitudDTO solicitudDTO
    ) {
        SolicitudResidente nueva = residenteService.crearSolicitud(tenant, solicitudDTO, username);
        return ResponseEntity.ok(nueva);
    }

    // ============================================================
    // ✅ Obtener historial del residente
    // GET /residentes/{tenant}/{username}/historial
    // ============================================================
    @GetMapping("/{tenant}/{username}/historial")
    public ResponseEntity<List<SolicitudResidente>> obtenerHistorial(
            @PathVariable String tenant,
            @PathVariable String username
    ) {
        List<SolicitudResidente> historial =
                residenteService.obtenerHistorial(tenant, username);

        return ResponseEntity.ok(historial);
    }




    // ============================================================
    // 🔍 Autocompletado: buscar último registro del visitante
    // GET /residentes/{tenant}/visitante/{nombre}
    // ============================================================

    @GetMapping("/{tenant}/visitante/{nombre}")
    public ResponseEntity<?> buscarVisitante(
            @PathVariable String tenant,
            @PathVariable String nombre) {

        return residenteService.buscarUltimoRegistro(nombre, tenant)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }


// verificar

    // ============================================================
    // 🔴 Cancelar solicitud (solo residente dueño)
    // PUT /residentes/{tenant}/{username}/solicitudes/{id}/cancelar
    // ============================================================
    @PutMapping("/{tenant}/{username}/solicitudes/{id}/cancelar")
    public ResponseEntity<Void> cancelarSolicitud(
            @PathVariable String tenant,
            @PathVariable String username,
            @PathVariable Long id
    ) {
        residenteService.cancelarSolicitud(tenant, username, id);
        return ResponseEntity.ok().build();
    }



}
