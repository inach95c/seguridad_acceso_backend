package com.seguridad.residentes;

import com.seguridad.residentes.dto.SolicitudDTO;
import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ResidenteService {

    private final SolicitudResidenteRepository repository;
    private final NotificacionService notificacionService;

    public ResidenteService(SolicitudResidenteRepository repository,
                            NotificacionService notificacionService) {
        this.repository = repository;
        this.notificacionService = notificacionService;
    }

    public SolicitudResidente crearSolicitud(String tenant, SolicitudDTO dto, String residenteUsername) {
        SolicitudResidente solicitud = new SolicitudResidente();
        solicitud.setTenant(tenant);
        solicitud.setResidenteUsername(residenteUsername);
        solicitud.setVisitante(dto.getVisitante());
        solicitud.setDestino(dto.getDestino());
        solicitud.setFechaHora(Instant.parse(dto.getFechaHora()));
        solicitud.setEstado("PENDIENTE");

        SolicitudResidente saved = repository.save(solicitud);

        // Notificar usando infraestructura existente
        String mensaje = "🔔 Nueva solicitud de acceso\n" +
                         "Residente: " + residenteUsername + "\n" +
                         "Visitante: " + dto.getVisitante() + "\n" +
                         "Destino: " + dto.getDestino() + "\n" +
                         "Fecha/Hora: " + dto.getFechaHora() + "\n" +
                         "Tenant: " + tenant;

        notificacionService.notificar(TipoEvento.SOLICITUD_RESIDENTE, mensaje);

        return saved;
    }

    public List<SolicitudResidente> obtenerHistorial(String tenant, String residenteUsername) {
        return repository.findByTenantAndResidenteUsernameOrderByFechaHoraDesc(tenant, residenteUsername);
    }
}
