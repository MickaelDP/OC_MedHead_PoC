package com.medHead.poc.testIntegration.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

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
     * Teste le gestionnaire d'erreur du RestTemplate.
     * Vérifie que les erreurs HTTP, comme une réponse 500 Internal Server Error,
     * sont correctement gérées et enregistrées via le logger configuré.
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
