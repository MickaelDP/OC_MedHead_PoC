package com.medHead.poc.testIntegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test pour valider le gestionnaire global des exceptions.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AdviceControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.fixed-token}")                                                                     // Injecte la valeur du token depuis application.properties
    private String fixedToken;

    /**
     * Teste le comportement du gestionnaire global pour IllegalArgumentException.
     * Vérifie que le gestionnaire renvoie une réponse HTTP 400 avec le message approprié.
     *
     * @throws Exception en cas d'erreur dans le test.
     */
    @Test
    public void testHandleIllegalArgumentException() throws Exception {
        // Envoie la requête avec un ID invalide
        mockMvc.perform(get("/api/patients/invalid-id")
                        .header("Authorization", "Bearer " + fixedToken))               // Ajoute le token dans le header
                .andExpect(status().isBadRequest()) // Vérifie que le statut est 400 (Bad Request)
                .andExpect(content().string("Invalid UUID string: invalid-id"));        // Vérifie le message d'erreur
    }
}
