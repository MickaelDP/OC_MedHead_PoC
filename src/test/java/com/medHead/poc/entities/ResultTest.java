package com.medHead.poc.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour l'entité Result.
 * Vérifie la création d'un résultat, l'utilisation des getters et setters,
 * ainsi que la génération d'ID unique.
 */
public class ResultTest {

    /**
     * Teste la création d'un résultat avec le constructeur paramétré.
     * Vérifie que les valeurs initialisées correspondent aux attentes.
     */
    @Test
    void testResultCreation() {
        Result result = new Result(1L, "Urgence", "Hopital Central", 15, true, true);

        assertEquals(1L, result.getPatientId());
        assertEquals("Urgence", result.getSpecialite());
        assertEquals("Hopital Central", result.getHopitalNom());
        assertEquals(15, result.getDelai());
        assertTrue(result.isSpecialiteDisponible());
        assertTrue(result.isLitDisponible());
    }

    /**
     * Teste les setters et getters de l'entité Result.
     * Vérifie que les valeurs peuvent être modifiées et récupérées correctement.
     */
    @Test
    void testSettersAndGetters() {
        Result result = new Result(2L, "Pédiatrie", "Hopital B", 20, false, false);

        result.setPatientId(3L);
        result.setSpecialite("Radiologie");
        result.setHopitalNom("Hopital A");
        result.setDelai(10);
        result.setSpecialiteDisponible(true);
        result.setLitDisponible(true);

        assertEquals(3L, result.getPatientId());
        assertEquals("Radiologie", result.getSpecialite());
        assertEquals("Hopital A", result.getHopitalNom());
        assertEquals(10, result.getDelai());
        assertTrue(result.isSpecialiteDisponible());
        assertTrue(result.isLitDisponible());
    }

    /**
     * Teste la génération d'ID unique.
     * Vérifie que deux objets Result ont des ID différents.
     */
    @Test
    void testUniqueIdGeneration() {
        Result result1 = new Result(1L, "Chirurgie", "Hopital D", 30, true, false);
        Result result2 = new Result(2L, "Cardiologie", "Hopital E", 25, true, true);

        assertNotEquals(result1.getId(), result2.getId(), "Les UUID générés doivent être uniques.");

    }
}
