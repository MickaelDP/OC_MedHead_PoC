package com.medHead.poc.testIntegration.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.ReserveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration pour la classe ReserveService.
 * Vérifie le comportement du service dans un environnement Spring complet.
 */
@SpringBootTest
class ReserveServiceITest {

    @Autowired
    private ReserveService reserveService;

    /**
     * Teste un scénario où la réservation est simulée avec succès.
     * Vérifie que la méthode reserveBed retourne true.
     */
    @Test
    void reserveBed_Success() {
        // Préparer un objet Hopital
        Hopital hopital = new Hopital("Hopital A", List.of(1, 2), 48.8566, 2.3522, 5);

        // Simuler une réservation réussie
        boolean result = reserveService.reserveBed(hopital, true);

        // Vérification du résultat
        assertTrue(result, "La réservation aurait dû réussir.");
    }

    /**
     * Teste un scénario où la réservation est simulée comme un échec.
     * Vérifie que la méthode reserveBed retourne false.
     */
    @Test
    void reserveBed_Failure() {
        // Préparer un objet Hopital
        Hopital hopital = new Hopital("Hopital A", List.of(1, 2), 48.8566, 2.3522, 5);

        // Simuler un échec de réservation
        boolean result = reserveService.reserveBed(hopital, false);

        // Vérification du résultat
        assertFalse(result, "La réservation aurait dû échouer.");
    }
}