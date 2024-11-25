package com.medHead.poc.testIntegration.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Classe de test d'intégration pour vérifier le comportement du RestTemplate configuré.
 * Ce test simule des réponses HTTP à l'aide de MockRestServiceServer.
 */
@SpringBootTest
public class RestTemplateConfigITest {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Teste un appel HTTP réussi avec le RestTemplate.
     * Vérifie que la réponse est correctement gérée par le RestTemplate.
     */
    @Test
    public void testRestTemplateSuccessfulCall() {
        // Création d'un serveur mock pour le RestTemplate
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        // Simuler une réponse HTTP 200 OK
        String expectedResponse = "{\"message\":\"success\"}";
        mockServer.expect(requestTo("/api/test"))
                .andRespond(withSuccess(expectedResponse, org.springframework.http.MediaType.APPLICATION_JSON));

        // Appel de l'API avec RestTemplate
        String response = restTemplate.getForObject("/api/test", String.class);

        // Validation de la réponse
        assert response != null;
        assert response.contains("success");

        // Vérification que la requête a bien été envoyée au mock server
        mockServer.verify();
    }

    /**
     * Teste la gestion des erreurs HTTP par le RestTemplate.
     * Vérifie qu'une erreur 500 Internal Server Error est correctement levée et gérée.
     */
    @Test
    public void testRestTemplateErrorHandler() {
        // Mock un serveur pour simuler les réponses HTTP
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        // Simuler une réponse 500 Internal Server Error
        mockServer.expect(requestTo("/api/test"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur"));

        // Appeler l'API
        try {
            restTemplate.getForEntity("/api/test", String.class);
        } catch (Exception e) {
            // Vérifiez ici que l'erreur a été correctement gérée
            System.out.println("Erreur capturée : " + e.getMessage());
        }

        // Vérification que le serveur a bien reçu la requête
        mockServer.verify();
    }
}
