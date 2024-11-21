package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.services.GPSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour GPSService.
 */
public class GPSServiceUTest {
    private GPSService gpsService;

    @BeforeEach
    void setUp() {
        // Initialise le service GPS avant chaque test
        gpsService = new GPSService();
    }

    /**
     * Teste si le délai de trajet généré est dans les limites définies.
     */
    @Test
    void testGetTravelDelayWithinRange() {
        // Paramètres fictifs
        double patientLat = 48.8566;
        double patientLon = 2.3522;
        double hospitalLat = 48.8600;
        double hospitalLon = 2.3260;

        // Appelle la méthode plusieurs fois pour vérifier la plage
        for (int i = 0; i < 100; i++) { // Vérifie sur plusieurs itérations pour valider l'aléatoire
            int delay = gpsService.getTravelDelay(patientLat, patientLon, hospitalLat, hospitalLon);
            assertTrue(delay >= 5 && delay <= 30, "Le délai doit être entre 5 et 30 minutes.");
        }
    }

    /**
     * Teste si le service gère correctement des coordonnées valides.
     */
    @Test
    void testGetTravelDelayWithValidCoordinates() {
        // Paramètres fictifs
        double patientLat = 40.7128;
        double patientLon = -74.0060;
        double hospitalLat = 34.0522;
        double hospitalLon = -118.2437;

        // Appelle la méthode et vérifie que le délai est dans la plage
        int delay = gpsService.getTravelDelay(patientLat, patientLon, hospitalLat, hospitalLon);
        assertTrue(delay >= 5 && delay <= 30, "Le délai doit être entre 5 et 30 minutes.");
    }

    /**
     * Teste si le service gère correctement des coordonnées identiques.
     */
    @Test
    void testGetTravelDelayWithIdenticalCoordinates() {
        // Coordonnées identiques pour le patient et l'hôpital
        double lat = 48.8566;
        double lon = 2.3522;

        // Appelle la méthode et vérifie que le délai est dans la plage
        int delay = gpsService.getTravelDelay(lat, lon, lat, lon);
        assertTrue(delay >= 5 && delay <= 30, "Le délai doit être entre 5 et 30 minutes même pour des coordonnées identiques.");
    }
}
