package com.seguridad.residentes;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    // ✅ Crear solicitud de acceso
    // ============================================================
    public SolicitudResidente crearSolicitud(String tenant, SolicitudDTO dto, String username) {

        // 1. Buscar destino por ID
        Destino destino = destinoRepository.findById((long) dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        // 2. Crear nueva solicitud
        SolicitudResidente solicitud = new SolicitudResidente();
        solicitud.setTenant(tenant);
        solicitud.setResidenteUsername(username);
        solicitud.setVisitante(dto.getVisitante());
        solicitud.setDestino(destino);
        solicitud.setFechaHora(Instant.parse(dto.getFechaHora() + "Z"));
        solicitud.setEstado("PENDIENTE");

        // 3. Guardar en BD
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
