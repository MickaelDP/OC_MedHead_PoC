package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.model.Patient;
import com.medHead.poc.model.Result;
import com.medHead.poc.services.ResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour le service ResultService.
 * Vérifie la création, la sauvegarde et la récupération des résultats,
 * tout en prenant en compte les validations et la logique métier.
 */
public class ResultServiceUTest {

    private ResultService resultService;
    private Patient patient;
    private List<Hopital> hopitaux;

    @BeforeEach
    void setUp() {
        resultService = new ResultService();
        patient = new Patient("Cardiologie", "Dr. House", "Qualité B", 48.8566, 2.3522);
        hopitaux = Arrays.asList(
                new Hopital("Hopital A", Arrays.asList(1, 2), 48.8566, 2.3522, 10),
                new Hopital("Hopital B", Arrays.asList(2, 3), 48.8648, 2.3499, 0)
        );
    }

    /**
     * Teste la création et la sauvegarde d'un résultat pour un patient
     * lorsqu'il y a des lits disponibles.
     */
    @Test
    void testCreateAndSaveResultWithBedsAvailable() {
        Hopital hopital = hopitaux.get(0); // Hôpital avec lits disponibles

        Result result = resultService.createAndSaveResult(patient, hopital, true);

        assertNotNull(result, "Le résultat ne doit pas être null.");
        assertEquals(patient.getId(), result.getPatientId(), "L'ID du patient ne correspond pas.");
        assertEquals("Hopital A", result.getHopitalNom(), "Le nom de l'hôpital ne correspond pas.");
        assertTrue(result.isLitReserve(), "Le lit aurait dû être marqué comme réservé.");
    }

    /**
     * Teste la création et la sauvegarde d'un résultat pour un patient
     * lorsque tous les lits sont indisponibles.
     */
    @Test
    void testCreateAndSaveResultNoBedsAvailable() {
        Hopital hopital = hopitaux.get(1); // Hôpital sans lits disponibles

        Result result = resultService.createAndSaveResult(patient, hopital, false);

        assertNotNull(result, "Le résultat ne doit pas être null.");
        assertEquals(patient.getId(), result.getPatientId(), "L'ID du patient ne correspond pas.");
        assertEquals("Hopital B", result.getHopitalNom(), "Le nom de l'hôpital ne correspond pas.");
        assertFalse(result.isLitReserve(), "Le lit aurait dû être marqué comme non réservé.");
    }

    /**
     * Teste la validation des données lors de la création d'un résultat.
     * Vérifie qu'une exception est levée si les données du patient ou de l'hôpital sont invalides.
     */
    @Test
    void testValidationInCreateAndSaveResult() {
        // Cas patient null
        Exception patientException = assertThrows(IllegalArgumentException.class, () ->
                resultService.createAndSaveResult(null, hopitaux.getFirst(), true)
        );
        assertEquals("Les informations du patient sont invalides ou incomplètes.", patientException.getMessage());

        // Cas hôpital null
        Exception hopitalException = assertThrows(IllegalArgumentException.class, () ->
                resultService.createAndSaveResult(patient, null, true)
        );
        assertEquals("Les informations de l'hôpital sont invalides ou incomplètes.", hopitalException.getMessage());
    }

    /**
     * Teste la récupération d'un résultat sauvegardé par son ID.
     */
    @Test
    void testSaveAndRetrieveResult() {
        Result result = new Result(patient.getId(), "Cardiologie", "Hopital A", 15, true, true);

        Result savedResult = resultService.saveResult(result);
        assertNotNull(savedResult.getId(), "L'ID du résultat sauvegardé ne doit pas être null.");

        Result retrievedResult = resultService.getResultById(savedResult.getId());
        assertNotNull(retrievedResult, "Le résultat récupéré ne doit pas être null.");
        assertEquals("Hopital A", retrievedResult.getHopitalNom(), "Le nom de l'hôpital ne correspond pas.");
    }

    /**
     * Teste la suppression d'un résultat par son ID.
     */
    @Test
    void testDeleteResult() {
        Result result = new Result(patient.getId(), "Pédiatrie", "Hopital B", 20, true, false);
        Result savedResult = resultService.saveResult(result);

        boolean deleted = resultService.deleteResult(savedResult.getId());
        assertTrue(deleted, "Le résultat aurait dû être supprimé.");

        Result retrievedResult = resultService.getResultById(savedResult.getId());
        assertNull(retrievedResult, "Le résultat supprimé ne devrait plus être récupérable.");
    }

    /**
     * Teste le comportement lorsque la liste des résultats est vide.
     */
    @Test
    void testGetResultFromEmptyList() {
        assertNull(resultService.getResultById(UUID.randomUUID()), "Aucun résultat ne devrait être trouvé dans une liste vide.");
    }

    /**
    * Test de la méthode ResultService.getAllResults().
    * Vérifie que tous les résultats sauvegardés dans le service sont correctement retournés.
    */
    @Test
    void testGetAllResults_ReturnsExpectedResults() {
        // Ajouter des résultats
        Result result1 = new Result(UUID.randomUUID(), "Cardiologie", "Hôpital A", 10, true, true);
        Result result2 = new Result(UUID.randomUUID(), "Neurologie", "Hôpital B", 15, true, false);

        // Ajouter les résultats dans le service (imaginons qu'il y ait une méthode addResult)
        resultService.saveResult(result1);
        resultService.saveResult(result2);

        // Récupérer tous les résultats
        List<Result> results = resultService.getAllResults();

        // Vérifier le contenu de la liste
        assertEquals(2, results.size(), "Il doit y avoir deux résultats.");
        assertTrue(results.contains(result1), "La liste doit contenir result1.");
        assertTrue(results.contains(result2), "La liste doit contenir result2.");
    }

    /**
     * Teste la suppression d'un résultat inexistant.
     * Vérifie que la méthode deleteResult retourne false lorsqu'un ID
     * de résultat qui n'existe pas est fourni.
     */
    @Test
    void testDeleteNonExistentResult() {
        boolean deleted = resultService.deleteResult(UUID.randomUUID());
        assertFalse(deleted, "La suppression d'un résultat inexistant doit retourner false.");
    }

    /**
     * Teste la limite de taille du cache dans le service ResultService.
     * Vérifie que le cache ne contient jamais plus de 100 résultats, conformément
     * à la limite définie dans la classe ResultService.
     */
    @Test
    void testCacheSizeLimit() {
        for (int i = 0; i < 110; i++) {
            resultService.saveResult(new Result(UUID.randomUUID(), "Specialite" + i, "Hopital" + i, i, true, true));
        }

        List<Result> results = resultService.getAllResults();
        assertEquals(100, results.size(), "Le cache ne doit pas contenir plus de 100 résultats.");
    }
}
