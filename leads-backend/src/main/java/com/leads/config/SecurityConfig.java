package com.leads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Webhooks de Meta deben ser públicos
                .requestMatchers("/webhook/**").permitAll()
                // OAuth callback público
                .requestMatchers("/auth/**").permitAll()
                // WebSocket público (auth manejada por token en header)
                .requestMatchers("/ws/**").permitAll()
                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
