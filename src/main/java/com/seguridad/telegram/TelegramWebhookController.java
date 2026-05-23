/*package com.seguridad.telegram;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.TelegramService;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {

    private final UsuarioRepository usuarioRepository;
    private final TelegramService telegramService;

    public TelegramWebhookController(UsuarioRepository usuarioRepository,
                                     TelegramService telegramService) {
        this.usuarioRepository = usuarioRepository;
        this.telegramService = telegramService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirUpdate(@RequestBody Map<String, Object> update) {
        System.out.println("✅ Mensaje recibido: " + update);

        Map<String, Object> message = (Map<String, Object>) update.get("message");
        if (message != null) {
            Map<String, Object> chat = (Map<String, Object>) message.get("chat");
            String chatId = String.valueOf(chat.get("id"));
            String usernameTelegram = (String) chat.get("username"); // opcional

            Usuario usuario = usuarioRepository.findByUsername(usernameTelegram).orElse(null);

            if (usuario != null) {
                usuario.setTelegramId(chatId);
                usuarioRepository.save(usuario);

                // ✅ Mensaje personalizado con nombre y rol
                String textoBienvenida = "👋 Hola *" + usuario.getUsername() + "*!\n\n" +
                        "✅ Tu cuenta ha sido vinculada correctamente.\n" +
                        "📌 Rol asignado: *" + usuario.getRol() + "*\n\n" +
                        "A partir de ahora recibirás notificaciones automáticas en este chat.";

                telegramService.sendMessage(chatId, textoBienvenida, "Markdown");

                System.out.println("✅ Telegram ID vinculado: " + chatId + " para usuario " + usuario.getUsername());
            } else {
                System.out.println("⚠️ Usuario no encontrado en BD: " + usernameTelegram);
            }
        }

        // 🔑 Siempre devolver 200 OK para que Telegram no marque error
        return ResponseEntity.ok("Update procesado correctamente");
    }
}
*/



package com.seguridad.telegram;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.TelegramService;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {

    private final UsuarioRepository usuarioRepository;
    private final TelegramService telegramService;
    private final TelegramCommandService telegramCommandService;

    public TelegramWebhookController(UsuarioRepository usuarioRepository,
                                     TelegramService telegramService,
                                     TelegramCommandService telegramCommandService) {
        this.usuarioRepository = usuarioRepository;
        this.telegramService = telegramService;
        this.telegramCommandService = telegramCommandService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirUpdate(@RequestBody Map<String, Object> update) {
        System.out.println("✅ Mensaje recibido: " + update);

        Map<String, Object> message = (Map<String, Object>) update.get("message");
        if (message != null) {
            Map<String, Object> chat = (Map<String, Object>) message.get("chat");
            String chatId = String.valueOf(chat.get("id"));
            String usernameTelegram = (String) chat.get("username"); // opcional
            String text = (String) message.get("text"); // 👈 texto del mensaje

            // 1️⃣ Vincular usuario si existe en BD
            Usuario usuario = null;
            if (usernameTelegram != null) {
                usuario = usuarioRepository.findByUsername(usernameTelegram).orElse(null);

                if (usuario != null) {
                    usuario.setTelegramId(chatId);
                    usuarioRepository.save(usuario);

                    String textoBienvenida = "👋 Hola *" + usuario.getUsername() + "*!\n\n" +
                            "✅ Tu cuenta ha sido vinculada correctamente.\n" +
                            "📌 Rol asignado: *" + usuario.getRol() + "*\n\n" +
                            "A partir de ahora recibirás notificaciones automáticas en este chat.";

                    telegramService.sendMessage(chatId, textoBienvenida, "Markdown");

                    System.out.println("✅ Telegram ID vinculado: " + chatId + " para usuario " + usuario.getUsername());
                } else {
                    System.out.println("⚠️ Usuario no encontrado en BD: " + usernameTelegram);
                }
            }

            // 2️⃣ Procesar comandos si hay texto
            if (text != null && text.startsWith("/")) {
                telegramCommandService.procesarComando(chatId, text, usuario);
            }
        }

        return ResponseEntity.ok("Update procesado correctamente");
    }
}
