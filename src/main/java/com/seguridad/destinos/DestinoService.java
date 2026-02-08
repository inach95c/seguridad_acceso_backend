package com.seguridad.destinos;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@Transactional
public class DestinoService {

    private final DestinoRepository destinoRepository;

    public DestinoService(DestinoRepository destinoRepository) {
        this.destinoRepository = destinoRepository;
    }

  /*  // Crear destino nuevo
    public Destino crearDestino(Destino destino) {
        return destinoRepository.save(destino);
    }*/

    // Crear destino nuevo con auditoría de usuario
    public Destino crearDestino(Destino destino) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "sistema";
        destino.setCreadoPor(username);   // 👈 nuevo campo en la entidad
        return destinoRepository.save(destino);
    }


    // Listar destinos activos
    public List<Destino> listarActivos() {
        return destinoRepository.findByActivoTrue();
    }

    // Desactivar destino
    public void desactivarDestino(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));
        destino.setActivo(false);
        destinoRepository.save(destino);
    }
    
 // Eliminar destino definitivamente
    public void eliminarDestino(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));
        destinoRepository.delete(destino);
    }

}
