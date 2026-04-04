/*package com.seguridad.destinos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    private final DestinoService destinoService;

    public DestinoController(DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    // Crear destino
    @PostMapping
    public ResponseEntity<Destino> crearDestino(@RequestBody Destino destino) {
        Destino nuevo = destinoService.crearDestino(destino);
        return ResponseEntity.ok(nuevo);
    }

    // Listar destinos activos
    @GetMapping("/activos")
    public ResponseEntity<List<Destino>> listarActivos() {
        return ResponseEntity.ok(destinoService.listarActivos());
    }

    // Desactivar destino
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarDestino(@PathVariable Long id) {
        destinoService.desactivarDestino(id);
        return ResponseEntity.noContent().build();
    }
    
 // Eliminar destino
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDestino(@PathVariable Long id) {
        destinoService.eliminarDestino(id);
        return ResponseEntity.noContent().build();
    }

}
*/

package com.seguridad.destinos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    private final DestinoService destinoService;

    public DestinoController(DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    // ✅ Crear destino
    @PostMapping
    public ResponseEntity<Destino> crearDestino(@RequestBody Destino destino) {
        Destino nuevo = destinoService.crearDestino(destino);
        return ResponseEntity.ok(nuevo);
    }

    // ✅ Listar todos los destinos activos (sin tenant)
    @GetMapping("/activos")
    public ResponseEntity<List<Destino>> listarActivos() {
        return ResponseEntity.ok(destinoService.listarActivos());
    }

    // ✅ Listar destinos activos filtrados por tenant
    @GetMapping("/{tenant}/activos")
    public ResponseEntity<List<Destino>> listarActivosPorTenant(@PathVariable String tenant) {
        return ResponseEntity.ok(destinoService.listarActivosPorTenant(tenant));
    }

    // ✅ Desactivar destino
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarDestino(@PathVariable Long id) {
        destinoService.desactivarDestino(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Eliminar destino
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDestino(@PathVariable Long id) {
        destinoService.eliminarDestino(id);
        return ResponseEntity.noContent().build();
    }
}

