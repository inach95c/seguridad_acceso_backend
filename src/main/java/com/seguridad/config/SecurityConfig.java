package com.seguridad.config;

import com.seguridad.security.AuthUserDetailsService;
import com.seguridad.security.JwtAuthFilter;
import com.seguridad.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import com.seguridad.security.SecurityAlertFilter;


import java.util.List;

@Configuration
public class SecurityConfig {

    private final AuthUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthUserDetailsService uds, JwtUtil jwtUtil) {
        this.userDetailsService = uds;
        this.jwtUtil = jwtUtil;
    }
    
   
   /* @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userDetailsService);

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                
                .requestMatchers("/api/registros/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/registros/completados/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/bitacoras/**").hasAnyRole("MASTER_ADMIN")
                .requestMatchers("/api/reportes/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/reportes/permanencia/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/reportes/reincidencia/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/metricas/**").hasAnyAuthority("MASTER_ADMIN", "GERENTE_ADMIN")


                
                .requestMatchers("/api/usuarios/**").hasAnyRole("MASTER_ADMIN", "GERENTE_ADMIN")
                .requestMatchers("/api/destinos/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")
                .anyRequest().authenticated()
            ) 
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
*/
    
  /*  @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,SecurityAlertFilter securityAlertFilter) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userDetailsService);

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            		
            	
            		
                // 👇 permitir acceso público a imágenes 
                .requestMatchers("/uploads/**").permitAll()

                // login
                .requestMatchers("/api/auth/login").permitAll()

                // 👇 permitir acceso público al webhook de Telegram
                .requestMatchers("/api/telegram/webhook").permitAll()

                // Registros
                .requestMatchers("/api/registros/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/registros/completados/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")

                // Bitácoras
                .requestMatchers("/api/bitacoras/**").hasRole("MASTER_ADMIN")

                // Reportes generales
                .requestMatchers("/api/reportes/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/reportes/permanencia/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/reportes/reincidencia/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")

                // Métricas
                .requestMatchers("/api/metricas/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")

                // Usuarios
                .requestMatchers("/api/usuarios/**").hasAnyRole("MASTER_ADMIN", "GERENTE_ADMIN")

                // Destinos
                .requestMatchers("/api/destinos/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            //.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
         // 👇 PRIMERO: detectar intentos no autorizados
            .addFilterBefore(securityAlertFilter, UsernamePasswordAuthenticationFilter.class) 
            // 👇 DESPUÉS: validar JWT
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
*/
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SecurityAlertFilter securityAlertFilter) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userDetailsService);

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 👇 permitir acceso público a Angular y recursos estáticos
                .requestMatchers("/", "/inicio", "/index.html", "/favicon.ico",
                                 "/assets/**", "/static/**",
                                 "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.ico").permitAll()

                // 👇 permitir acceso público a imágenes
                .requestMatchers("/uploads/**").permitAll()

                // login
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/ping").permitAll()


                // 👇 permitir acceso público al webhook de Telegram
                .requestMatchers("/api/telegram/webhook").permitAll()

                // Rutas protegidas
                .requestMatchers("/api/registros/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/registros/completados/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")

                .requestMatchers("/api/bitacoras/**").hasRole("MASTER_ADMIN")

                .requestMatchers("/api/reportes/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/reportes/permanencia/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")
                .requestMatchers("/api/reportes/reincidencia/**").hasAnyRole("GERENTE_ADMIN", "MASTER_ADMIN")

                .requestMatchers("/api/metricas/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")

                .requestMatchers("/api/usuarios/**").hasAnyRole("MASTER_ADMIN", "GERENTE_ADMIN")

                .requestMatchers("/api/destinos/**").hasAnyRole("GUARDIA", "GERENTE_ADMIN", "MASTER_ADMIN")

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            // 👇 PRIMERO: detectar intentos no autorizados
            .addFilterBefore(securityAlertFilter, UsernamePasswordAuthenticationFilter.class)
            // 👇 DESPUÉS: validar JWT
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

   //Este es el original y el que hay que modificar en produccion
    /* @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
       // c.setAllowedOrigins(List.of("http://localhost:4200")); // ngx-admin en dev
        c.setAllowedOrigins(List.of( "http://localhost:4200",
        		"http://192.168.1.100:4200")); // 👈 ajusta con la IP real de tu PC 
        c.setAllowedMethods(List.of("GET","POST","PATCH","PUT","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*")); // 👈 permite todos los headers
        c.setAllowedHeaders(List.of("Authorization","Content-Type"));
        c.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }
    */
    
    //solo pruebas locales
  /*  @Bean   // ok sin tenals
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
     // 👇 Permitir tu frontend Angular en local y en producción
        c.setAllowedOrigins(List.of( 
        		"http://localhost:4200",
        		//"https://seguridad-acceso-backend.onrender.com"
        		 "https://seguridadaccesos.netlify.app",
        		 "https://seguridadaccesos01.netlify.app", // frontend cliente1
        		 "https://seguridadaccesos02.netlify.app", // frontend cliente2
        		 "https://seguridadaccesos03.netlify.app" // frontend cliente3
        		));
        // 👇 Permite cualquier origen (localhost, IP local, incluso dominios externos)  esto es para aceder de manera local de mi movil
       // c.addAllowedOriginPattern("*");

        // Métodos permitidos
        c.setAllowedMethods(List.of("GET","POST","PATCH","PUT","DELETE","OPTIONS"));

        // Headers permitidos
        c.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies, Authorization header)
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }*/
    
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();

        // 👇 Permitir Angular local y tus 3 frontends en Netlify
        c.setAllowedOriginPatterns(List.of(
            "http://localhost:4200",
            "https://seguridadaccesos.netlify.app",
            "https://seguridadaccesos01.netlify.app",
            "https://seguridadaccesos02.netlify.app",
            "https://seguridadaccesos03.netlify.app"
        ));

        // Métodos permitidos
        c.setAllowedMethods(List.of("GET","POST","PATCH","PUT","DELETE","OPTIONS"));

        // Headers permitidos
        c.setAllowedHeaders(List.of("*"));

        // Headers expuestos (para que Angular pueda leerlos)
        c.setExposedHeaders(List.of("Authorization", "X-Tenant-ID"));

        // Permitir credenciales
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }


    
    
}

