package com.seguridad.residentes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudResidenteRepository extends JpaRepository<SolicitudResidente, Long> {

    // ✅ Historial ordenado por fecha de creación (más reciente primero)
    List<SolicitudResidente> findByTenantAndResidenteUsernameOrderByCreadoEnDesc(
            String tenant,
            String residenteUsername
    );
}
