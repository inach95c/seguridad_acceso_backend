/*package com.seguridad.registros;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.security.Principal;


@RestController
@RequestMapping("/api/registros")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // Registrar ingreso con fotos
    @PostMapping("/ingreso")
    public ResponseEntity<Registro> registrarIngreso(
    		
           
            @RequestParam("destinoId") Long destinoId,
            @RequestParam(value = "sospechoso", defaultValue = "false") boolean sospechoso,
            @RequestParam("placa") String placa,                     // 👈 nuevo parámetro
            @RequestParam("placaFoto") MultipartFile placaFoto,
            @RequestParam("licenciaFoto") MultipartFile licenciaFoto) {
    	

        Registro nuevo = registroService.registrarIngresoConFotos(destinoId, sospechoso, placa, placaFoto, licenciaFoto);
        return ResponseEntity.ok(nuevo);
    }


    // Registrar salida
    @PostMapping("/salida")
    public ResponseEntity<Registro> registrarSalida(@RequestParam String folio) {
        Registro actualizado = registroService.registrarSalida(folio);
        return ResponseEntity.ok(actualizado);
    }
    
    
//--------------------------------------

//----------------------------------------

    

    // Reporte por rango de fechas
    @GetMapping("/reporte")
    public ResponseEntity<List<Registro>> obtenerRegistrosPorFecha(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return ResponseEntity.ok(registroService.obtenerRegistrosPorFecha(inicio, fin));
    }

    // Registros abiertos
    @GetMapping("/abiertos")
    public ResponseEntity<List<Registro>> obtenerRegistrosAbiertos() {
        return ResponseEntity.ok(registroService.obtenerRegistrosAbiertos());
    }
    
    // REGISTROS COMPLETADOS
    
        @GetMapping("/completados")
        public ResponseEntity<List<Registro>> obtenerRegistrosCompletados() {
            return ResponseEntity.ok(registroService.obtenerRegistrosCompletados());
        }
      
    
}

*/


package com.seguridad.registros;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;

@RestController
@RequestMapping("/api/registros")
public class RegistroController {

    private final RegistroService registroService;
    private final UsuarioRepository usuarioRepository;

    public RegistroController(RegistroService registroService, UsuarioRepository usuarioRepository) {
        this.registroService = registroService;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // Registrar ingreso con fotos
    // =========================
    @PostMapping("/ingreso")
    public ResponseEntity<Registro> registrarIngreso(
            @RequestParam("destinoId") Long destinoId,
            @RequestParam(value = "sospechoso", defaultValue = "false") boolean sospechoso,
            @RequestParam("placa") String placa,
            @RequestParam("placaFoto") MultipartFile placaFoto,
            @RequestParam("licenciaFoto") MultipartFile licenciaFoto,
            @RequestParam(value = "tipoVisitante", required = false) Registro.TipoVisitante tipoVisitante,
            Principal principal) {

        String usernameActual = principal.getName();
        Registro nuevo = registroService.registrarIngresoConFotos(
                destinoId,
                sospechoso,
                placa,
                placaFoto,
                licenciaFoto,
                tipoVisitante,
                usernameActual
        );
        return ResponseEntity.ok(nuevo);
    }

    // =========================
    // Registrar salida
    // =========================
    @PostMapping("/salida")
    public ResponseEntity<Registro> registrarSalida(@RequestParam String folio, Principal principal) {
        String usernameActual = principal.getName();
        Registro actualizado = registroService.registrarSalida(folio, usernameActual);
        return ResponseEntity.ok(actualizado);
    }

    // =========================
    // Reporte por rango de fechas
    // =========================
    @GetMapping("/reporte")
    public ResponseEntity<List<Registro>> obtenerRegistrosPorFecha(
            @RequestParam Instant inicio,
            @RequestParam Instant fin) {
        return ResponseEntity.ok(registroService.obtenerRegistrosPorFecha(inicio, fin));
    }

    // =========================
    // Registros abiertos
    // =========================
    @GetMapping("/abiertos")
    public ResponseEntity<List<Registro>> obtenerRegistrosAbiertos() {
        return ResponseEntity.ok(registroService.obtenerRegistrosAbiertos());
    }

    // =========================
    // Registros completados
    // =========================
    @GetMapping("/completados")
    public ResponseEntity<List<Registro>> obtenerRegistrosCompletados() {
        return ResponseEntity.ok(registroService.obtenerRegistrosCompletados());
    }
    
 // =========================
 // Eliminar registro por ID
 // =========================
 @DeleteMapping("/{id}")
 public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id, Principal principal) {
     String usernameActual = principal.getName();
     registroService.eliminarRegistro(id, usernameActual);
     return ResponseEntity.noContent().build();
 }
 
 //PARA TELEGRAM    

     // =========================
     // Actualizar telegramId del usuario actual
     // =========================
     @PatchMapping("/telegram")
     public ResponseEntity<Usuario> actualizarTelegramId(@RequestParam String telegramId, Principal principal) {
         String usernameActual = principal.getName();

         Usuario usuario = usuarioRepository.findByUsername(usernameActual)
                 .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

         usuario.setTelegramId(telegramId);
         usuarioRepository.save(usuario);

         return ResponseEntity.ok(usuario);
     }
 


}
