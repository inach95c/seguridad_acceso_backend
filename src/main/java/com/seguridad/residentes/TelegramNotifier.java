package com.seguridad.residentes;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramNotifier {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String botToken = "TU_BOT_TOKEN"; // 👈 reemplaza con tu token real
    private final String chatId = "CHAT_ID_ADMIN";  // 👈 reemplaza con el chat/grupo

    public void sendAlert(String mensaje) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        Map<String, String> params = new HashMap<>();
        params.put("chat_id", chatId);
        params.put("text", mensaje);

        restTemplate.postForObject(url, params, String.class);
    }
}
