 // com/seguridad/notificaciones/NotificacionService.java
package com.seguridad.notificaciones;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.TelegramService;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificacionService {

    private final UsuarioRepository usuarioRepository;
    private final TelegramService telegramService;

    public NotificacionService(UsuarioRepository usuarioRepository,
                               TelegramService telegramService) {
        this.usuarioRepository = usuarioRepository;
        this.telegramService = telegramService;
    }

    public void notificar(TipoEvento tipo, String mensaje) {
        // 1) Resolver destinatarios según tipo
        Set<Usuario.Rol> rolesDestino = resolverRolesDestino(tipo);

        // 2) Buscar usuarios con esos roles y telegramId no nulo
        List<Usuario> destinatarios = usuarioRepository.findByRolInAndTelegramIdIsNotNull(rolesDestino);

        // 3) Enviar mensaje a cada uno
        for (Usuario u : destinatarios) {
            telegramService.sendMessage(u.getTelegramId(), mensaje, "Markdown");
        }
    }

    private Set<Usuario.Rol> resolverRolesDestino(TipoEvento tipo) {
        switch (tipo) {
            // Guardias → operativos
            case REGISTRO_INGRESO:
            case REGISTRO_SALIDA:
            case REGISTRO_ELIMINADO:
            case ERROR_FOTO:
            case SOSPECHOSO_DETECTADO:
                return EnumSet.of(Usuario.Rol.GUARDIA, Usuario.Rol.GERENTE_ADMIN, Usuario.Rol.MASTER_ADMIN);

            // Admins → administrativos y críticos
            case USUARIO_CREADO:
            case USUARIO_DESACTIVADO:
            case FALLO_SISTEMA:
            case ALERTA_SEGURIDAD:
                return EnumSet.of(Usuario.Rol.GERENTE_ADMIN, Usuario.Rol.MASTER_ADMIN);

            // Master → críticos duros
            case ERROR_GRAVE:
            case INTENTO_ACCESO_NO_AUTORIZADO:
                return EnumSet.of(Usuario.Rol.MASTER_ADMIN);

            default:
                return EnumSet.of(Usuario.Rol.MASTER_ADMIN);
        }
    }
}


/*
package com.seguridad.notificaciones;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.TelegramService;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificacionService {

    private final UsuarioRepository usuarioRepository;
    private final TelegramService telegramService;

    public NotificacionService(UsuarioRepository usuarioRepository,
                               TelegramService telegramService) {
        this.usuarioRepository = usuarioRepository;
        this.telegramService = telegramService;
    }

    public void notificar(TipoEvento tipo, String mensaje) {
        Set<Usuario.Rol> rolesDestino = resolverRolesDestino(tipo);

        List<Usuario> destinatarios =
                usuarioRepository.findByRolInAndTelegramIdIsNotNull(rolesDestino);

        for (Usuario u : destinatarios) {
            try {
                telegramService.sendMessage(u.getTelegramId(), mensaje, "HTML");
            } catch (Exception e) {
                // ⚠️ IMPORTANTE: NO ROMPER EL REGISTRO
                System.err.println("⚠️ Error enviando notificación a Telegram, pero el sistema continúa: " + e.getMessage());
            }
        }
    }

    private Set<Usuario.Rol> resolverRolesDestino(TipoEvento tipo) {
        switch (tipo) {
            case REGISTRO_INGRESO:
            case REGISTRO_SALIDA:
            case REGISTRO_ELIMINADO:
            case ERROR_FOTO:
            case SOSPECHOSO_DETECTADO:
                return EnumSet.of(Usuario.Rol.GUARDIA, Usuario.Rol.GERENTE_ADMIN, Usuario.Rol.MASTER_ADMIN);

            case USUARIO_CREADO:
            case USUARIO_DESACTIVADO:
            case FALLO_SISTEMA:
            case ALERTA_SEGURIDAD:
                return EnumSet.of(Usuario.Rol.GERENTE_ADMIN, Usuario.Rol.MASTER_ADMIN);

            case ERROR_GRAVE:
            case INTENTO_ACCESO_NO_AUTORIZADO:
                return EnumSet.of(Usuario.Rol.MASTER_ADMIN);

            default:
                return EnumSet.of(Usuario.Rol.MASTER_ADMIN);
        }
    }
}
*/
