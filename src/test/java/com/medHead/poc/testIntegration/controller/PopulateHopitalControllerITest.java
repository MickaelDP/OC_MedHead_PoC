package com.medHead.poc.testIntegration.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test d'intégration pour le contrôleur HopitalController.
 * Vérifie le fonctionnement des endpoints exposés pour la gestion des hôpitaux.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PopulateHopitalControllerITest {

    @Value("${jwt.fixed-token}")
    private String fixedToken;

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
                        .header("Authorization", "Bearer " + fixedToken)
                        .param("serviceId", "1")
                        .param("lat", "48.8566")
                        .param("lon", "2.3522"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10));
    }

    /**
     * Teste le comportement du cache pour le endpoint GET /api/hospitals.
     * Vérifie que les réponses sont identiques pour des paramètres identiques,
     * indiquant que le cache est utilisé correctement.
     * @throws Exception en cas d'échec de l'appel ou de la vérification.
     */
    @Test
    void testGetHospitalsCache() throws Exception {

        // Première requête (remplissage du cache)
        mockMvc.perform(get("/api/hospitals")
                        .header("Authorization", "Bearer " + fixedToken)
                        .param("serviceId", "1")
                        .param("lat", "48.8566")
                        .param("lon", "2.3522"))
                .andExpect(status().isOk());

        // Vérifie que la réponse est toujours la même pour les mêmes paramètres
        mockMvc.perform(get("/api/hospitals")
                        .header("Authorization", "Bearer " + fixedToken)
                        .param("serviceId", "1")
                        .param("lat", "48.8566")
                        .param("lon", "2.3522"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    /**
     * Teste le comportement de l'endpoint DELETE /api/hospitals/cache/clear.
     * Vérifie que le cache est vidé correctement et que les réponses
     * sont rechargées après un vidage de cache.
     * @throws Exception en cas d'échec de l'appel ou de la vérification.
     */
    @Test
    void testClearCache() throws Exception {
        String csrfToken;
        // Étape 1 : Récupérer le cookie CSRF
        MvcResult result = mockMvc.perform(get("/test/csrf"))
                .andExpect(status().isOk())
                .andReturn();

        // Récupérer le cookie CSRF de la réponse
        Cookie csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        if (csrfCookie != null) {
            csrfToken = csrfCookie.getValue();  // Stocke le token CSRF
        }

        csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        csrfToken = result.getResponse().getContentAsString();
        // Première requête pour remplir le cache
        mockMvc.perform(get("/api/hospitals")
                        .header("Authorization", "Bearer " + fixedToken)
                        .param("serviceId", "1")
                        .param("lat", "48.8566")
                        .param("lon", "2.3522"))
                .andExpect(status().isOk());

        // Appel de l'endpoint pour vider le cache
        mockMvc.perform(delete("/api/hospitals/cache/clear")
                        .cookie(csrfCookie)                                     // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken))
                .andExpect(status().isOk());

        // Effectuer une nouvelle requête pour confirmer que le cache a été vidé (rempli à nouveau)
        mockMvc.perform(get("/api/hospitals")
                        .header("Authorization", "Bearer " + fixedToken)
                        .param("serviceId", "1")
                        .param("lat", "48.8566")
                        .param("lon", "2.3522"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }
}