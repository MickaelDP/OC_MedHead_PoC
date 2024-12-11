package com.medHead.poc.testIntegration.controller;

import com.medHead.poc.controller.TestController;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test d'intégration pour le contrôleur ReserveController.
 * Vérifie les interactions dans un environnement Spring complet.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestController.class) // Importer explicitement le contrôleur
public class ReserveControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.fixed-token}")
    private String fixedToken;

    private Cookie csrfCookie;
    private String csrfToken;
    private MvcResult result;

    /**
     * Récupère le token CSRF avant chaque test
     */
    @BeforeEach
    void setUp() throws Exception {
        // Étape 1 : Récupérer le cookie CSRF
        result = mockMvc.perform(get("/test/csrf"))
                .andExpect(status().isOk())
                .andReturn();

        // Récupérer le cookie CSRF de la réponse
        csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        if (csrfCookie != null) {
            csrfToken = csrfCookie.getValue();  // Stocke le token CSRF
        }

        csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        csrfToken = result.getResponse().getContentAsString();
    }

    /* test toke csrf */
    @Test
    void testGenerateCsrfToken() throws Exception {
        // Vérifier que le cookie CSRF existe
        assertNotNull(csrfCookie, "Le cookie CSRF est manquant !");
        // Vérifier que la valeur du token CSRF est non nulle
        assertNotNull(csrfCookie.getValue(), "Le token CSRF est vide !");
        // Ici, au lieu de comparer la valeur exacte, vous pouvez vérifier la longueur du token ou simplement s'il existe
        assertTrue(csrfCookie.getValue().length() > 0, "Le token CSRF est invalide ou vide.");
    }


    /**
     * Teste le scénario où la réservation réussit.
     * Vérifie que le contrôleur retourne une réponse HTTP 200 avec un message de succès.
     */
    @Test
    void testReserveBed_Success() throws Exception {
        // Étape 2 : Ajouter le token CSRF dans le header et le cookie
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
                        .cookie(csrfCookie)                                                // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                           // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)     // Votre JWT
                        .param("simulateSuccess", "true")                    // Paramètre pour simuler le succès
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hopitalJson))
                .andExpect(status().isOk());
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
                        .cookie(csrfCookie)                                                     // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)          // Ajout de l'en-tête Authorization
                        .param("simulateSuccess", "false")                        // Paramètre pour simuler un échec
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hopitalJson))                                                  // Corps de la requête
                .andExpect(status().isBadRequest())                                             // Vérifie le statut HTTP 400
                .andExpect(content().string("Échec de la réservation de lit.")); // Vérifie le message
    }

    /**
     * Teste le scénario où le corps de la requête est invalide (Hopital manquant).
     * Vérifie que le contrôleur retourne une réponse HTTP 400.
     */
    @Test
    void testReserveBed_InvalidRequest() throws Exception {

        // Effectuer une requête POST sans corps
        mockMvc.perform(post("/api/reserve")
                        .cookie(csrfCookie)                                                     // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)          // Ajout de l'en-tête Authorization
                        .param("simulateSuccess", "true")
                        .contentType(MediaType.APPLICATION_JSON))                               // Pas de contenu
                .andExpect(status().isBadRequest());                                            // Vérifie le statut HTTP 400
    }
}