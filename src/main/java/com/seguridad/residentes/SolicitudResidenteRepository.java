package com.seguridad.residentes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudResidenteRepository extends JpaRepository<SolicitudResidente, Long> {
    List<SolicitudResidente> findByTenantAndResidenteUsernameOrderByFechaHoraDesc(
        String tenant, String residenteUsername
    );
}
