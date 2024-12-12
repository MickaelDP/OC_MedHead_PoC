package com.medHead.poc.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Component
public class TokenController {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");

    private String csrfToken;

    // Injection via le constructeur pour éviter le cycle
    public TokenController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build(); // Utilise un RestTemplate standard
    }

    /**
     * Récupère un nouveau token CSRF avant chaque requête.
     */
    public String getCsrfToken() {
        try {
            // Appel au backend pour récupérer le token
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8443/test/csrf", String.class);

            // Log de la récupération du token sans afficher sa valeur
            logger.info(HTTP_MARKER, "Token CSRF récupéré avec succès, taille du token : {}", response.getBody() != null ? response.getBody().length() : "null");

            // Retourner la réponse brute du body comme token CSRF
            return response.getBody();
        } catch (Exception e) {
            // Loguer les erreurs pour mieux diagnostiquer les problèmes
            logger.error(HTTP_MARKER, "Erreur lors de la récupération du token CSRF : {}", e.getMessage());
            return null;
        }
    }

    /**
     * Ajoute le token CSRF à l'en-tête de la requête.
     *
     * @param headers Les en-têtes où le token doit être ajouté
     */
    public void addCsrfHeader(HttpHeaders headers) {
        String token = getCsrfToken();
        if (token != null) {
            headers.set("X-XSRF-TOKEN", token);
            logger.info(HTTP_MARKER, "Token CSRF ajouté à l'en-tête de la requête.");
        } else {
            logger.warn(HTTP_MARKER, "Impossible d'ajouter le token CSRF : token null.");
        }
    }
}
