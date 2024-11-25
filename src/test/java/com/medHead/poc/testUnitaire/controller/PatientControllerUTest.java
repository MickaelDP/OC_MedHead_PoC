package com.medHead.poc.testUnitaire.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.controller.PatientController;
import com.medHead.poc.model.Patient;
import com.medHead.poc.model.Result;
import com.medHead.poc.services.PatientService;
import com.medHead.poc.services.PopulateHopitalService;
import com.medHead.poc.services.GPSService;
import com.medHead.poc.services.HopitalService;
import com.medHead.poc.services.ReserveService;
import com.medHead.poc.services.ResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Classe de test unitaire pour le contrôleur PatientController.
 */
public class PatientControllerUTest {

    @Mock
    private PatientService patientService;

    @Mock
    private PopulateHopitalService populateHopitalService;

    @Mock
    private GPSService gpsService;

    @Mock
    private HopitalService hopitalService;

    @Mock
    private ReserveService reserveService;


    @Mock
    private ResultService resultService;

    @InjectMocks
    private PatientController patientController;                                    // Injecte les mocks dans le contrôleur


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);                                // Initialise les mocks
    }


    /**
     * Teste le endpoint POST /api/patients.
     * Vérifie que le contrôleur retourne un statut HTTP 201 (Created)
     * et le patient sauvegardé fourni par le service.
     */
    @Test
    void testCreatePatient() {
        // Mock du service
        Patient newPatient = new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        when(patientService.savePatient(any(Patient.class))).thenReturn(newPatient);

        // Appel au contrôleur
        ResponseEntity<Patient> response = patientController.createPatient(newPatient);

        // Assertions
        assertEquals(CREATED, response.getStatusCode(), "Le statut HTTP doit être 201.");
        assertEquals(newPatient, response.getBody(), "Le patient créé doit correspondre.");
    }

    /**
     * Teste le endpoint pour récupérer tous les patients.
     * Vérifie que le contrôleur retourne un code HTTP 200 (OK)
     * et la liste complète des patients fournie par le service.
     */
    @Test
    void testGetAllPatients() {
        // Mock du service
        List<Patient> patients = Arrays.asList(
                new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522),
                new Patient("Pédiatrie", "Dr. Jones", "Qualité B", 34.0522, -118.2437)
        );
        when(patientService.getAllPatients()).thenReturn(patients);

        // Appel au contrôleur
        ResponseEntity<List<Patient>> response = patientController.getAllPatients();

        // Assertions
        assertEquals(OK, response.getStatusCode(), "Le statut HTTP doit être 200.");
        assertEquals(2, response.getBody().size(), "La réponse doit contenir 2 patients.");
    }

    /**
     * Teste le endpoint GET /api/patients/{id} pour un patient existant.
     * Vérifie que le contrôleur retourne un statut HTTP 200 (OK)
     * et les informations correctes du patient.
     */
    @Test
    void testGetPatientById_Success() {
        // Mock du service pour retourner un patient spécifique
        UUID patientId = UUID.randomUUID();
        Patient mockPatient = new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        when(patientService.getPatientById(patientId)).thenReturn(Optional.of(mockPatient));

        // Appel du contrôleur
        ResponseEntity<Patient> response = patientController.getPatientById(patientId);

        // Assertions
        assertEquals(OK, response.getStatusCode(), "Le statut HTTP doit être 200.");
        assertEquals(mockPatient, response.getBody(), "Le patient retourné doit correspondre.");
    }

    /**
     * Teste le endpoint pour récupérer un patient par ID inexistant.
     * Vérifie que le contrôleur retourne un code HTTP 404 (NOT FOUND)
     * et un corps de réponse null.
     */
    @Test
    void testGetPatientById_NotFound() {
        // Mock du service pour retourner un Optional vide
        UUID unknownId = UUID.randomUUID();
        when(patientService.getPatientById(unknownId)).thenReturn(Optional.empty());

        // Appel au contrôleur
        ResponseEntity<Patient> response = patientController.getPatientById(unknownId);

        // Assertions
        assertEquals(NOT_FOUND, response.getStatusCode(), "Le statut HTTP doit être 404.");
        assertNull(response.getBody(), "Le corps de la réponse doit être null.");
    }

    /**
     * Teste le endpoint DELETE /api/patients/{id} pour un patient existant.
     * Vérifie que le contrôleur retourne un statut HTTP 200 (OK)
     * et un message de succès.
     */
    @Test
    void testDeletePatient_Success() {
        // Mock du service
        UUID patientId = UUID.randomUUID();
        when(patientService.deletePatient(patientId)).thenReturn(true);

        // Appel au contrôleur
        ResponseEntity<String> response = patientController.deletePatient(patientId);

        // Assertions
        assertEquals(OK, response.getStatusCode(), "Le statut HTTP doit être 200.");
        assertEquals("Patient supprime avec succes.", response.getBody(), "Le message de succès doit correspondre.");
    }

    /**
     * Teste le endpoint DELETE /api/patients/{id} pour un patient inexistant.
     * Vérifie que le contrôleur retourne un statut HTTP 404 (Not Found)
     * et un message d'échec.
     */
    @Test
    void testDeletePatient_NotFound() {
        // Mock du service
        UUID unknownId = UUID.randomUUID();
        when(patientService.deletePatient(unknownId)).thenReturn(false);

        // Appel au contrôleur
        ResponseEntity<String> response = patientController.deletePatient(unknownId);

        // Assertions
        assertEquals(NOT_FOUND, response.getStatusCode(), "Le statut HTTP doit être 404.");
        assertEquals("Echec de la suppression : Patient non trouve.", response.getBody(), "Le message d'échec doit correspondre.");
    }

    /**
     * Teste le traitement complet d'un patient avec succès.
     * Vérifie les interactions entre les services et la création d'un résultat.
     */
    @Test
    void testProcessPatient_Success() {
        // Données simulées pour le patient
        Patient patient = new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        patient.setId(UUID.randomUUID());

        // Données simulées pour les hôpitaux
        List<Hopital> hopitaux = new ArrayList<>();
        Hopital hopital1 = new Hopital("Hopital A", List.of(1, 2, 5), 48.8567, 2.3523, 5);
        Hopital hopital2 = new Hopital("Hopital B", List.of(1, 2, 5), 48.8570, 2.3530, 2);
        hopitaux.add(hopital1);
        hopitaux.add(hopital2);

        // Mocks pour chaque étape
        when(patientService.initializePatient(any(Patient.class))).thenReturn(patient);
        when(populateHopitalService.getHospitalsByServiceId(anyInt(), anyDouble(), anyDouble())).thenReturn(hopitaux);
        when(gpsService.getTravelDelay(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(10); // Tous les hôpitaux ont un délai simulé de 10 minutes
        when(hopitalService.trierHopitauxParDelaiEtLits(anyList())).thenReturn(hopitaux);
        when(reserveService.reserveBed(any(Hopital.class), anyBoolean())).thenReturn(true);

        // Appel du contrôleur
        ResponseEntity<Result> response = patientController.processPatient(patient);

        // Vérifications
        assertEquals(OK, response.getStatusCode(), "Le statut HTTP doit être 200.");
        assertNotNull(response.getBody(), "Le résultat ne doit pas être null.");
        assertEquals(patient.getId(), response.getBody().getPatientId(), "L'ID du patient doit correspondre.");
        assertTrue(response.getBody().isLitReserve(), "Un lit doit avoir été réservé.");
    }

    /**
     * Teste le traitement d'un patient lorsque aucun hôpital n'est trouvé.
     * Vérifie que le contrôleur retourne un statut HTTP 204 (No Content).
     */
    @Test
    void testProcessPatient_NoHospitalsFound() {
        // Données simulées pour le patient
        Patient patient = new Patient("Chirurgie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        patient.setId(UUID.randomUUID());

        // Mocks pour les services
        when(patientService.initializePatient(any(Patient.class))).thenReturn(patient);
        when(populateHopitalService.getHospitalsByServiceId(anyInt(), anyDouble(), anyDouble()))
                .thenReturn(new ArrayList<>()); // Aucune liste d'hôpitaux

        // Appel du contrôleur
        ResponseEntity<Result> response = patientController.processPatient(patient);

        // Vérifications
        assertEquals(NO_CONTENT, response.getStatusCode(), "Le statut HTTP doit être 204.");
        assertNull(response.getBody(), "Le corps de la réponse doit être null.");
    }

    /**
     * Teste le traitement d'un patient lorsqu'une exception inattendue se produit.
     * Vérifie que le contrôleur retourne un statut HTTP 500 (Internal Server Error).
     */
    @Test
    void testProcessPatient_ExceptionThrown() {
        // Préparation des données simulées pour le patient
        Patient patient = new Patient("Radiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522);
        UUID patientId = UUID.randomUUID();
        patient.setId(patientId);

        // Appel du contrôleur
        ResponseEntity<Result> response = patientController.processPatient(patient);

        // Vérifications
        assertNotNull(response, "La réponse ne doit pas être null.");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Le statut HTTP doit être 500.");
        assertNull(response.getBody(), "Le corps de la réponse doit être null.");
    }
}
