package com.seguridad.destinos;

import com.seguridad.config.TenantContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DestinoService {

    private final DestinoRepository destinoRepository;

    public DestinoService(DestinoRepository destinoRepository) {
        this.destinoRepository = destinoRepository;
    }

    // ============================================================
    // ✅ Crear destino con auditoría y multi‑tenant
    // ============================================================
    public Destino crearDestino(Destino destino) {

        // Usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "sistema";

        // Auditoría
        destino.setCreadoPor(username);

        // Multi‑tenant
        destino.setTenant(TenantContext.getTenantId());

        return destinoRepository.save(destino);
    }

    // ============================================================
    // ✅ Listar destinos activos del tenant actual
    // ============================================================
    public List<Destino> listarActivos() {
        String tenantId = TenantContext.getTenantId();
        return destinoRepository.findByActivoTrueAndTenant(tenantId);
    }

    // ============================================================
    // ❗ OPCIONAL: Listar destinos activos por tenant explícito
    // ============================================================
    public List<Destino> listarActivosPorTenant(String tenant) {
        return destinoRepository.findByActivoTrueAndTenant(tenant);
    }

    // ============================================================
    // ✅ Desactivar destino
    // ============================================================
    public void desactivarDestino(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));
        destino.setActivo(false);
        destinoRepository.save(destino);
    }

    // ============================================================
    // ✅ Eliminar destino
    // ============================================================
    public void eliminarDestino(Long id) {
        destinoRepository.deleteById(id);
    }
}
