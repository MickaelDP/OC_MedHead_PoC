package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.model.Patient;
import com.medHead.poc.services.PatientServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de test pour le service PatientService.
 */
@SpringBootTest
public class PatientServiceUTest {

    @Autowired
    private PatientServiceInterface patientService;

    @Test
    void testInitializePatient_ValidPatient() {
        Patient patient = new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        Patient initializedPatient = patientService.initializePatient(patient);

        assertNotNull(initializedPatient.getId(), "L'ID du patient ne doit pas être null.");
        assertEquals(5, initializedPatient.getServiceId(), "Le service ID doit correspondre à la spécialité.");
    }

    @Test
    void testInitializePatient_InvalidSpeciality() {
        Patient patient = new Patient("SpécialitéInconnue", "Dr. Smith", "Qualité A", 48.8566, 2.3522);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> patientService.initializePatient(patient));

        assertEquals("Spécialité inconnue : SpécialitéInconnue", exception.getMessage());
    }

    /**
     * Teste la sauvegarde d'un patient.
     * Vérifie que le patient est bien enregistré avec un ID généré
     * et que ses informations sont correctement sauvegardées.
     */
    @Test
    void testSavePatient() {
        // Utiliser une spécialité valide
        Patient patient = new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        Patient savedPatient = patientService.savePatient(patient);

        assertNotNull(savedPatient.getId(), "L'ID du patient ne doit pas être null.");
        assertEquals(5, savedPatient.getServiceId(), "Le service ID doit être correctement attribué (Urgence -> 4).");
        assertEquals("Cardiologie", savedPatient.getSpecialite(), "La spécialité doit être correctement sauvegardée.");
    }

    /**
     * Teste la méthode savePatient avec une spécialité invalide.
     * Vérifie qu'une exception IllegalArgumentException est levée
     * lorsque la spécialité d'un patient n'est pas présente dans le dictionnaire des spécialités.
     */
    @Test
    void testSavePatient_InvalidSpeciality() {
        Patient patient = new Patient("SpécialitéInconnue", "Dr. Smith", "Qualité A", 48.8566, 2.3522);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> patientService.savePatient(patient));

        assertEquals("Spécialité inconnue : SpécialitéInconnue", exception.getMessage());
    }

    /**
     * Teste la récupération de tous les patients.
     * Vérifie que le service renvoie une liste contenant tous les patients enregistrés.
     */
    @Test
    void testGetAllPatients() {
        patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
        patientService.savePatient(new Patient("Pédiatrie", "Dr. Jones", "Qualité B", 34.0522, -118.2437));

        List<Patient> patients = patientService.getAllPatients();

        assertEquals(6, patients.size(), "La liste des patients doit contenir 2 entrées.");
    }

    /**
     * Teste la récupération d'un patient par ID.
     * Vérifie que le service retourne le bon patient lorsqu'il est trouvé.
     */
    @Test
    void testGetPatientById() {
        Patient patient = patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
        Optional<Patient> retrievedPatient = patientService.getPatientById(patient.getId());

        assertTrue(retrievedPatient.isPresent(), "Le patient doit être trouvé par son ID.");
        assertEquals(patient.getId(), retrievedPatient.get().getId(), "Les ID doivent correspondre.");
    }

    /**
     * Teste la récupération d'un patient inexistant par ID.
     * Vérifie que le service retourne un Optional vide si aucun patient n'est trouvé.
     */
    @Test
    void testGetPatientById_NotFound() {
        Patient patient = patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
        Optional<Patient> retrievedPatient = patientService.getPatientById(patient.getId());

        assertTrue(retrievedPatient.isPresent(), "Le patient doit être trouvé par son ID.");
        assertEquals(patient.getId(), retrievedPatient.get().getId(), "Les ID doivent correspondre.");
    }

    /**
     * Teste la suppression d'un patient.
     * Vérifie que le patient est bien supprimé et qu'il ne peut plus être retrouvé.
     */
    @Test
    void testDeletePatient() {
        Patient patient = patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));

        // Compter le nombre de patients avant la suppression
        int initialSize = patientService.getAllPatients().size();

        boolean deleted = patientService.deletePatient(patient.getId());

        assertTrue(deleted, "La suppression doit retourner true.");
        // Vérifier que le nombre de patients est réduit de 1 après suppression
        assertEquals(initialSize - 1, patientService.getAllPatients().size(), "La liste des patients doit être réduite de 1 après suppression.");

    }

    /**
     * Teste la suppression d'un patient inexistant.
     * Vérifie que le service retourne false si le patient à supprimer n'existe pas.
     */
    @Test
    void testDeletePatient_NotFound() {
        boolean deleted = patientService.deletePatient(UUID.randomUUID());

        assertFalse(deleted, "La suppression d'un patient inexistant doit retourner false.");
    }

    /**
     * Teste l'initialisation du dictionnaire des spécialités.
     * Vérifie que les spécialités sont bien chargées avec leurs correspondances en service ID.
     */
    @Test
    void testSpecialityDictionaryInitialization() {
        // Vérifie que le dictionnaire des spécialités est initialisé et contient les valeurs attendues.
        Map<String, Integer> dictionary = patientService.getSpecialityDictionary();

        assertNotNull(dictionary, "Le dictionnaire des spécialités ne doit pas être null.");
        assertFalse(dictionary.isEmpty(), "Le dictionnaire des spécialités ne doit pas être vide.");
        assertEquals(1, dictionary.get("Anesthésie"), "La spécialité 'Anesthésie' doit avoir un service ID de 1.");
    }

    /**
     * Teste l'initialisation d'un patient sans ID.
     * Vérifie qu'un identifiant unique est généré si aucun ID n'est défini.
     */
    @Test
    void testInitializePatient_GeneratesUUIDIfIdIsNull() {
        // Créer un patient sans définir d'ID
        Patient patient = new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);

        // Laisser l'ID à sa valeur par défaut (pas besoin de définir explicitement null)

        // Initialiser le patient
        Patient initializedPatient = patientService.initializePatient(patient);

        // Vérifier que l'ID a été généré
        assertNotNull(initializedPatient.getId(), "Un ID unique doit être généré si l'ID du patient est null.");
        assertEquals(5, initializedPatient.getServiceId(), "Le service ID doit correspondre à la spécialité.");
    }

    /**
     * Teste l'initialisation d'un patient avec un ID existant.
     * Vérifie que l'ID existant n'est pas remplacé.
     */
    @Test
    void testInitializePatient_RetainsExistingId() {
        // Créer un patient avec un ID existant
        UUID existingId = UUID.randomUUID();
        Patient patient = new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        patient.setId(existingId); // Définir un ID existant

        // Initialiser le patient
        Patient initializedPatient = patientService.initializePatient(patient);

        // Vérifier que l'ID reste le même
        assertEquals(existingId, initializedPatient.getId(), "L'ID existant doit être conservé.");
        assertEquals(5, initializedPatient.getServiceId(), "Le service ID doit correspondre à la spécialité.");
    }

    /**
     * Teste la limite de mémoire pour le stockage des patients.
     * Vérifie que le nombre maximum de patients en mémoire est limité à 1000.
     */
    @Test
    void testMemoryLimit() {
        for (int i = 0; i < 1100; i++) {
            patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
        }
        assertEquals(1000, patientService.getAllPatients().size(), "Le nombre de patients doit être limité à 1000.");
    }
}
