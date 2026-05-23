package com.seguridad.telegram;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seguridad.users.Bitacora;
import com.seguridad.users.BitacoraRepository;
import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.TelegramService;

@Service
public class TelegramCommandService {

    private final TelegramService telegramService;
    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    public TelegramCommandService(TelegramService telegramService,
                                  BitacoraRepository bitacoraRepository,
                                  UsuarioRepository usuarioRepository) {
        this.telegramService = telegramService;
        this.bitacoraRepository = bitacoraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void procesarComando(String chatId, String text, Usuario usuario) {
        String comando = text.trim().split("\\s+")[0]; // solo la primera palabra

        switch (comando) {
            case "/ayuda":
                manejarAyuda(chatId, usuario);
                break;

            case "/estado":
                manejarEstado(chatId);
                break;

            case "/ultimos":
                manejarUltimos(chatId);
                break;

            case "/misnotificaciones":
                manejarMisNotificaciones(chatId, usuario);
                break;

            default:
                telegramService.sendMessage(chatId,
                        "❓ Comando no reconocido.\nEscribe /ayuda para ver las opciones disponibles.",
                        "Markdown");
        }
    }

    private void manejarAyuda(String chatId, Usuario usuario) {
        String nombre = (usuario != null ? usuario.getUsername() : "usuario");
        String rol = (usuario != null && usuario.getRol() != null
                ? usuario.getRol().toString()
                : "N/D");


        String mensaje = "📘 *Ayuda del sistema de Seguridad-Acceso*\n\n" +
                "👤 Usuario: *" + nombre + "*\n" +
                "🔐 Rol: *" + rol + "*\n\n" +
                "Comandos disponibles:\n" +
                "• */ayuda* → Muestra este menú de ayuda\n" +
                "• */estado* → Estado general del sistema\n" +
                "• */ultimos* → Últimos registros de actividad\n" +
                "• */misnotificaciones* → Información sobre tus notificaciones\n";

        telegramService.sendMessage(chatId, mensaje, "Markdown");
    }

    private void manejarEstado(String chatId) {
        try {
            long totalBitacoras = bitacoraRepository.count();
            long totalUsuarios = usuarioRepository.count();

            String mensaje = "📊 *Estado del sistema*\n\n" +
                    "• Registros en bitácora: *" + totalBitacoras + "*\n" +
                    "• Usuarios registrados: *" + totalUsuarios + "*\n" +
                    "• Estado general: ✅ Operativo\n";

            telegramService.sendMessage(chatId, mensaje, "Markdown");
        } catch (Exception e) {
            telegramService.sendMessage(chatId,
                    "💥 Ocurrió un error al consultar el estado del sistema.",
                    "Markdown");
        }
    }

    private void manejarUltimos(String chatId) {
        List<Bitacora> ultimos = bitacoraRepository.findTop5ByOrderByFechaHoraDesc();

        if (ultimos.isEmpty()) {
            telegramService.sendMessage(chatId,
                    "📄 *Últimos registros*\n\nNo hay registros en la bitácora.",
                    "Markdown");
            return;
        }

        StringBuilder sb = new StringBuilder("📄 *Últimos 5 registros*\n\n");

        for (Bitacora b : ultimos) {
            sb.append("• ")
              .append(b.getDescripcion())
              .append("\n   👤 Usuario: *")
              .append(b.getUsuario() != null ? b.getUsuario().getUsername() : "Sistema")
              .append("*\n   🕒 ")
              .append(b.getFechaHora())
              .append("\n\n");
        }

        telegramService.sendMessage(chatId, sb.toString(), "Markdown");
    }

    private void manejarMisNotificaciones(String chatId, Usuario usuario) {
        if (usuario == null) {
            telegramService.sendMessage(chatId,
                    "⚠️ No pude asociar este chat con un usuario del sistema.\n" +
                    "Asegúrate de que tu *username de Telegram* coincida con tu *usuario del sistema*.",
                    "Markdown");
            return;
        }

        String mensaje = "🔔 *Mis notificaciones*\n\n" +
                "👤 Usuario: *" + usuario.getUsername() + "*\n" +
                "🔐 Rol: *" + usuario.getRol() + "*\n\n" +
                "Actualmente recibirás:\n" +
                "• Notificaciones de eventos importantes del sistema\n" +
                "• Mensajes administrativos según tu rol\n\n" +
                "Si deseas cambiar esta configuración, contacta al administrador del sistema.";

        telegramService.sendMessage(chatId, mensaje, "Markdown");
    }
}
