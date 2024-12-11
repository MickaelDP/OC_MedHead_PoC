package com.medHead.poc.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenController {

    private final RestTemplate restTemplate;

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
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/test/csrf", String.class);

            // Retourner la réponse brute du body comme token CSRF
            return response.getBody();
        } catch (Exception e) {
            // Loguer les erreurs pour mieux diagnostiquer les problèmes
            System.err.println("Erreur lors de la récupération du token CSRF : " + e.getMessage());
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
        } else {
            System.err.println("Impossible d'ajouter le token CSRF : token null.");
        }
    }
}
