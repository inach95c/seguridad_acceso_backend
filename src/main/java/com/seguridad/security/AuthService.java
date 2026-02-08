package com.seguridad.security;

import com.seguridad.security.dto.LoginRequest;
import com.seguridad.security.dto.LoginResponse;
import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager am, JwtUtil jwtUtil,
                       UsuarioRepository usuarioRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.authManager = am;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest req, long expiresInSeconds) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        Usuario u = usuarioRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

         String token = jwtUtil.generateToken(u.getUsername(), u.getRol().name());
       

        return new LoginResponse(token, expiresInSeconds, u.getRol().name(), u.getUsername());
    }

    // Utilidad para crear usuarios con password encriptado (si la necesitas)
    public Usuario createUserSecure(Usuario u) {
        u.setPasswordHash(passwordEncoder.encode(u.getPasswordHash()));
        return usuarioRepository.save(u);
    }
}
