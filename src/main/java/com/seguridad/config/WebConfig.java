/*package com.seguridad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 👇 expone la carpeta "uploads" como recurso estático
        // Los archivos dentro de uploads/registros serán accesibles en /uploads/registros/...
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
*/


package com.seguridad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Mantiene tu configuración actual para exponer /uploads/**
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    // Registra el filtro que captura el X-Tenant-ID
    @Bean
    public Filter tenantFilter() {
        return new TenantFilter();
    }
}
