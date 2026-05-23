


package com.seguridad.residentes;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

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
    // ✅ Crear solicitud de acceso (versión completa)
    // ============================================================
    public SolicitudResidente crearSolicitud(String tenant, SolicitudDTO dto, String username) {

        Destino destino = destinoRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        SolicitudResidente solicitud = new SolicitudResidente();
        solicitud.setTenant(tenant);
        solicitud.setResidenteUsername(username);
        solicitud.setVisitante(dto.getVisitante());
        solicitud.setDestino(destino);

        // Conversión robusta de fecha/hora
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .optionalStart()
                .appendOffsetId()
                .optionalEnd()
                .toFormatter();

        LocalDateTime ldt = LocalDateTime.parse(dto.getFechaHora(), formatter);
        Instant fechaHoraUTC = ldt.atZone(ZoneId.of("UTC")).toInstant();
        solicitud.setFechaHora(fechaHoraUTC);

        solicitud.setEstado("PENDIENTE");

        // ============================================================
        // 🔥 NUEVOS CAMPOS “nivel CondominioFeliz”
        // ============================================================
        solicitud.setTipoVisitante(dto.getTipoVisitante());
        solicitud.setMotivoVisita(dto.getMotivoVisita());
        solicitud.setPlacaVehiculo(dto.getPlacaVehiculo());
        solicitud.setDniVisitante(dto.getDniVisitante());
        solicitud.setNumeroAcompanantes(dto.getNumeroAcompanantes());

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
    
 // ============================================================
 // 🔍 Buscar último registro de un visitante (para autocompletar)
 // ============================================================
 public Optional<SolicitudResidente> buscarUltimoRegistro(String visitante, String tenant) {
     return solicitudRepository.findTopByVisitanteAndTenantOrderByFechaHoraDesc(visitante, tenant);
 }

 
//============================================================
//🔴 Cancelar solicitud (solo residente dueño y si está PENDIENTE)
//============================================================
public void cancelarSolicitud(String tenant, String username, Long id) {
  SolicitudResidente solicitud = solicitudRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

  if (!solicitud.getTenant().equals(tenant)) {
      throw new RuntimeException("No autorizado: tenant no coincide");
  }

  if (!solicitud.getResidenteUsername().equals(username)) {
      throw new RuntimeException("No autorizado: la solicitud no pertenece a este residente");
  }

  if (!"PENDIENTE".equals(solicitud.getEstado())) {
      throw new RuntimeException("Solo se pueden cancelar solicitudes en estado PENDIENTE");
  }

  solicitud.setEstado("CANCELADA");
  solicitudRepository.save(solicitud);
}




}

