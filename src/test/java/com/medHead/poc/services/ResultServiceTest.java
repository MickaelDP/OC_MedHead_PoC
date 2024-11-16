package com.medHead.poc.services;

import com.medHead.poc.entities.Hopital;
import com.medHead.poc.entities.Patient;
import com.medHead.poc.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour le service ResultService.
 * Cette classe teste les principales fonctionnalités liées à la gestion des résultats.
 */
public class ResultServiceTest {

    private ResultService resultService;
    private Patient patient;
    private List<Hopital> hopitaux;


    @BeforeEach
    void setUp() {
        resultService = new ResultService();
        patient = new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        hopitaux = Arrays.asList(
                new Hopital("Hopital A", Arrays.asList(1, 2), 48.8566, 2.3522, 10),
                new Hopital("Hopital B", Arrays.asList(2, 3), 48.8648, 2.3499, 0) // Aucun lit disponible
        );
    }

    /**
     * Teste la création d'un résultat pour un patient lorsqu'il y a des lits disponibles.
     * Vérifie que le résultat est correctement créé et que le lit est disponible.
     */
    @Test
    void testCreateResultForPatient() {
        Result result = resultService.createResultForPatient(patient, hopitaux);

        assertNotNull(result);
        assertEquals(patient.getId(), result.getPatientId());
        assertEquals("Hopital A", result.getHopitalNom());
        assertTrue(result.isLitDisponible());
    }

    /**
     * Teste la création d'un résultat pour un patient lorsque tous les lits sont indisponibles.
     * Vérifie que le résultat est créé, mais sans lit disponible.
     */
    @Test
    void testCreateResultForPatientWhenNoBedsAvailable() {
        // Tous les lits sont indisponibles dans cette liste
        List<Hopital> hospitalsNoBeds = Arrays.asList(
                new Hopital("Hopital C", Arrays.asList(1, 2), 48.8584, 2.2945, 0),
                new Hopital("Hopital D", Arrays.asList(2, 3), 48.8600, 2.3270, 0)
        );
        Result result = resultService.createResultForPatient(patient, hospitalsNoBeds);

        assertNotNull(result);
        assertEquals(patient.getId(), result.getPatientId());
        assertFalse(result.isLitDisponible());
    }

    /**
     * Teste la sauvegarde et la récupération d'un résultat.
     * Vérifie que le résultat sauvegardé peut être récupéré correctement par son ID.
     */
    @Test
    void testSaveAndGetResult() {
        Result result = new Result(patient.getId(), "Urgence", "Hopital A", 15, true, true);
        Result savedResult = resultService.saveResult(result);

        assertNotNull(savedResult.getId());
        Result retrievedResult = resultService.getResultById(savedResult.getId());
        assertEquals(savedResult.getHopitalNom(), retrievedResult.getHopitalNom());
    }

    /**
     * Teste la suppression d'un résultat.
     * Vérifie que le résultat est bien supprimé et qu'il ne peut plus être retrouvé.
     */
    @Test
    void testDeleteResult() {
        Result result = new Result(patient.getId(), "Pédiatrie", "Hopital B", 20, true, true);
        Result savedResult = resultService.saveResult(result);

        boolean deleted = resultService.deleteResult(savedResult.getId());
        assertTrue(deleted);

        Result retrievedResult = resultService.getResultById(savedResult.getId());
        assertNull(retrievedResult);
    }
}
