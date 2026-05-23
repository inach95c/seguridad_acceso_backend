/*package com.seguridad.security;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (Boolean.FALSE.equals(u.getActivo())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        // Spring Security espera roles con prefijo "ROLE_"
        String roleName = "ROLE_" + u.getRol().name();
        System.out.println("Asignando autoridad: " + roleName); // 👈 log para ver el rol
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        return new User(u.getUsername(), u.getPasswordHash(), authorities);
    }
}*/


package com.seguridad.security;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (Boolean.FALSE.equals(u.getActivo())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        // Spring Security espera roles con prefijo "ROLE_"
        String roleName = "ROLE_" + u.getRol().name();
        System.out.printf("Asignando autoridad: %s | Usuario: %s | Tenant: %s%n",
                roleName, u.getUsername(), u.getTenant());

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        // Devuelve el usuario con sus credenciales y autoridades
        return new User(u.getUsername(), u.getPasswordHash(), authorities);
    }
}

