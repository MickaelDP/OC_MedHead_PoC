package com.medHead.poc.testIntegration.controller;

import com.medHead.poc.services.GPSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


/**
 * Test pour valider le gestionnaire global des exceptions.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AdviceControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GPSService gpsService;                                                                   // Mock de la dépendance non nécessaire pour ce test


    /**
     * Teste le comportement du gestionnaire global pour IllegalArgumentException.
     * Vérifie que le gestionnaire renvoie une réponse HTTP 400 avec le message approprié.
     *
     * @throws Exception en cas d'erreur dans le test.
     */
    @Test
    public void testHandleIllegalArgumentException() throws Exception {
        // Simule une requête GET vers un endpoint qui lève une IllegalArgumentException
        mockMvc.perform(get("/api/patients/invalid-id"))                                  // Exemple d'URL qui déclenche l'exception
                .andExpect(status().isBadRequest())                                                  // Vérifie que le statut est 400 BAD REQUEST
                .andExpect(content().string("Invalid UUID string: invalid-id"));      // Vérifie le contenu du message d'erreur
    }
}
