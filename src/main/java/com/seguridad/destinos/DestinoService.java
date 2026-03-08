package com.seguridad.destinos;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seguridad.config.TenantContext;

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

    // Crear destino nuevo con auditoría y tenant
    public Destino crearDestino(Destino destino) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "sistema";

        destino.setCreadoPor(username);                     // Auditoría
        destino.setTenant(TenantContext.getTenantId());     // Multi‑tenant

        return destinoRepository.save(destino);
    }

    // Listar destinos activos por tenant
    public List<Destino> listarActivos() {
        String tenantId = TenantContext.getTenantId();
        return destinoRepository.findByActivoTrueAndTenant(tenantId);
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
