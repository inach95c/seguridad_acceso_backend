/*package com.seguridad.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String chatId, String text) {
        String url = String.format(
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
            botToken, chatId, text
        );
        try {
            restTemplate.getForObject(url, String.class);
            System.out.println("Mensaje enviado a Telegram: " + text);
        } catch (Exception e) {
            System.err.println("Error enviando mensaje a Telegram: " + e.getMessage());
        }
    }
}
*/






///  ok ok

/*
package com.seguridad.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(String chatId, String text, String parseMode) {
        String url = String.format(
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s%s",
            botToken,
            chatId,
            text,
            (parseMode != null ? "&parse_mode=" + parseMode : "")
        );
        try {
            restTemplate.getForObject(url, String.class);
            System.out.println("✅ Mensaje enviado a Telegram: " + text);
        } catch (Exception e) {
            System.err.println("❌ Error enviando mensaje a Telegram: " + e.getMessage());
        }
    }
}

*/




package com.seguridad.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(String chatId, String text, String parseMode) {
        // ⚠️ Filtro: si el texto contiene NoResourceFoundException, no enviar
        if (text != null && text.contains("NoResourceFoundException")) {
            System.out.println("⚠️ Ignorado: NoResourceFoundException, no se envía a Telegram.");
            return;
        }

        String url = String.format(
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s%s",
            botToken,
            chatId,
            text,
            (parseMode != null ? "&parse_mode=" + parseMode : "")
        );
        try {
            restTemplate.getForObject(url, String.class);
            System.out.println("✅ Mensaje enviado a Telegram: " + text);
        } catch (Exception e) {
            System.err.println("❌ Error enviando mensaje a Telegram: " + e.getMessage());
        }
    }

    // Método opcional para enviar errores
    public void sendError(String chatId, Exception ex) {
        // ⚠️ Filtro: no enviar NoResourceFoundException
        if (ex instanceof NoResourceFoundException) {
            System.out.println("⚠️ Ignorado: NoResourceFoundException, no se envía a Telegram.");
            return;
        }

        String text = "💥 *Error grave en el sistema*\n" +
                      "📌 Tipo: " + ex.getClass().getSimpleName() + "\n" +
                      "📌 Mensaje: " + ex.getMessage();

        sendMessage(chatId, text, "Markdown");
    }
}

