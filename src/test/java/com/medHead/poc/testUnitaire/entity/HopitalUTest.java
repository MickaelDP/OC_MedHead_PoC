package com.medHead.poc.testUnitaire.entity;

import com.medHead.poc.entity.Hopital;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour l'entité Hopital.
 * Vérifie la création, les setters/getters, les valeurs par défaut, et la gestion des exceptions.
 */
public class HopitalUTest {

    /**
     * Teste la création d'un hôpital avec un constructeur paramétré.
     * Vérifie que les valeurs initialisées correspondent aux attentes.
     */
    @Test
    void testHopitalCreation() {
        Hopital hopital = new Hopital("Hopital Central", List.of(1, 2, 3), 48.8566, 2.3522, 10);

        assertEquals("Hopital Central", hopital.getNom());
        assertEquals(List.of(1, 2, 3), hopital.getServiceIdsDisponibles());
        assertEquals(48.8566, hopital.getLatitude());
        assertEquals(2.3522, hopital.getLongitude());
        assertEquals(10, hopital.getNombreLitDisponible());
        assertEquals(9999, hopital.getDelai(), "Le délai par défaut doit être 9999.");
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
        hopital.setDelai(15);
        hopital.setId(123L);

        assertEquals("Hopital B", hopital.getNom());
        assertEquals(List.of(4, 5), hopital.getServiceIdsDisponibles());
        assertEquals(40.7128, hopital.getLatitude());
        assertEquals(-74.0060, hopital.getLongitude());
        assertEquals(5, hopital.getNombreLitDisponible());
        assertEquals(15, hopital.getDelai());
        assertEquals(123L, hopital.getId());
    }

    /**
     * Teste les validations des setters.
     * Vérifie que les exceptions sont levées pour des entrées invalides.
     */
    @Test
    void testSettersValidation() {
        Hopital hopital = new Hopital();

        // Test du nom
        IllegalArgumentException nomException = assertThrows(IllegalArgumentException.class, () -> hopital.setNom(null));
        assertEquals("Le nom de l'hôpital ne peut pas être null ou vide.", nomException.getMessage());
        IllegalArgumentException nomVideException = assertThrows(IllegalArgumentException.class, () -> hopital.setNom(""));
        assertEquals("Le nom de l'hôpital ne peut pas être null ou vide.", nomVideException.getMessage());
        // Test de la latitude
        IllegalArgumentException latitudeBasseException = assertThrows(IllegalArgumentException.class, () -> hopital.setLatitude(-91));
        assertEquals("La latitude doit être comprise entre -90 et 90.", latitudeBasseException.getMessage());
        IllegalArgumentException latitudeHauteException = assertThrows(IllegalArgumentException.class, () -> hopital.setLatitude(91));
        assertEquals("La latitude doit être comprise entre -90 et 90.", latitudeHauteException.getMessage());
        // Test de la longitude
        IllegalArgumentException longitudeBasseException = assertThrows(IllegalArgumentException.class, () -> hopital.setLongitude(-181));
        assertEquals("La longitude doit être comprise entre -180 et 180.", longitudeBasseException.getMessage());
        IllegalArgumentException longitudeHauteException = assertThrows(IllegalArgumentException.class, () -> hopital.setLongitude(181));
        assertEquals("La longitude doit être comprise entre -180 et 180.", longitudeHauteException.getMessage());
        // Test du nombre de lits disponibles
        IllegalArgumentException litsException = assertThrows(IllegalArgumentException.class, () -> hopital.setNombreLitDisponible(-1));
        assertEquals("Le nombre de lits disponibles ne peut pas être négatif.", litsException.getMessage());
        // Test du délai
        IllegalArgumentException delaiException = assertThrows(IllegalArgumentException.class, () -> hopital.setDelai(-1));
        assertEquals("Le délai ne peut pas être négatif.", delaiException.getMessage());
        // Test de la liste des services
        IllegalArgumentException servicesNullException = assertThrows(IllegalArgumentException.class, () -> hopital.setServiceIdsDisponibles(null));
        assertEquals("La liste des services disponibles ne peut pas être null ou vide.", servicesNullException.getMessage());
        IllegalArgumentException servicesVideException = assertThrows(IllegalArgumentException.class, () -> hopital.setServiceIdsDisponibles(List.of()));
        assertEquals("La liste des services disponibles ne peut pas être null ou vide.", servicesVideException.getMessage());
    }

    /**
     * Teste la méthode toString de l'entité Hopital.
     * Vérifie que la représentation sous forme de chaîne de l'objet Hopital correspond au format attendu.
     */
    @Test
    void testToString() {
        Hopital hopital = new Hopital("Hopital Central", List.of(1, 2, 3), 48.8566, 2.3522, 10);
        String expectedString = "Hopital{id=null, nom='Hopital Central', serviceIdsDisponibles=[1, 2, 3], latitude=48.8566, longitude=2.3522, nombreLitDisponible=10, delai=9999}";
        assertEquals(expectedString, hopital.toString());
    }


    /**
     * Teste l'immuabilité de la liste des IDs des services disponibles.
     * Vérifie que la liste retournée par getServiceIdsDisponibles() est immuable et ne peut pas être modifiée directement.
     */
    @Test
    void testServiceIdsImmutability() {
        Hopital hopital = new Hopital("Hopital A", List.of(1, 2, 3), 48.8566, 2.3522, 10);
        List<Integer> serviceIds = hopital.getServiceIdsDisponibles();

        assertThrows(UnsupportedOperationException.class, () -> serviceIds.add(4));
    }
}
