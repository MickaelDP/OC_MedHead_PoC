package com.medHead.poc.testUnitaire.controller;

import com.medHead.poc.controller.AdviceController;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe de test unitaire pour AdviceController.
 * Vérifie la gestion des exceptions par le contrôleur global des conseils.
 */
public class AdviceControllerUTest {

    /**
     * Teste la méthode handleIllegalArgumentException pour s'assurer qu'elle
     * retourne une réponse HTTP 400 avec le message d'erreur correct.
     */
    @Test
    void testHandleIllegalArgumentException() {
        AdviceController controller = new AdviceController();
        IllegalArgumentException ex = new IllegalArgumentException("Message d'erreur de test");
        ResponseEntity<String> response = controller.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Message d'erreur de test", response.getBody());
    }
}
