package com.seguridad.residentes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SolicitudResidenteRepository extends JpaRepository<SolicitudResidente, Long> {

    // ✅ Historial ordenado por fecha de creación (más reciente primero)
    List<SolicitudResidente> findByTenantAndResidenteUsernameOrderByCreadoEnDesc(
            String tenant,
            String residenteUsername
    );
    
    Optional<SolicitudResidente> findTopByVisitanteAndTenantOrderByFechaHoraDesc(String visitante, String tenant);

    
    Optional<SolicitudResidente> findById(Long id);


}
