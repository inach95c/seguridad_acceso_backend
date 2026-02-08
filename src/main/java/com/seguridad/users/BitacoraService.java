package com.seguridad.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;

    public BitacoraService(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    // Registrar acción en la bitácora
    public Bitacora registrarAccion(String descripcion, Usuario usuario) {
        Bitacora bitacora = new Bitacora();
        bitacora.setDescripcion(descripcion);
        bitacora.setFechaHora(Instant.now());
        bitacora.setUsuario(usuario);
        return bitacoraRepository.save(bitacora);
    }

    // Listar todas las bitácoras
    public List<Bitacora> listarBitacoras() {
        return bitacoraRepository.findAll();
    }

    // Eliminar todas las bitácoras de un usuario (solo MASTER_ADMIN)
    public void eliminarBitacorasPorUsuario(Long usuarioId) {
        bitacoraRepository.deleteByUsuarioId(usuarioId);
    }

    // Eliminar una bitácora específica (solo MASTER_ADMIN)
    public void eliminarBitacoraPorId(Long id) {
        bitacoraRepository.deleteById(id);
    }
}
