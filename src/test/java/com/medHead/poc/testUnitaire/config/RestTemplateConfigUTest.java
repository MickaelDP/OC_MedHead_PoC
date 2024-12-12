package com.medHead.poc.testUnitaire.config;

import com.medHead.poc.services.PopulateHopitalServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe de test unitaire pour vérifier la configuration et l'injection
 * des dépendances dans le contexte Spring Boot.
 */
@SpringBootTest
public class RestTemplateConfigUTest {
    @Autowired
    private PopulateHopitalServiceInterface populateHopitalService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Vérifie que les beans PopulateHopitalService et RestTemplate
     * sont correctement injectés par le conteneur Spring.
     */
    @Test
    void testRestTemplateInjection() {
        assertNotNull(populateHopitalService, "Le service PopulateHopitalService n'a pas été injecté.");
        assertNotNull(restTemplate, "Le RestTemplate n'a pas été injecté.");
    }
}
