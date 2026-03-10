package com.nairbdev.academiasbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Dev (React/Vite/Angular). En prod pondrás tu dominio real.
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:4200"
        ));

        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"
        ));

        // Para Auth0 con JWT en Authorization header NO es obligatorio,
        // pero no estorba. Si luego usas cookies/sesión, sí lo necesitas.
        config.setAllowCredentials(true);

        // headers que tu front puede leer (si los usas)
        config.setExposedHeaders(List.of("Location"));

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {})               // <- CLAVE
                .csrf(csrf -> csrf.disable())   // API stateless típico
                .authorizeHttpRequests(auth -> auth
                        // preflight siempre permitido
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        // mientras no implementas auth:
                        .anyRequest().permitAll()
                )
                .build();
    }
}