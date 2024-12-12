package com.medHead.poc.testIntegration.controller;

import com.medHead.poc.model.Patient;
import com.medHead.poc.services.PatientServiceInterface;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Locale;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerITest {

    @Value("${external.api.url}")
    private String apiUrl;

    @Value("${jwt.fixed-token}")
    private String fixedToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientServiceInterface patientService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private Cookie csrfCookie;
    private String csrfToken;
    private MvcResult result;

    @BeforeEach
    void setUp() throws Exception {
        patientService.clearPatients();                                                      // Réinitialise les patients
        mockServer = MockRestServiceServer.createServer(restTemplate);
        Locale.setDefault(Locale.US);

        // Étape 1 : Récupérer le cookie CSRF
        result = mockMvc.perform(get("/test/csrf"))
                .andExpect(status().isOk())
                .andReturn();

        // Récupérer le cookie CSRF de la réponse
        csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        if (csrfCookie != null) {
            csrfToken = csrfCookie.getValue();  // Stocke le token CSRF
        }

        csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        csrfToken = result.getResponse().getContentAsString();
    }


    @Test
    void checkDefaultLocale() {
        assertEquals(Locale.US, Locale.getDefault(), "La Locale par défaut n'est pas définie sur US.");
    }

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
                        .cookie(csrfCookie)                                     // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specialite").value("Cardiologie"))
                .andExpect(jsonPath("$.responsable").value("Dr. Smith"));
    }

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
                        .cookie(csrfCookie)                                     // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatientJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Spécialité inconnue : SpécialitéInconnue"));
    }

    @Test
    void testGetAllPatients() throws Exception {
        patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));

        mockMvc.perform(get("/api/patients")
                        .header("Authorization", "Bearer " + fixedToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].specialite").value("Cardiologie"));
    }

    @Test
    void testDeletePatient() throws Exception {
        Patient patient = patientService.savePatient(new Patient("Cardiologie", "Dr. Smith", "Qualité A", 48.8566, 2.3522));

        mockMvc.perform(delete("/api/patients/" + patient.getId())
                        .cookie(csrfCookie)                              // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient supprime avec succes."));
    }

    @Test
    void testGetPatientById_NotFound() throws Exception {
        String nonExistentId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/patients/" + nonExistentId)
                        .header("Authorization", "Bearer " + fixedToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testProcessPatient() throws Exception {
        mockServer.expect(requestTo(apiUrl + "/hospitals?serviceId=5&lat=48.856600&lon=2.352200"))
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
                        .cookie(csrfCookie)                              // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialite").value("Cardiologie"))
                .andExpect(jsonPath("$.hopitalNom").value("Hopital A"))
                .andExpect(jsonPath("$.litReserve").value(true));
    }

    @Test
    void testProcessPatient_NoHospitalFound() throws Exception {
        mockServer.expect(requestTo(apiUrl + "/hospitals?serviceId=5&lat=48.856600&lon=2.352200"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

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
                        .cookie(csrfCookie)                                     // Réinjecte le cookie CSRF
                        .header("X-XSRF-TOKEN", csrfToken)                // Transmet le token dans le header
                        .header("Authorization", "Bearer " + fixedToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isNoContent());
    }
}
