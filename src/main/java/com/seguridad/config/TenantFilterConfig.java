package com.seguridad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class TenantFilterConfig {

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration() {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new TenantFilter());
        registration.addUrlPatterns("/*"); // Se aplica a todas las rutas
        registration.setOrder(1); // Se ejecuta antes de Spring Security

        return registration;
    }
}
