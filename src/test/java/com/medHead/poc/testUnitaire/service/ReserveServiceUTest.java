package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.ReserveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitaire pour la classe ReserveService.
 * Vérifie le comportement de la méthode reserveBed dans différents scénarios.
 */
public class ReserveServiceUTest {

    private ReserveService reserveService; // Instance du service à tester
    private Hopital hopital; // Objet Hopital utilisé dans les tests

    /**
     * Initialisation des données de test avant chaque méthode.
     */
    @BeforeEach
    void setUp() {
        // Initialisation du service
        reserveService = new ReserveService();

        // Préparer un objet Hopital pour les tests
        hopital = new Hopital("Hopital A", List.of(1, 2, 5), 48.8566, 2.3522, 10);
    }

    /**
     * Teste un scénario où la réservation réussit.
     * Vérifie que la méthode {@code reserveBed} retourne {@code true}.
     */
    @Test
    void reserveBed_Success() {
        // Appeler la méthode avec un résultat simulé de succès
        boolean result = reserveService.reserveBed(hopital, true);

        // Vérifications
        assertTrue(result, "La réservation aurait dû réussir.");
    }

    /**
     * Teste un scénario où la réservation échoue.
     * Vérifie que la méthode {@code reserveBed} retourne {@code false}.
     */
    @Test
    void reserveBed_Failure() {
        // Appeler la méthode avec un résultat simulé d'échec
        boolean result = reserveService.reserveBed(hopital, false);

        // Vérifications
        assertFalse(result, "La réservation aurait dû échouer.");
    }
}