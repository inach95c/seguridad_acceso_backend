package com.seguridad.turnos;

import com.seguridad.config.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class TurnoService {

    private final TurnoRepository turnoRepository;

    public TurnoService(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    public Turno crearTurno(Turno turno) {
        turno.setTenant(TenantContext.getTenantId()); // 👈 obligatorio
        return turnoRepository.save(turno);
    }

    public List<Turno> listarActivos() {
        String tenant = TenantContext.getTenantId();
        return turnoRepository.findByActivoTrueAndTenant(tenant);
    }

    public List<Turno> listarTodos() {
        String tenant = TenantContext.getTenantId();
        return turnoRepository.findByTenant(tenant);
    }
}
