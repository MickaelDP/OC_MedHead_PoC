package com.medHead.poc.testUnitaire.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.controller.ReserveController;
import com.medHead.poc.services.ReserveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Classe de test unitaire pour le contrôleur ReserveController.
 * Vérifie le comportement des endpoints pour gérer les réservations de lits.
 */
class ReserveServiceControllerUTest {

    @Mock
    private ReserveService reserveService;

    @InjectMocks
    private ReserveController reserveController;

    private Hopital hopital;

    /**
     * Initialisation avant chaque test.
     * Configure les mocks et initialise l'objet Hopital.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hopital = new Hopital("Hopital A", List.of(1, 2), 48.8566, 2.3522, 5);
    }

    /**
     * Teste un scénario où la réservation est réussie.
     * Vérifie que la méthode reserveBed retourne une réponse HTTP 200 avec un message de succès.
     */
    @Test
    void reserveBed_Success() {
        // Simule le succès du service
        when(reserveService.reserveBed(hopital, true)).thenReturn(true);

        // Appelle le contrôleur avec simulateSuccess=true
        ResponseEntity<String> response = reserveController.reserveBed(hopital, true);

        // Vérifie les résultats
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Réservation de lit réussie.", response.getBody());
        verify(reserveService, times(1)).reserveBed(hopital, true);
    }

    /**
     * Teste un scénario où la réservation échoue.
     * Vérifie que la méthode reserveBed retourne une réponse HTTP 400 avec un message d'échec.
     */
    @Test
    void reserveBed_Failure() {
        // Simule un échec du service
        when(reserveService.reserveBed(hopital, false)).thenReturn(false);

        // Appelle le contrôleur avec simulateSuccess=false
        ResponseEntity<String> response = reserveController.reserveBed(hopital, false);

        // Vérifie les résultats
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Échec de la réservation de lit.", response.getBody());
        verify(reserveService, times(1)).reserveBed(hopital, false);
    }
}