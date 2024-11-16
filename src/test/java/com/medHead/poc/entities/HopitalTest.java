package com.medHead.poc.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour l'entité Hopital.
 * Vérifie la création d'un hôpital, l'utilisation des setters et getters,
 * ainsi que la valeur par défaut du délai.
 */
public class HopitalTest {

    /**
     * Teste la création d'un hôpital avec un constructeur paramétré.
     * Vérifie que les valeurs initialisées correspondent aux attentes.
     */
    @Test
    void testHopitalCreation() {
        Hopital hopital = new Hopital("Hopital Central", List.of(1, 2, 3), 48.8566, 2.3522, 10);

        assertEquals("Hopital Central", hopital.getNom());
        assertEquals(3, hopital.getServiceIdsDisponibles().size());
        assertEquals(48.8566, hopital.getLatitude());
        assertEquals(2.3522, hopital.getLongitude());
        assertEquals(10, hopital.getNombreLitDisponible());
    }

    /**
     * Teste les setters et getters de l'entité Hopital.
     * Vérifie que les valeurs peuvent être modifiées et récupérées correctement.
     */
    @Test
    void testSettersAndGetters() {
        Hopital hopital = new Hopital();
        hopital.setNom("Hopital B");
        hopital.setServiceIdsDisponibles(List.of(4, 5));
        hopital.setLatitude(40.7128);
        hopital.setLongitude(-74.0060);
        hopital.setNombreLitDisponible(5);

        assertEquals("Hopital B", hopital.getNom());
        assertEquals(2, hopital.getServiceIdsDisponibles().size());
        assertEquals(40.7128, hopital.getLatitude());
        assertEquals(-74.0060, hopital.getLongitude());
        assertEquals(5, hopital.getNombreLitDisponible());
    }

    /**
     * Teste la valeur par défaut de l'attribut 'delai'.
     * Vérifie que le délai par défaut est correctement initialisé à 9999.
     */
    @Test
    void testDefaultDelai() {
        Hopital hopital = new Hopital();
        assertEquals(9999, hopital.getDelai());
    }
}
