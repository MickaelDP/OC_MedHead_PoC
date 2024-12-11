package com.medHead.poc.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour les tests afin de valider les fonctionnalités liées à la sécurité CSRF.
 * Cette classe fournit un endpoint permettant de récupérer le token CSRF
 * généré et géré par Spring Security.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * Endpoint pour récupérer le token CSRF.
     * Ce token est géré par Spring Security et est automatiquement stocké dans un cookie.
     * @param request l'objet {@link HttpServletRequest} permettant d'accéder aux attributs de la requête.
     * @return une réponse contenant le token CSRF en tant que chaîne de caractères.
     */
    @GetMapping("/csrf")
    public ResponseEntity<String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // Le token est maintenant disponible via csrfToken.getToken()
        // Inutile de remettre un cookie vous-même, Spring Security le fait déjà.
        return ResponseEntity.ok(csrfToken.getToken());
    }
}
