package com.seguridad.security;

import com.seguridad.security.dto.LoginRequest;
import com.seguridad.security.dto.LoginResponse;
import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @Value("${security.jwt.expiration-seconds}")
    private long expiresInSeconds;

    public AuthController(AuthService authService,UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse resp = authService.login(req, expiresInSeconds);
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        u.setActivo(false);
        usuarioRepository.save(u);

        return ResponseEntity.ok().build();
    }


}
