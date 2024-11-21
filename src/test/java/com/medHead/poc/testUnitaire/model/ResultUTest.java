package com.medHead.poc.testUnitaire.model;


import com.medHead.poc.model.Result;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour l'entité Result.
 * Vérifie la création d'un résultat, l'utilisation des getters et setters,
 * ainsi que la gestion des exceptions pour les validations.
 */
public class ResultUTest {

    /**
     * Teste la création d'un résultat avec le constructeur paramétré.
     * Vérifie que les valeurs initialisées correspondent aux attentes.
     */
    @Test
    void testResultCreation() {
        UUID patientId = UUID.randomUUID();
        Result result = new Result(patientId, "Urgence", "Hopital Central", 15, true, true);

        assertEquals(patientId, result.getPatientId());
        assertEquals("Urgence", result.getSpecialite());
        assertEquals("Hopital Central", result.getHopitalNom());
        assertEquals(15, result.getDelai());
        assertTrue(result.isSpecialiteDisponible());
        assertTrue(result.isLitReserve());
    }

    /**
     * Teste les setters et getters de l'entité Result.
     * Vérifie que les valeurs peuvent être modifiées et récupérées correctement.
     */
    @Test
    void testSettersAndGetters() {
        UUID patientId = UUID.randomUUID();
        UUID newPatientId = UUID.randomUUID();
        Result result = new Result(patientId, "Pédiatrie", "Hopital B", 20, false, false);

        result.setPatientId(newPatientId);
        result.setSpecialite("Radiologie");
        result.setHopitalNom("Hopital A");
        result.setDelai(10);
        result.setSpecialiteDisponible(true);
        result.setLitReserve(true);

        assertEquals(newPatientId, result.getPatientId());
        assertEquals("Radiologie", result.getSpecialite());
        assertEquals("Hopital A", result.getHopitalNom());
        assertEquals(10, result.getDelai());
        assertTrue(result.isSpecialiteDisponible());
        assertTrue(result.isLitReserve());
    }

    /**
     * Teste la génération d'ID unique.
     * Vérifie que deux objets Result ont des ID différents.
     */
    @Test
    void testUniqueIdGeneration() {
        UUID patientId1 = UUID.randomUUID();
        UUID patientId2 = UUID.randomUUID();
        Result result1 = new Result(patientId1, "Chirurgie", "Hopital D", 30, true, false);
        Result result2 = new Result(patientId2 , "Cardiologie", "Hopital E", 25, true, true);

        assertNotEquals(result1.getId(), result2.getId(), "Les UUID générés doivent être uniques.");
    }

    /**
     * Teste les validations dans les setters.
     * Vérifie que les exceptions sont levées pour des valeurs invalides.
     */
    @Test
    void testSetterValidations() {
        Result result = new Result();

        // Validation pour patientId
        assertThrows(IllegalArgumentException.class, () -> result.setPatientId(null), "PatientId ne peut pas être null.");

        // Validation pour specialite
        assertThrows(IllegalArgumentException.class, () -> result.setSpecialite(""), "Specialite ne peut pas être vide.");
        assertThrows(IllegalArgumentException.class, () -> result.setSpecialite(null), "Specialite ne peut pas être null.");

        // Validation pour hopitalNom
        assertThrows(IllegalArgumentException.class, () -> result.setHopitalNom(""), "HopitalNom ne peut pas être vide.");
        assertThrows(IllegalArgumentException.class, () -> result.setHopitalNom(null), "HopitalNom ne peut pas être null.");

        // Validation pour delai
        assertThrows(IllegalArgumentException.class, () -> result.setDelai(-5), "Delai ne peut pas être négatif.");
    }

    /**
     * Teste les valeurs par défaut des attributs pour un nouvel objet Result.
     * Vérifie que les valeurs sont correctement initialisées.
     */
    @Test
    void testDefaultValues() {
        Result result = new Result();

        assertNull(result.getPatientId());
        assertNull(result.getSpecialite());
        assertNull(result.getHopitalNom());
        assertEquals(0, result.getDelai());
        assertFalse(result.isSpecialiteDisponible());
        assertFalse(result.isLitReserve());
    }
}