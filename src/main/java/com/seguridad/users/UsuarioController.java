

package com.seguridad.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService,
                             PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // CREAR USUARIO
    // ============================================================
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO dto,
                                          Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No autenticado");
        }

        // Rol del usuario autenticado
        String rolActual = authentication.getAuthorities()
                .iterator().next().getAuthority();

        if (rolActual.startsWith("ROLE_")) {
            rolActual = rolActual.substring(5);
        }

        // GERENTE_ADMIN solo puede crear GUARDIA
        if ("GERENTE_ADMIN".equals(rolActual)
                && dto.getRol() != Usuario.Rol.GUARDIA) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Un Gerente solo puede crear Guardias");
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("La contraseña es obligatoria");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setRol(dto.getRol());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        String creador = authentication.getName();

        Usuario nuevo = usuarioService.crearUsuario(usuario, creador);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ============================================================
    // LISTAR USUARIOS
    // ============================================================
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // ============================================================
    // BUSCAR POR USERNAME
    // ============================================================
    @GetMapping("/{username}")
    public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
        return usuarioService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // DESACTIVAR USUARIO
    // ============================================================
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id,
                                               Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No autenticado");
        }

        String actualizador = authentication.getName();

        usuarioService.desactivarUsuario(id, actualizador);

        return ResponseEntity.ok("Usuario desactivado por " + actualizador);
    }

 // ============================================================
 // ELIMINAR USUARIO
 // ============================================================
 @DeleteMapping("/{id}")
 public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id,
                                             Authentication authentication) {
     if (authentication == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     }

     String actualizador = authentication.getName();
     usuarioService.eliminarUsuario(id, actualizador);

     return ResponseEntity.noContent().build(); // ✅ 204 No Content
 }

 // ============================================================
 // ELIMINAR BITÁCORA DE UN USUARIO
 // ============================================================
 @DeleteMapping("/{id}/bitacora")
 public ResponseEntity<Void> eliminarBitacora(@PathVariable Long id,
                                              Authentication authentication) {
     if (authentication == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     }

     String actualizador = authentication.getName();
     usuarioService.eliminarBitacora(id, actualizador);

     return ResponseEntity.noContent().build(); // ✅ 204 No Content
 }

    
    
    // para desactivar y activar usuaarioList
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestBody Map<String, Boolean> body,
                                           Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No autenticado");
        }

        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest().body("Campo 'activo' requerido");
        }

        String actualizador = authentication.getName();

        usuarioService.cambiarEstado(id, activo, actualizador);

        return ResponseEntity.ok().build();
    }

    
 // =========================
 // ✏️ Editar/Actualizar usuario (FINAL)
 // =========================
 @PutMapping("/{id}")
 public ResponseEntity<?> actualizarUsuario(@PathVariable Long id,
                                            @RequestBody Map<String, Object> body,
                                            Authentication authentication) {

     if (authentication == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body("No autenticado");
     }

     String actualizador = authentication.getName();

     usuarioService.actualizarUsuario(id, body, actualizador);

     return ResponseEntity.ok().build();
 }

 
 
 @GetMapping("/id/{id}")
 public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
     return usuarioService.findById(id)
             .map(ResponseEntity::ok)
             .orElse(ResponseEntity.notFound().build());
 }



    
}












