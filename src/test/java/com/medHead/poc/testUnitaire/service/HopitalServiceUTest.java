package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.HopitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour le service HopitalService.
 * Vérifie les fonctionnalités principales comme le tri et la mise à jour des lits.
 */
public class HopitalServiceUTest {

    private HopitalService hopitalService;

    @BeforeEach
    void setUp() {
        hopitalService = new HopitalService();
    }

    /**
     * Teste le tri des hôpitaux par délai et lits disponibles.
     * Vérifie que les hôpitaux sont triés correctement.
     */
    @Test
    void testTrierHopitauxParDelaiEtLits() {
        Hopital h1 = new Hopital("Hopital A", Arrays.asList(1, 2), 48.8566, 2.3522, 5);
        h1.setDelai(20);
        Hopital h2 = new Hopital("Hopital B", Arrays.asList(1, 3), 43.8336, 4.3652, 0);        // Pas de lits
        h2.setDelai(10);
        Hopital h3 = new Hopital("Hopital C", Arrays.asList(2, 4), 45.764, 4.8357, 3);
        h3.setDelai(15);

        List<Hopital> hopitaux = Arrays.asList(h1, h2, h3);
        List<Hopital> result = hopitalService.trierHopitauxParDelaiEtLits(hopitaux);

        assertEquals(2, result.size(), "Seuls les hôpitaux avec des lits disponibles doivent être inclus.");

        // Vérifie que le tri est correct (par délai et lits disponibles)
        assertEquals("Hopital C", result.get(0).getNom());
        assertEquals("Hopital A", result.get(1).getNom());
    }

    /**
     * Teste la mise à jour du nombre de lits disponibles.
     * Vérifie que le nombre de lits est correctement modifié.
     */
    @Test
    void testMettreAJourLits() {
        Hopital hopital = new Hopital("Hopital Test", Arrays.asList(1, 2), 48.8566, 2.3522, 10);
        hopitalService.mettreAJourLits(hopital, 5);
        assertEquals(5, hopital.getNombreLitDisponible());

        // Vérifie qu'une exception est levée pour une valeur négative
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                hopitalService.mettreAJourLits(hopital, -1));
        assertEquals("Le nombre de lits disponibles ne peut pas être négatif.", exception.getMessage());
    }

    /**
     * Teste le tri quand aucun lit n'est disponible.
     * Vérifie que le tri est effectué uniquement par délai.
     */
    @Test
    void testTrierHopitauxSansLitsDisponibles() {
        Hopital h1 = new Hopital("Hopital A", Arrays.asList(1), 48.8566, 2.3522, 0);
        h1.setDelai(30);
        Hopital h2 = new Hopital("Hopital B", Arrays.asList(1), 43.8336, 4.3652, 0);
        h2.setDelai(10);

        List<Hopital> hopitaux = Arrays.asList(h1, h2);
        List<Hopital> result = hopitalService.trierHopitauxParDelaiEtLits(hopitaux);

        // Vérifie que le tri se base uniquement sur le délai
        assertEquals("Hopital B", result.get(0).getNom());
        assertEquals("Hopital A", result.get(1).getNom());
    }

    /**
     * Teste la méthode mettreAJourLits pour vérifier qu'il est possible
     * de mettre à jour le nombre de lits disponibles à zéro.
     */
    @Test
    void testMettreAJourLitsValeurZero() {
        Hopital hopital = new Hopital("Hopital Test", Arrays.asList(1, 2), 48.8566, 2.3522, 10);
        hopitalService.mettreAJourLits(hopital, 0);
        assertEquals(0, hopital.getNombreLitDisponible(), "Le nombre de lits doit pouvoir être mis à zéro.");
    }
}
