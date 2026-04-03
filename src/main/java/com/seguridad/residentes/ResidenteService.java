package com.seguridad.residentes;

import com.seguridad.residentes.dto.SolicitudDTO;
import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ResidenteService {

    private final SolicitudResidenteRepository repository;
    private final NotificacionService notificacionService;
    private final DestinoRepository destinoRepository;

    public ResidenteService(SolicitudResidenteRepository repository,
                            NotificacionService notificacionService,
                            DestinoRepository destinoRepository) {
        this.repository = repository;
        this.notificacionService = notificacionService;
        this.destinoRepository = destinoRepository;
    }

    public SolicitudResidente crearSolicitud(String tenant, SolicitudDTO dto, String residenteUsername) {
        // Buscar el destino por ID
        Destino destino = destinoRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        SolicitudResidente solicitud = new SolicitudResidente();
        solicitud.setTenant(tenant);
        solicitud.setResidenteUsername(residenteUsername);
        solicitud.setVisitante(dto.getVisitante());
        solicitud.setDestino(destino); // relación con la entidad Destino
        solicitud.setFechaHora(Instant.parse(dto.getFechaHora()));
        solicitud.setEstado("PENDIENTE");

        SolicitudResidente saved = repository.save(solicitud);

        // Notificar usando infraestructura existente
        String mensaje = "🔔 Nueva solicitud de acceso\n" +
                         "Residente: " + residenteUsername + "\n" +
                         "Visitante: " + dto.getVisitante() + "\n" +
                         "Destino: " + destino.getNombre() + "\n" +
                         "Fecha/Hora: " + dto.getFechaHora() + "\n" +
                         "Tenant: " + tenant;

        notificacionService.notificar(TipoEvento.SOLICITUD_RESIDENTE, mensaje);

        return saved;
    }

    public List<SolicitudResidente> obtenerHistorial(String tenant, String residenteUsername) {
        return repository.findByTenantAndResidenteUsernameOrderByFechaHoraDesc(tenant, residenteUsername);
    }
}
