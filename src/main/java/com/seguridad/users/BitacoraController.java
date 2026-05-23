/*package com.seguridad.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    // Listar todas las bitácoras (visible para GERENTE_ADMIN y MASTER_ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE_ADMIN','MASTER_ADMIN')")
    public ResponseEntity<List<Bitacora>> listarBitacoras() {
        return ResponseEntity.ok(bitacoraService.listarBitacoras());
    }

    // Eliminar todas las bitácoras de un usuario (solo MASTER_ADMIN)
    @DeleteMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    public ResponseEntity<Void> eliminarBitacorasPorUsuario(@PathVariable Long usuarioId) {
        bitacoraService.eliminarBitacorasPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }

    // Eliminar una bitácora específica (solo MASTER_ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    public ResponseEntity<Void> eliminarBitacoraPorId(@PathVariable Long id) {
        bitacoraService.eliminarBitacoraPorId(id);
        return ResponseEntity.noContent().build();
    }
}
*/

package com.seguridad.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE_ADMIN','MASTER_ADMIN')")
    public ResponseEntity<List<Bitacora>> listarBitacoras() {
        return ResponseEntity.ok(bitacoraService.listarBitacoras());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    public ResponseEntity<Void> eliminarBitacoraPorId(@PathVariable Long id) {
        bitacoraService.eliminarBitacoraPorId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    public ResponseEntity<Void> eliminarBitacorasPorUsuario(@PathVariable Long usuarioId) {
        bitacoraService.eliminarBitacorasPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
