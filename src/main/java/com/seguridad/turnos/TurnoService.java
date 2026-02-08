package com.seguridad.turnos;

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
        return turnoRepository.save(turno);
    }

    public List<Turno> listarActivos() {
        return turnoRepository.findByActivoTrue();
    }
}
