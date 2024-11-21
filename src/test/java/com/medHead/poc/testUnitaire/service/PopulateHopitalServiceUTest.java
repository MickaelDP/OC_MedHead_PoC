package com.medHead.poc.testUnitaire.service;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.PopulateHopitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour PopulateHopitalService.
 * Teste les comportements normaux et les cas où des exceptions doivent être levées.
 */
@SpringBootTest
@TestPropertySource(properties = "external.api.url=http://localhost:8080/api")
public class PopulateHopitalServiceUTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PopulateHopitalService populateHopitalService;

    @BeforeEach
    void setUp() {
        // Initialise les mocks
        MockitoAnnotations.openMocks(this);

        // Injecte une URL de base correcte
        populateHopitalService.setApiUrl("http://localhost:8080/api");

    }

    /**
     * Teste la récupération des hôpitaux par serviceId avec un appel API simulé.
     */
    @Test
    void testGetHospitalsByValidServiceId() {
        int serviceId = 1;
        double latitude = 48.8566;
        double longitude = 2.3522;

        // Prépare une réponse simulée
        Hopital[] mockResponse = {
                new Hopital("Hopital A", List.of(serviceId), latitude, longitude, 15),
                new Hopital("Hopital B", List.of(serviceId), latitude, longitude, 10)
        };

        String expectedUrl = String.format("http://localhost:8080/api/hospitals?serviceId=%d&lat=%.6f&lon=%.6f", serviceId, latitude, longitude);
        when(restTemplate.getForObject(expectedUrl, Hopital[].class)).thenReturn(mockResponse);

        // Appel de la méthode
        List<Hopital> hospitals = populateHopitalService.getHospitalsByServiceId(serviceId, latitude, longitude);

        // Assertions
        assertNotNull(hospitals, "La liste des hôpitaux ne doit pas être null.");
        assertEquals(2, hospitals.size(), "La liste des hôpitaux doit contenir 2 éléments.");
        assertEquals("Hopital A", hospitals.get(0).getNom());
        assertEquals("Hopital B", hospitals.get(1).getNom());

        // Vérifie que RestTemplate a été appelé une fois
        verify(restTemplate, times(1)).getForObject(expectedUrl, Hopital[].class);
    }

    /**
     * Teste la récupération des hôpitaux avec un serviceId négatif.
     * Vérifie qu'une exception est levée.
     */
    @Test
    void testGetHospitalsByNegativeServiceId() {
        int negativeServiceId = -1;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> populateHopitalService.getHospitalsByServiceId(negativeServiceId, 48.8566, 2.3522)
        );

        assertEquals("L'ID du service doit être un entier positif.", exception.getMessage());
    }

    /**
     * Teste la récupération des hôpitaux pour un serviceId inexistant.
     * Vérifie que la liste retournée est vide.
     */
    @Test
    void testGetHospitalsByNonExistentServiceId() {
        int nonExistentServiceId = 99; // Service ID qui n'existe pas
        double latitude = 48.8566;
        double longitude = 2.3522;

        // Simule une réponse vide
        String expectedUrl = String.format("http://localhost:8080/api/hospitals?serviceId=%d&lat=%.6f&lon=%.6f", nonExistentServiceId, latitude, longitude);
        when(restTemplate.getForObject(expectedUrl, Hopital[].class)).thenReturn(new Hopital[0]);

        // Appel de la méthode
        List<Hopital> hospitals = populateHopitalService.getHospitalsByServiceId(nonExistentServiceId, latitude, longitude);

        // Assertions
        assertNotNull(hospitals, "La liste des hôpitaux ne doit pas être null.");
        assertTrue(hospitals.isEmpty(), "La liste des hôpitaux doit être vide pour un service ID inexistant.");

        // Vérifie que RestTemplate a été appelé une fois
        verify(restTemplate, times(1)).getForObject(expectedUrl, Hopital[].class);
    }

    /**
     * Teste le comportement en cas de réponse null de l'API.
     * Vérifie que la liste retournée est vide.
     */
    @Test
    void testApiReturnsNull() {
        int serviceId = 1;
        double latitude = 48.8566;
        double longitude = 2.3522;

        // Simule une réponse null
        String expectedUrl = String.format("http://localhost:8080/api/hospitals?serviceId=%d&lat=%.6f&lon=%.6f", serviceId, latitude, longitude);
        when(restTemplate.getForObject(expectedUrl, Hopital[].class)).thenReturn(null);

        // Appel de la méthode
        List<Hopital> hospitals = populateHopitalService.getHospitalsByServiceId(serviceId, latitude, longitude);

        // Assertions
        assertNotNull(hospitals, "La liste des hôpitaux ne doit pas être null.");
        assertTrue(hospitals.isEmpty(), "La liste des hôpitaux doit être vide si l'API retourne null.");

        // Vérifie que RestTemplate a été appelé une fois
        verify(restTemplate, times(1)).getForObject(expectedUrl, Hopital[].class);
    }

    /**
     * Teste la validation des coordonnées avec une latitude invalide.
     */
    @Test
    void testInvalidLatitude() {
        int serviceId = 1;
        double invalidLatitude = 100.0; // Latitude invalide
        double longitude = 2.3522;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> populateHopitalService.getHospitalsByServiceId(serviceId, invalidLatitude, longitude)
        );

        assertEquals("La latitude doit être comprise entre -90 et 90.", exception.getMessage());
        verifyNoInteractions(restTemplate); // Vérifie que RestTemplate n'a pas été utilisé
    }
}
