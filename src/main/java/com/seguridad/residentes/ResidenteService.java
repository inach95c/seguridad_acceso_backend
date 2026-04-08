package com.seguridad.residentes;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.seguridad.residentes.dto.SolicitudDTO;

@Service
public class ResidenteService {

    private final SolicitudResidenteRepository solicitudRepository;
    private final DestinoRepository destinoRepository;

    public ResidenteService(SolicitudResidenteRepository solicitudRepository,
                            DestinoRepository destinoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.destinoRepository = destinoRepository;
    }

    // ============================================================
    // ✅ Crear solicitud de acceso (versión corregida)
    // ============================================================
    public SolicitudResidente crearSolicitud(String tenant, SolicitudDTO dto, String username) {

        Destino destino = destinoRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        SolicitudResidente solicitud = new SolicitudResidente();
        solicitud.setTenant(tenant);
        solicitud.setResidenteUsername(username);
        solicitud.setVisitante(dto.getVisitante());
        solicitud.setDestino(destino);

        // Conversión flexible de fecha/hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "[yyyy-MM-dd'T'HH:mm]"
              + "[yyyy-MM-dd'T'HH:mm:ss]"
              + "[yyyy-MM-dd'T'HH:mm:ss.SSS]"
              + "[yyyy-MM-dd'T'HH:mm'Z']"
              + "[yyyy-MM-dd'T'HH:mm:ss'Z']"
        );

        LocalDateTime ldt = LocalDateTime.parse(dto.getFechaHora(), formatter);
        Instant fechaHoraUTC = ldt.atZone(ZoneId.of("UTC")).toInstant();
        solicitud.setFechaHora(fechaHoraUTC);

        solicitud.setEstado("PENDIENTE");

        return solicitudRepository.save(solicitud);
    }

    // ============================================================
    // ✅ Obtener historial del residente
    // ============================================================
    public List<SolicitudResidente> obtenerHistorial(String tenant, String username) {
        return solicitudRepository.findByTenantAndResidenteUsernameOrderByCreadoEnDesc(
                tenant, username
        );
    }
}
