package com.seguridad.config;

import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final NotificacionService notificacionService;

    public GlobalExceptionHandler(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {

        notificacionService.notificar(
                TipoEvento.FALLO_SISTEMA,
                "⚠️ *Error de validación*\n" +
                "📌 Detalle: " + ex.getMessage()
        );

        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // ✅ NUEVO — Manejar 409, 404, 403… con notificación a Telegram
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatus(ResponseStatusException ex) {

        notificacionService.notificar(
                TipoEvento.FALLO_SISTEMA,
                "⚠️ *Evento controlado*\n" +
                "📌 Código: " + ex.getStatusCode().value() + "\n" +
                "📌 Mensaje: " + ex.getReason()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {

        notificacionService.notificar(
                TipoEvento.ERROR_GRAVE,
                "💥 *Error grave en el sistema*\n" +
                "📌 Tipo: " + ex.getClass().getSimpleName() + "\n" +
                "📌 Mensaje: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
    }
}
