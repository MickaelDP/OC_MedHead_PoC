package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.services.PatientTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe PatientTrackerService.
 * Vérifie les fonctionnalités de suivi des patients en traitement.
 */
public class PatientTrackerServiceUtest {


    private PatientTrackerService patientTrackerService;

    @BeforeEach
    void setUp() {
        patientTrackerService = new PatientTrackerService();
    }

    /**
     * Teste l'ajout d'un patient pour la première fois.
     */
    @Test
    void testStartProcessing_AddsPatientSuccessfully() {
        UUID patientId = UUID.randomUUID();

        // Ajouter un patient
        boolean added = patientTrackerService.startProcessing(patientId);

        // Vérifications
        assertTrue(added, "Le patient doit être ajouté avec succès.");
        assertTrue(patientTrackerService.isProcessing(patientId), "Le patient doit être en cours de traitement.");
    }

    /**
     * Teste qu'une exception est levée si un patient est déjà en cours de traitement.
     */
    @Test
    void testStartProcessing_ThrowsExceptionIfAlreadyProcessing() {
        UUID patientId = UUID.randomUUID();

        // Ajouter le patient une première fois
        patientTrackerService.startProcessing(patientId);

        // Vérifier que l'ajout du même patient lève une exception
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> patientTrackerService.startProcessing(patientId),
                "Une exception doit être levée si le patient est déjà en cours de traitement.");
        assertEquals("Patient déjà en cours de traitement", exception.getMessage());
    }

    /**
     * Teste la suppression d'un patient après traitement.
     */
    @Test
    void testEndProcessing_RemovesPatientSuccessfully() {
        UUID patientId = UUID.randomUUID();

        // Ajouter et ensuite supprimer un patient
        patientTrackerService.startProcessing(patientId);
        patientTrackerService.endProcessing(patientId);

        // Vérifications
        assertFalse(patientTrackerService.isProcessing(patientId), "Le patient ne doit plus être en cours de traitement.");
    }

    /**
     * Teste que la suppression d'un patient non existant ne cause pas d'erreur.
     */
    @Test
    void testEndProcessing_NoErrorIfPatientNotFound() {
        UUID patientId = UUID.randomUUID();

        // Appeler endProcessing sur un ID qui n'existe pas
        patientTrackerService.endProcessing(patientId);

        // Vérifications
        assertFalse(patientTrackerService.isProcessing(patientId), "Le patient ne doit pas être en cours de traitement.");
    }

    /**
     * Teste que `isProcessing` retourne vrai pour un patient en traitement.
     */
    @Test
    void testIsProcessing_ReturnsTrueIfPatientIsProcessing() {
        UUID patientId = UUID.randomUUID();

        // Ajouter un patient
        patientTrackerService.startProcessing(patientId);

        // Vérification
        assertTrue(patientTrackerService.isProcessing(patientId), "Le patient doit être en cours de traitement.");
    }

    /**
     * Teste que `isProcessing` retourne faux pour un patient non ajouté.
     */
    @Test
    void testIsProcessing_ReturnsFalseIfPatientIsNotProcessing() {
        UUID patientId = UUID.randomUUID();

        // Vérification
        assertFalse(patientTrackerService.isProcessing(patientId), "Le patient ne doit pas être en cours de traitement.");
    }
}