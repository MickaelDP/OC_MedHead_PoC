package com.medHead.poc.services;

import com.medHead.poc.entities.Hopital;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour le service HopitalService.
 * Cette classe teste les fonctionnalités principales liées à la gestion des hôpitaux.
 */
public class HopitalServiceTest {

    private HopitalService hopitalService;

    @BeforeEach
    void setUp() {
        hopitalService = new HopitalService();
    }

    /**
     * Teste la sauvegarde d'un hôpital.
     * Vérifie que l'hôpital est bien enregistré avec un ID généré
     * et que ses propriétés sont correctement sauvegardées.
     */
    @Test
    void testSaveHopital() {
        Hopital hopital = new Hopital("Hopital Test", Arrays.asList(1, 2), 48.8566, 2.3522, 10);
        Hopital savedHopital = hopitalService.saveHopital(hopital);

        assertNotNull(savedHopital.getId());
        assertEquals("Hopital Test", savedHopital.getNom());
        assertEquals(2, savedHopital.getServiceIdsDisponibles().size());
    }

    /**
     * Teste la récupération de tous les hôpitaux.
     * Vérifie que le service renvoie une liste contenant tous les hôpitaux enregistrés.
     */
    @Test
    void testGetAllHopitaux() {
        hopitalService.saveHopital(new Hopital("Hopital A", Arrays.asList(1, 2), 48.8566, 2.3522, 10));
        hopitalService.saveHopital(new Hopital("Hopital B", Arrays.asList(3, 4), 43.8336, 4.3652, 5));

        List<Hopital> hopitaux = hopitalService.getAllHopitaux();
        assertEquals(2, hopitaux.size());
    }

    /**
     * Teste la récupération d'un hôpital par son ID.
     * Vérifie que l'hôpital correspondant est bien retrouvé.
     */
    @Test
    void testGetHopitalById() {
        Hopital hopital = new Hopital("Hopital Test", Arrays.asList(1, 2), 48.8566, 2.3522, 10);
        Hopital savedHopital = hopitalService.saveHopital(hopital);

        Optional<Hopital> retrievedHopital = hopitalService.getHopitalById(savedHopital.getId());
        assertTrue(retrievedHopital.isPresent());
        assertEquals(savedHopital.getNom(), retrievedHopital.get().getNom());
    }

    /**
     * Teste la suppression d'un hôpital.
     * Vérifie que l'hôpital est bien supprimé et qu'il ne peut plus être retrouvé.
     */
    @Test
    void testDeleteHopital() {
        Hopital hopital = new Hopital("Hopital Test", Arrays.asList(1, 2), 48.8566, 2.3522, 10);
        Hopital savedHopital = hopitalService.saveHopital(hopital);

        boolean deleted = hopitalService.deleteHopital(savedHopital.getId());
        assertTrue(deleted);

        Optional<Hopital> retrievedHopital = hopitalService.getHopitalById(savedHopital.getId());
        assertFalse(retrievedHopital.isPresent());
    }
}
