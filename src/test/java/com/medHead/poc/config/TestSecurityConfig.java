package com.medHead.poc.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.crypto.SecretKey;

/**
 * Configuration de sécurité pour les tests.
 * Fournit une configuration simplifiée et adaptée aux tests, incluant une clé JWT.
 */
@TestConfiguration
public class TestSecurityConfig {

    /**
     * Fournit un service d'utilisateur pour les tests.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("test-user")
                .password(passwordEncoder().encode("password"))
                .roles("QUALIFIED_USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Fournit un encodeur de mots de passe pour les tests.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit une clé secrète JWT sécurisée pour les tests.
     * Cette clé est utilisée pour signer et vérifier les tokens JWT dans le contexte de test.
     * @return une instance de SecretKey sécurisée pour HS256
     */
    @Bean
    public SecretKey testJwtSecretKey() {
        return Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes());
    }
}