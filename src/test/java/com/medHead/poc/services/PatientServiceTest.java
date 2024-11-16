package com.medHead.poc.services;

import com.medHead.poc.entities.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour le service PatientService.
 * Cette classe teste les principales fonctionnalités liées à la gestion des patients.
 */
public class PatientServiceTest {

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService();
    }

    /**
     * Teste la sauvegarde d'un patient.
     * Vérifie que le patient est bien enregistré avec un ID généré
     * et que ses informations sont correctement sauvegardées.
     */
    @Test
    void testSavePatient() {
        Patient patient = new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        Patient savedPatient = patientService.savePatient(patient);

        assertNotNull(savedPatient.getId());
        assertEquals("Urgence", savedPatient.getSpecialite());
    }

    /**
     * Teste la récupération de tous les patients.
     * Vérifie que le service renvoie une liste contenant tous les patients enregistrés.
     */
    @Test
    void testGetAllPatients() {
        patientService.savePatient(new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
        List<Patient> patients = patientService.getAllPatients();

        assertEquals(1, patients.size());
    }

    /**
     * Teste la suppression d'un patient.
     * Vérifie que le patient est bien supprimé et qu'il ne peut plus être retrouvé.
     */
    @Test
    void testDeletePatient() {
        Patient patient = patientService.savePatient(new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
        boolean deleted = patientService.deletePatient(patient.getId());

        assertTrue(deleted);
        assertTrue(patientService.getAllPatients().isEmpty());
    }
}
