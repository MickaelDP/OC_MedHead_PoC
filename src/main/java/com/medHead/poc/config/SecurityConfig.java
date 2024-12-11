package com.medHead.poc.config;

import com.medHead.poc.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructeur pour injecter le filtre d'authentification JWT.
     * @param jwtAuthenticationFilter le filtre JWT personnalisé pour gérer l'authentification.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configure la chaîne de filtres de sécurité pour l'application.
     *
     * @param http l'objet HttpSecurity utilisé pour configurer les options de sécurité.
     * @return SecurityFilterChain configuré.
     * @throws Exception en cas de problème avec la configuration de sécurité.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())           // Stockage dans un cookie accessible par JS
                )
                //.csrf(csrf -> csrf.disable())                                                       // Désactiver Csrf en théorie
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**", "/",  "/reserve", "/index.html",  "/test/csrf","/assets/**", "/auth/**", "/resources/**", "/error", "/styles-*.css", "/styles/**", "/polyfills-*.js", "/main-*.js", "/runtime-*.js", "/favicon.ico", "/scripts/**").permitAll() // Autoriser l'accès à l'endpoint CSRF
                        .requestMatchers("/api/**").hasRole("QUALIFIED_USER")               // Restrictions habituelles pour /api/**
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}