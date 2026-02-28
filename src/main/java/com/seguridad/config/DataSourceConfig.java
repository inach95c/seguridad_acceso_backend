package com.seguridad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    // Base de datos por defecto (neondb)
    @Value("${SPRING_DATASOURCE_URL}")
    private String defaultUrl;

    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String password;

    // Bases de datos por cliente
    @Value("${SPRING_DATASOURCE_URL_CLIENTE1}")
    private String cliente1Url;

    @Value("${SPRING_DATASOURCE_URL_CLIENTE2}")
    private String cliente2Url;

    @Value("${SPRING_DATASOURCE_URL_CLIENTE3}")
    private String cliente3Url;

    @Bean
    public DataSource dataSource() {

        // Crear DataSource por cada base
        DriverManagerDataSource defaultDs = createDataSource(defaultUrl);
        DriverManagerDataSource cliente1Ds = createDataSource(cliente1Url);
        DriverManagerDataSource cliente2Ds = createDataSource(cliente2Url);
        DriverManagerDataSource cliente3Ds = createDataSource(cliente3Url);

        // Registrar todos los DataSources
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("default", defaultDs);
        dataSources.put("cliente1", cliente1Ds);
        dataSources.put("cliente2", cliente2Ds);
        dataSources.put("cliente3", cliente3Ds);

        // Configurar el enrutador
        MultiTenantRoutingDataSource routingDataSource = new MultiTenantRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(defaultDs);
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    private DriverManagerDataSource createDataSource(String url) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }
}

