package com.medHead.poc.testIntegration.service;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.HopitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration pour le service HopitalService.
 * Vérifie les fonctionnalités principales du service.
 */
@SpringBootTest
public class PopulateHopitalServiceITest {

    private HopitalService hopitalService;

    @BeforeEach
    void setUp() {
        // Initialise le service
        hopitalService = new HopitalService();
    }

    @Test
    void testTrierHopitauxParDelaiEtLits() {
        // Prépare des données de test
        List<Hopital> hopitaux = List.of(
                new Hopital("Hopital A", List.of(1), 48.8566, 2.3522, 5),
                new Hopital("Hopital B", List.of(2), 48.8566, 2.3522, 0),
                new Hopital("Hopital C", List.of(3), 48.8566, 2.3522, 10)
        );

        // Appel de la méthode à tester
        List<Hopital> result = hopitalService.trierHopitauxParDelaiEtLits(hopitaux);

        // Assertions
        assertNotNull(result, "La liste triée ne doit pas être null.");
        assertEquals(2, result.size(), "La liste triée doit contenir uniquement les hôpitaux avec des lits disponibles.");
        assertEquals("Hopital A", result.get(0).getNom(), "Le premier hôpital doit être celui avec le délai le plus court.");
        assertEquals("Hopital C", result.get(1).getNom(), "Le second hôpital doit être celui avec le deuxième délai le plus court.");
    }

    @Test
    void testTrierHopitauxParDelaiSansLitsDisponibles() {
        // Prépare des données de test
        List<Hopital> hopitaux = List.of(
                new Hopital("Hopital A", List.of(1), 48.8566, 2.3522, 0),
                new Hopital("Hopital B", List.of(2), 48.8566, 2.3522, 0),
                new Hopital("Hopital C", List.of(3), 48.8566, 2.3522, 0)
        );

        // Simule l'attribution des délais (normalement calculé via un service comme GPSService)
        hopitaux.get(0).setDelai(10);
        hopitaux.get(1).setDelai(5);
        hopitaux.get(2).setDelai(15);


        // Appel de la méthode à tester
        List<Hopital> result = hopitalService.trierHopitauxParDelaiEtLits(hopitaux);

        // Assertions
        assertNotNull(result, "La liste triée ne doit pas être null.");
        assertEquals(3, result.size(), "La liste triée doit contenir tous les hôpitaux.");
        assertEquals("Hopital B", result.get(0).getNom(), "Le premier hôpital doit être celui avec le délai le plus court.");
        assertEquals("Hopital A", result.get(1).getNom(), "Le deuxième hôpital doit être celui avec le deuxième délai le plus court.");
        assertEquals("Hopital C", result.get(2).getNom(), "Le dernier hôpital doit être celui avec le délai le plus long.");
    }

    @Test
    void testMettreAJourLits() {
        // Prépare des données de test
        Hopital hopital = new Hopital("Hopital A", List.of(1), 48.8566, 2.3522, 5);

        // Met à jour les lits disponibles
        hopitalService.mettreAJourLits(hopital, 10);

        // Assertions
        assertEquals(10, hopital.getNombreLitDisponible(), "Le nombre de lits disponibles doit être mis à jour.");
    }

    @Test
    void testMettreAJourLitsException() {
        // Prépare des données de test
        Hopital hopital = new Hopital("Hopital A", List.of(1), 48.8566, 2.3522, 5);

        // Vérifie qu'une exception est levée
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> hopitalService.mettreAJourLits(hopital, -1)
        );

        assertEquals("Le nombre de lits disponibles ne peut pas être négatif.", exception.getMessage());
    }
}
