package com.seguridad.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // Leer el header X-Tenant-ID
            String tenantId = httpRequest.getHeader(TENANT_HEADER);

            // Si no viene, puedes definir un tenant por defecto
            if (tenantId == null || tenantId.isBlank()) {
                tenantId = "default"; // luego lo ajustamos si quieres
            }

            // Guardar el tenant en el contexto
            TenantContext.setTenantId(tenantId);

            chain.doFilter(request, response);

        } finally {
            // Limpiar después del request
            TenantContext.clear();
        }
    }
}
