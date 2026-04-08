package com.seguridad.residentes;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        // 1. Buscar destino por ID
        Destino destino = destinoRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        // 2. Crear nueva solicitud
        SolicitudResidente solicitud = new SolicitudResidente();
        solicitud.setTenant(tenant);
        solicitud.setResidenteUsername(username);
        solicitud.setVisitante(dto.getVisitante());
        solicitud.setDestino(destino);

        // 3. Conversión segura de fecha/hora desde Angular
        LocalDateTime ldt = LocalDateTime.parse(dto.getFechaHora());
        Instant fechaHoraUTC = ldt.atZone(ZoneId.of("UTC")).toInstant();
        solicitud.setFechaHora(fechaHoraUTC);

        solicitud.setEstado("PENDIENTE");

        // 4. Guardar en BD
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
