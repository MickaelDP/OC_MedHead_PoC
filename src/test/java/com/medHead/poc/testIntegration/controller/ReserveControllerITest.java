package com.medHead.poc.testIntegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test d'intégration pour le contrôleur ReserveController.
 * Vérifie les interactions dans un environnement Spring complet.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ReserveControllerITest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Teste le scénario où la réservation réussit.
     * Vérifie que le contrôleur retourne une réponse HTTP 200 avec un message de succès.
     */
    @Test
    void testReserveBed_Success() throws Exception {
        // Corps JSON de la requête
        String hopitalJson = """
            {
                "nom": "Hopital A",
                "latitude": 48.8566,
                "longitude": 2.3522,
                "nombreLitDisponible": 5,
                "delai": 10,
                "serviceId": [1, 2]
            }
        """;

        // Effectuer une requête POST avec succès simulé
        mockMvc.perform(post("/api/reserve")
                        .param("simulateSuccess", "true")                         // Paramètre pour simuler le succès
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hopitalJson))                                                  // Corps de la requête
                .andExpect(status().isOk())                                                     // Vérifie le statut HTTP 200
                .andExpect(content().string("Réservation de lit réussie."));      // Vérifie le message
    }

    /**
     * Teste le scénario où la réservation échoue.
     * Vérifie que le contrôleur retourne une réponse HTTP 400 avec un message d'échec.
     */
    @Test
    void testReserveBed_Failure() throws Exception {
        // Corps JSON de la requête
        String hopitalJson = """
            {
                "nom": "Hopital B",
                "latitude": 48.8648,
                "longitude": 2.3499,
                "nombreLitDisponible": 0,
                "delai": 15,
                "serviceId": [2, 3]
            }
        """;

        // Effectuer une requête POST avec échec simulé
        mockMvc.perform(post("/api/reserve")
                        .param("simulateSuccess", "false")                        // Paramètre pour simuler un échec
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hopitalJson))                                                  // Corps de la requête
                .andExpect(status().isBadRequest())                                             // Vérifie le statut HTTP 400
                .andExpect(content().string("Échec de la réservation de lit."));  // Vérifie le message
    }

    /**
     * Teste le scénario où le corps de la requête est invalide (Hopital manquant).
     * Vérifie que le contrôleur retourne une réponse HTTP 400.
     */
    @Test
    void testReserveBed_InvalidRequest() throws Exception {
        // Effectuer une requête POST sans corps
        mockMvc.perform(post("/api/reserve")
                        .param("simulateSuccess", "true")
                        .contentType(MediaType.APPLICATION_JSON))                               // Pas de contenu
                .andExpect(status().isBadRequest());                                            // Vérifie le statut HTTP 400
    }
}
