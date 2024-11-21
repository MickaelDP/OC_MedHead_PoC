package com.medHead.poc.testIntegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test d'intégration pour le contrôleur HopitalController.
 * Vérifie le fonctionnement des endpoints exposés pour la gestion des hôpitaux.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PopulateHopitalControllerITest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Teste le endpoint GET /api/hospitals.
     * Vérifie que la réponse contient un tableau de hôpitaux avec la taille attendue.
     *
     * @throws Exception en cas d'échec de l'appel ou de la vérification.
     */
    @Test
    void testGetHospitals() throws Exception {
        mockMvc.perform(get("/api/hospitals")
                        .param("serviceId", "1")
                        .param("lat", "48.8566")
                        .param("lon", "2.3522"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10));
    }
}
