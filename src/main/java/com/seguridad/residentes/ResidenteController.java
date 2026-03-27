package com.seguridad.residentes;

import com.seguridad.residentes.dto.SolicitudDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/residentes")
public class ResidenteController {

    private final ResidenteService residenteService;

    public ResidenteController(ResidenteService residenteService) {
        this.residenteService = residenteService;
    }

    @PostMapping("/{tenant}/{username}/solicitudes")
    public ResponseEntity<SolicitudResidente> crearSolicitud(
            @PathVariable String tenant,
            @PathVariable String username,
            @RequestBody SolicitudDTO solicitud) {

        SolicitudResidente nueva = residenteService.crearSolicitud(tenant, solicitud, username);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/{tenant}/{username}/historial")
    public ResponseEntity<List<SolicitudResidente>> historial(
            @PathVariable String tenant,
            @PathVariable String username) {

        List<SolicitudResidente> historial = residenteService.obtenerHistorial(tenant, username);
        return ResponseEntity.ok(historial);
    }
}
