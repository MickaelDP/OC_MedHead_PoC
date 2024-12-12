package com.medHead.poc.testIntegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration pour GPSController.
 * Ces tests valident le bon fonctionnement du contrôleur REST de GPS.
 * Ils vérifient la réponse pour des coordonnées valides et invalides,
 * ainsi que les valeurs des délais simulés retournées par le service.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GPSControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.fixed-token}")                                                        // Injection du token depuis application.properties
    private String fixedToken;

    /**
     * Teste l'endpoint /api/gps/delay avec des coordonnées valides.
     * Vérifie que le délai simulé retourné est bien dans la plage attendue (5 à 30 minutes).
     *
     * @throws Exception En cas d'erreur dans l'appel du contrôleur.
     */
    @Test
    void testGetTravelDelay() throws Exception {
        // Appel au contrôleur avec des paramètres valides
        mockMvc.perform(get("/api/gps/delay")
                        .header("Authorization", "Bearer " + fixedToken)
                        .param("patientLat", "48.8566")
                        .param("patientLon", "2.3522")
                        .param("hospitalLat", "48.8600")
                        .param("hospitalLon", "2.3260"))
                .andExpect(status().isOk())                                             // Vérifie que le statut HTTP est 200
                .andExpect(jsonPath("$").isNumber())                          // Vérifie que la réponse est un nombre
                .andExpect(jsonPath("$", greaterThanOrEqualTo(5)))      // Vérifie que la réponse est >= 5
                .andExpect(jsonPath("$", lessThanOrEqualTo(30)));       // Vérifie que la réponse est <= 30
    }

    /**
     * Teste l'endpoint /api/gps/delay pour des coordonnées valides.
     * Vérifie que le statut de la réponse est HTTP 200 et que la réponse est un nombre.
     *
     * @throws Exception En cas d'erreur dans l'appel du contrôleur.
     */
    @Test
    void testValidCoordinates() throws Exception {

        mockMvc.perform(get("/api/gps/delay")
                        .header("Authorization", "Bearer " + fixedToken) // Ajoute le token au header
                        .param("patientLat", "48.8566")
                        .param("patientLon", "2.3522")
                        .param("hospitalLat", "48.8600")
                        .param("hospitalLon", "2.3260")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    /**
     * Teste l'endpoint /api/gps/delay avec des coordonnées invalides.
     * Vérifie que le statut de la réponse est HTTP 400 (Bad Request).
     *
     * @throws Exception En cas d'erreur dans l'appel du contrôleur.
     */
    @Test
    void testInvalidCoordinates() throws Exception {

        // Test avec des coordonnées invalides
        mockMvc.perform(get("/api/gps/delay")
                        .header("Authorization", "Bearer " + fixedToken) // Ajoute le token au header
                        .param("patientLat", "100.0") // Latitude invalide
                        .param("patientLon", "2.3522")
                        .param("hospitalLat", "48.8600")
                        .param("hospitalLon", "2.3260")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
