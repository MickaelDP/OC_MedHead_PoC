package com.medHead.poc.testIntegration.controller;

import com.medHead.poc.model.Patient;
import com.medHead.poc.services.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import java.util.UUID;

@SpringBootTest                                                                                         // Charge le contexte complet de Spring
@AutoConfigureMockMvc
public class PatientControllerITest {

    @Autowired
    private MockMvc mockMvc;                                                                            // Simule les appels HTTP

    @Autowired
    private PatientService patientService;                                                              // Accès direct au service

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        patientService.clearPatients(); // Réinitialise les patients
        mockServer = MockRestServiceServer.createServer(restTemplate);
        Locale.setDefault(Locale.US);
    }

    /**
     * Teste US locale
     */
    @Test
    void checkDefaultLocale() {
        assertEquals(Locale.US, Locale.getDefault(), "La Locale par défaut n'est pas définie sur US.");
    }

    /**
     * Teste-le endpoint POST /api/patients.
     * Vérifie que le patient est créé avec succès.
     */
    @Test
    void testCreatePatient() throws Exception {
        String patientJson = """
            {
                "specialite": "Cardiologie",
                "responsable": "Dr. Smith",
                "qualite": "Qualité A",
                "latitude": 48.8566,
                "longitude": 2.3522
            }
        """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isCreated())                                                       // Vérifie le statut HTTP 201
                .andExpect(jsonPath("$.specialite").value("Cardiologie"))        // Vérifie la spécialité
                .andExpect(jsonPath("$.responsable").value("Dr. Smith"));        // Vérifie le responsable
    }

    /**
     * Teste-le endpoint POST /api/patients avec une spécialité invalide.
     * Vérifie que l'API retourne un statut HTTP 400 (Bad Request) et un message d'erreur approprié
     * lorsque la spécialité fournie dans la requête est inconnue du système.
     */
    @Test
    void testCreatePatient_InvalidSpeciality() throws Exception {
        String invalidPatientJson = """
        {
            "specialite": "SpécialitéInconnue",
            "responsable": "Dr. Smith",
            "qualite": "Qualité A",
            "latitude": 48.8566,
            "longitude": 2.3522
        }
    """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatientJson))
                .andExpect(status().isBadRequest())                                                     // Vérifie que le statut HTTP 400 est retourné
                .andExpect(content().string("Spécialité inconnue : SpécialitéInconnue")); // Vérifie le message d'erreur
    }

    /**
     * Teste-le endpoint GET /api/patients.
     * Vérifie que la liste des patients est retournée correctement.
     */
    @Test
    void testGetAllPatients() throws Exception {
        // Ajout d'un patient via le service
        patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())                                                            // Vérifie le statut HTTP 200
                .andExpect(jsonPath("$.length()").value(1))                      // Vérifie qu'il y a un patient
                .andExpect(jsonPath("$[0].specialite").value("Cardiologie"));    // Vérifie la spécialité du premier patient
    }

    /**
     * Teste-le endpoint DELETE /api/patients/{id}.
     * Vérifie qu'un patient est supprimé avec succès.
     */
    @Test
    void testDeletePatient() throws Exception {
        // Ajout d'un patient via le service
        Patient patient = patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));

        mockMvc.perform(delete("/api/patients/" + patient.getId()))
                .andExpect(status().isOk())                                                            // Vérifie le statut HTTP 200
                .andExpect(content().string("Patient supprime avec succes."));           // Vérifie le message de succès
    }

    /**
     * Teste-le endpoint GET /api/patients/{id} avec un ID inexistant.
     * Vérifie que l'API retourne un statut HTTP 404 (Not Found) lorsque l'ID du patient
     * fourni dans la requête ne correspond à aucun patient enregistré.
     */
    @Test
    void testGetPatientById_NotFound() throws Exception {
        // Utiliser un UUID valide mais inexistant
        String nonExistentId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/patients/" + nonExistentId))
                .andExpect(status().isNotFound()); // Vérifie que le statut HTTP 404 est retourné
    }

    /**
     * Teste-le "endpoint" POST /api/patients/process.
     * Vérifie que le traitement d'un patient génère un résultat valide.
     */
    @Test
    void testProcessPatient() throws Exception {
        // Configurer une réponse simulée pour l'API externe, attention usage de ',' pour des raisons de localisation
        mockServer.expect(requestTo("http://localhost:8080/api/hospitals?serviceId=5&lat=48.856600&lon=2.352200"))
                .andRespond(withSuccess("""
            [
                {
                    "nom": "Hopital A",
                    "latitude": 48.8566,
                    "longitude": 2.3522,
                    "serviceId": [5],
                    "nombreLitDisponible": 3
                }
            ]
        """, MediaType.APPLICATION_JSON));


        // Effectuer le test
        String patientJson = """
            {
                "specialite": "Cardiologie",
                "responsable": "Dr. Smith",
                "qualite": "Qualité A",
                "latitude": 48.8566,
                "longitude": 2.3522
            }
        """;

        mockMvc.perform(post("/api/patients/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isOk())                                      // Vérifie que le statut est 200
                .andExpect(jsonPath("$.specialite").value("Cardiologie"))       // Vérifie la spécialité
                .andExpect(jsonPath("$.hopitalNom").value("Hopital A"))         // Vérifie le nom de l'hôpital
                .andExpect(jsonPath("$.litReserve").value(true));               // Vérifie si un lit a été réservé
    }

    @Test
    void testProcessPatient_NoHospitalFound() throws Exception {
        mockServer.expect(requestTo("http://localhost:8080/api/hospitals?serviceId=5&lat=48.856600&lon=2.352200"))
                .andRespond(withServerError());

        // Effectuer le test
        String patientJson = """
            {
                "specialite": "Cardiologie",
                "responsable": "Dr. Smith",
                "qualite": "Qualité A",
                "latitude": 48.8566,
                "longitude": 2.3522
            }
        """;

        // Exécute la requête et vérifie le statut 204
        mockMvc.perform(post("/api/patients/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isNoContent()); // Vérifie que le statut HTTP est 204
    }
}
