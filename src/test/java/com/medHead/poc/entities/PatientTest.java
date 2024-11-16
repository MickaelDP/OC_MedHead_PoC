package com.medHead.poc.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour l'entité Patient.
 * Vérifie les getters et setters pour s'assurer que les valeurs des attributs
 * peuvent être modifiées et récupérées correctement.
 */
public class PatientTest {

    /**
     * Teste les getters et setters de l'entité Patient.
     * Vérifie que les valeurs peuvent être modifiées et récupérées comme attendu.
     */
    @Test
    void testPatientGettersAndSetters() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setSpecialite("Urgence");
        patient.setResponsable("Dr. Smith");
        patient.setQualite("Qualité A");
        patient.setLatitude(48.8566);
        patient.setLongitude(2.3522);

        assertEquals(1L, patient.getId());
        assertEquals("Urgence", patient.getSpecialite());
        assertEquals("Dr. Smith", patient.getResponsable());
        assertEquals("Qualité A", patient.getQualite());
        assertEquals(48.8566, patient.getLatitude());
        assertEquals(2.3522, patient.getLongitude());
    }
}
