package com.medHead.poc.testUnitaire.model;

import com.medHead.poc.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Classe de test pour l'entité Patient.
 * Vérifie les getters et setters pour s'assurer que les valeurs des attributs
 * peuvent être modifiées et récupérées correctement.
 */
public class PatientUTest {

    /**
     * Teste le constructeur paramétré de la classe Patient.
     * Vérifie que le constructeur initialise correctement les champs de l'objet Patient avec les valeurs fournies.
     */
    @Test
    void testConstructorWithParameters() {
        Patient patient = new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522);

        assertNotNull(patient.getId());
        assertEquals("Urgence", patient.getSpecialite());
        assertEquals("Dr. Smith", patient.getResponsable());
        assertEquals("Qualité A", patient.getQualite());
        assertEquals(48.8566, patient.getLatitude());
        assertEquals(2.3522, patient.getLongitude());
        assertEquals(0, patient.getServiceId()); // Par défaut
    }

    /**
     * Teste les getters et setters de l'entité Patient avec des valeurs valides.
     * Vérifie que les valeurs peuvent être modifiées et récupérées comme attendu.
     */
    @Test
    void testPatientGettersAndSetters() {
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setServiceId(2);
        patient.setSpecialite("Urgence");
        patient.setResponsable("Dr. Smith");
        patient.setQualite("Qualité A");
        patient.setLatitude(48.8566);
        patient.setLongitude(2.3522);

        assertEquals(patientId, patient.getId());
        assertEquals(2, patient.getServiceId());
        assertEquals("Urgence", patient.getSpecialite());
        assertEquals("Dr. Smith", patient.getResponsable());
        assertEquals("Qualité A", patient.getQualite());
        assertEquals(48.8566, patient.getLatitude());
        assertEquals(2.3522, patient.getLongitude());
    }

    /**
     * Teste les validations des setters pour des valeurs invalides.
     * Vérifie que les exceptions sont levées comme attendu.
     */
    @Test
    void testPatientSettersInvalidValues() {
        Patient patient = new Patient();

        // Test d'ID invalide
        Exception idException = assertThrows(IllegalArgumentException.class, () -> patient.setId(null));
        assertEquals("L'ID doit être un entier positif non nul.", idException.getMessage());

        // Test de ServiceId invalide
        Exception serviceIdException = assertThrows(IllegalArgumentException.class, () -> patient.setServiceId(0));
        assertEquals("L'ID du service doit être positif.", serviceIdException.getMessage());

        // Test de latitude invalide
        Exception latitudeException = assertThrows(IllegalArgumentException.class, () -> patient.setLatitude(-100));
        assertEquals("La latitude doit être comprise entre -90 et 90.", latitudeException.getMessage());

        // Test de longitude invalide
        Exception longitudeException = assertThrows(IllegalArgumentException.class, () -> patient.setLongitude(200));
        assertEquals("La longitude doit être comprise entre -180 et 180.", longitudeException.getMessage());

        // Test de spécialité vide
        Exception specialiteException = assertThrows(IllegalArgumentException.class, () -> patient.setSpecialite(""));
        assertEquals("La spécialité ne peut pas être vide ou nulle.", specialiteException.getMessage());

        // Test de responsable null
        Exception responsableException = assertThrows(IllegalArgumentException.class, () -> patient.setResponsable(null));
        assertEquals("Le nom du responsable ne peut pas être vide ou nul.", responsableException.getMessage());
    }

    /**
     * Teste la méthode toString de la classe Patient.
     * Vérifie que la représentation textuelle de l'objet Patient correspond aux attentes, en incluant tous les champs de l'objet.
     */
    @Test
    void testToString() {
        Patient patient = new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        String expectedString = "Patient{id=" + patient.getId() +
                ", serviceId=0, specialite='Urgence', responsable='Dr. Smith', qualite='Qualité A', latitude=48.8566, longitude=2.3522}";
        assertEquals(expectedString, patient.toString());
    }


}
