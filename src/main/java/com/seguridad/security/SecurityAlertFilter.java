package com.seguridad.security;

import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityAlertFilter implements Filter {

    private final NotificacionService notificacionService;

    public SecurityAlertFilter(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {

            notificacionService.notificar(
                    TipoEvento.INTENTO_ACCESO_NO_AUTORIZADO,
                    "🚨 *Intento de acceso no autorizado*\n" +
                    "📌 Endpoint: " + req.getRequestURI() + "\n" +
                    "📌 Método: " + req.getMethod()
            );

            throw ex;
        }
    }
}
