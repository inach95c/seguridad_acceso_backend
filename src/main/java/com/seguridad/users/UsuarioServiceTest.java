/*package com.seguridad.users;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final UsuarioService usuarioService = new UsuarioService(usuarioRepository);

    @Test
    void crearUsuario_exitoso() {
        Usuario usuario = new Usuario();
        usuario.setUsername("guardia1");
        usuario.setPasswordHash("hash");
        usuario.setRol(Usuario.Rol.GUARDIA);

        when(usuarioRepository.existsByUsername("guardia1")).thenReturn(false);
        when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario creado = usuarioService.crearUsuario(usuario, "master1");

        assertEquals("guardia1", creado.getUsername());
        assertEquals("master1", creado.getCreadoPor());
        assertNotNull(creado.getCreadoEn());
    }

    @Test
    void crearUsuario_usernameDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setUsername("guardia1");

        when(usuarioRepository.existsByUsername("guardia1")).thenReturn(true);

        assertThrows(RuntimeException.class,
            () -> usuarioService.crearUsuario(usuario, "master1"));
    }
}*/
