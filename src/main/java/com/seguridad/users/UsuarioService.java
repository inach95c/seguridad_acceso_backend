package com.seguridad.users;

import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import com.seguridad.registros.RegistroRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BitacoraRepository bitacoraRepository;
    private final NotificacionService notificacionService;
    private final RegistroRepository registroRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          BitacoraRepository bitacoraRepository,
                          NotificacionService notificacionService,
                          RegistroRepository registroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.bitacoraRepository = bitacoraRepository;
        this.notificacionService = notificacionService;
        this.registroRepository = registroRepository;
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // ============================================================
    // CREAR USUARIO
    // ============================================================
    public Usuario crearUsuario(Usuario usuario, String creadorUsername) {

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Usuario creador = usuarioRepository.findByUsername(creadorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Creador no encontrado"));

        String tenant = creador.getTenant();

        usuario.setCreadoPor(creadorUsername);
        usuario.setActualizadoPor(creadorUsername);
        usuario.setCreadoEn(Instant.now());
        usuario.setActualizadoEn(Instant.now());
        usuario.setActivo(true);
        usuario.setTenant(tenant);

        Usuario saved = usuarioRepository.save(usuario);

        // Bitácora
        bitacoraRepository.save(new Bitacora(
                "Usuario creado: " + saved.getUsername(),
                creador,
                tenant
        ));

        // Notificación
        String mensaje = "👤 *Usuario creado*\n" +
                "👤 Username: *" + saved.getUsername() + "*\n" +
                "🔑 Rol: *" + saved.getRol() + "*\n" +
                "👤 Creado por: *" + creadorUsername + "*";

        notificacionService.notificar(TipoEvento.USUARIO_CREADO, mensaje);

        return saved;
    }

    // ============================================================
    // ACTUALIZAR USUARIO
    // ============================================================
    public Usuario actualizarUsuario(Long id, UsuarioDTO dto, String actualizadorUsername) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Usuario actualizador = usuarioRepository.findByUsername(actualizadorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Actualizador no encontrado"));

        String tenant = actualizador.getTenant();

        usuario.setPasswordHash(dto.getPassword());
        usuario.setRol(dto.getRol());
        usuario.setActualizadoPor(actualizadorUsername);
        usuario.setActualizadoEn(Instant.now());

        Usuario updated = usuarioRepository.save(usuario);

        // Bitácora
        bitacoraRepository.save(new Bitacora(
                "Usuario actualizado: " + usuario.getUsername(),
                actualizador,
                tenant
        ));

        // Notificación
        String mensaje = "✏️ *Usuario actualizado*\n" +
                "👤 Username: *" + updated.getUsername() + "*\n" +
                "🔑 Nuevo rol: *" + updated.getRol() + "*\n" +
                "👤 Actualizado por: *" + actualizadorUsername + "*";

        notificacionService.notificar(TipoEvento.USUARIO_CREADO, mensaje);

        return updated;
    }

    // ============================================================
    // DESACTIVAR USUARIO
    // ============================================================
    public void desactivarUsuario(Long id, String actualizadorUsername) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Usuario actualizador = usuarioRepository.findByUsername(actualizadorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Actualizador no encontrado"));

        String tenant = actualizador.getTenant();

        usuario.setActivo(false);
        usuario.setActualizadoPor(actualizadorUsername);
        usuario.setActualizadoEn(Instant.now());

        usuarioRepository.save(usuario);

        // Bitácora
        bitacoraRepository.save(new Bitacora(
                "Usuario desactivado: " + usuario.getUsername(),
                actualizador,
                tenant
        ));

        // Notificación
        String mensaje = "🚫 *Usuario desactivado*\n" +
                "👤 Username: *" + usuario.getUsername() + "*\n" +
                "🔑 Rol: *" + usuario.getRol() + "*\n" +
                "👤 Desactivado por: *" + actualizadorUsername + "*";

        notificacionService.notificar(TipoEvento.USUARIO_DESACTIVADO, mensaje);
    }

    // ============================================================
    // ELIMINAR USUARIO (con validación de registros)
    // ============================================================
    public void eliminarUsuario(Long id, String actualizadorUsername) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Usuario actualizador = usuarioRepository.findByUsername(actualizadorUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actualizador no encontrado"));

        String tenant = actualizador.getTenant();

        // Validar si tiene registros asociados
        if (registroRepository.existsByUsuarioEntradaId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el usuario porque tiene registros asociados. Debe desactivarse."
            );
        }

        // Bitácora
        bitacoraRepository.save(new Bitacora(
                "Usuario eliminado: " + usuario.getUsername(),
                actualizador,
                tenant
        ));

        usuarioRepository.delete(usuario);

        // Notificación
        String mensaje = "🗑 *Usuario eliminado*\n" +
                "👤 Username: *" + usuario.getUsername() + "*\n" +
                "🔑 Rol: *" + usuario.getRol() + "*\n" +
                "👤 Eliminado por: *" + actualizadorUsername + "*";

        notificacionService.notificar(TipoEvento.USUARIO_ELIMINADO, mensaje);
    }

    // ============================================================
    // ELIMINAR BITÁCORA
    // ============================================================
    public void eliminarBitacora(Long usuarioId, String actualizadorUsername) {

        Usuario actualizador = usuarioRepository.findByUsername(actualizadorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Actualizador no encontrado"));

        String tenant = actualizador.getTenant();

        bitacoraRepository.deleteByUsuarioId(usuarioId);

        bitacoraRepository.save(new Bitacora(
                "Bitácora eliminada del usuario ID: " + usuarioId,
                actualizador,
                tenant
        ));

        String mensaje = "🗄 *Bitácora eliminada*\n" +
                "👤 Usuario ID: *" + usuarioId + "*\n" +
                "👤 Eliminada por: *" + actualizadorUsername + "*";

        notificacionService.notificar(TipoEvento.ALERTA_SEGURIDAD, mensaje);
    }

    // ============================================================
    // CAMBIAR ESTADO (activar/desactivar)
    // ============================================================
    public void cambiarEstado(Long id, Boolean activo, String actualizadorUsername) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Usuario actualizador = usuarioRepository.findByUsername(actualizadorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Actualizador no encontrado"));

        String tenant = actualizador.getTenant();

        // Reglas de negocio
        if (actualizador.getRol() == Usuario.Rol.GUARDIA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "GUARDIA no puede activar ni desactivar usuarios");
        }

        if (actualizador.getRol() == Usuario.Rol.GERENTE_ADMIN &&
            usuario.getRol() != Usuario.Rol.GUARDIA) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "GERENTE_ADMIN solo puede activar/desactivar usuarios con rol GUARDIA");
        }

        usuario.setActivo(activo);
        usuario.setActualizadoPor(actualizadorUsername);
        usuario.setActualizadoEn(Instant.now());

        usuarioRepository.save(usuario);

        // Bitácora
        bitacoraRepository.save(new Bitacora(
                (activo ? "Usuario activado: " : "Usuario desactivado: ") + usuario.getUsername(),
                actualizador,
                tenant
        ));

        // Notificación
        String mensaje = (activo ? "✅ *Usuario activado*\n" : "🚫 *Usuario desactivado*\n") +
                "👤 Username: *" + usuario.getUsername() + "*\n" +
                "🔑 Rol: *" + usuario.getRol() + "*\n" +
                "👤 Actualizado por: *" + actualizadorUsername + "*";

        notificacionService.notificar(
                activo ? TipoEvento.USUARIO_CREADO : TipoEvento.USUARIO_DESACTIVADO,
                mensaje
        );
    }

    // ============================================================
    // ACTUALIZACIÓN PARCIAL (Map)
    // ============================================================
    public void actualizarUsuario(Long id, Map<String, Object> body, String actualizadorUsername) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Usuario actualizador = usuarioRepository.findByUsername(actualizadorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Actualizador no encontrado"));

        String tenant = actualizador.getTenant();

        // Validación username duplicado
        if (body.containsKey("username")) {
            String nuevoUsername = body.get("username").toString();

            usuarioRepository.findByUsername(nuevoUsername).ifPresent(u -> {
                if (!u.getId().equals(usuario.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El username ya está en uso");
                }
            });

            usuario.setUsername(nuevoUsername);
        }

        // Actualización parcial
        if (body.containsKey("rol")) {
            usuario.setRol(Usuario.Rol.fromString(body.get("rol").toString()));
        }

        if (body.containsKey("activo")) {
            usuario.setActivo(Boolean.parseBoolean(body.get("activo").toString()));
        }

        usuario.setActualizadoPor(actualizadorUsername);
        usuario.setActualizadoEn(Instant.now());

        usuarioRepository.save(usuario);

        // Bitácora
        bitacoraRepository.save(new Bitacora(
                "Usuario editado: " + usuario.getUsername(),
                actualizador,
                tenant
        ));

        // Notificación
        String mensaje = "✏️ *Usuario editado*\n" +
                "👤 Username: *" + usuario.getUsername() + "*\n" +
                "🔑 Rol: *" + usuario.getRol() + "*\n" +
                "👤 Editado por: *" + actualizadorUsername + "*";

        notificacionService.notificar(TipoEvento.USUARIO_CREADO, mensaje);
    }

    // ============================================================
    // LISTAR USUARIOS ACTIVOS
    // ============================================================
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }
}
