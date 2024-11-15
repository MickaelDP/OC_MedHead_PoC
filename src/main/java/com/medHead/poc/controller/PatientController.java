package com.medHead.poc.controller;

import com.medHead.poc.entities.Patient;
import com.medHead.poc.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour gérer les patients (CRUD).
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * Récupère tous les patients.
     * @return Une liste de tous les patients enregistrés ou un statut 204 si vide.
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();                                              // 204 No Content si la liste est vide
        }
        return ResponseEntity.ok(patients);                                                         // 200 OK avec la liste des patients
    }

    /**
     * Récupère un patient par ID.
     * @param id L'ID du patient à rechercher.
     * @return Le patient correspondant à l'ID ou un statut 404 si non trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(ResponseEntity::ok)                                                      // 200 OK si trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());                                // 404 Not Found sinon
    }


    /**
     * Crée un nouveau patient.
     * @param patient Les informations du patient fournies dans la requête.
     * @return Le patient créé avec un statut 201 Created.
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.status(201).body(savedPatient);                                       // 201 Created avec les détails du patient
    }

    /**
     * Supprime un patient par ID.
     * @param id L'ID du patient à supprimer.
     * @return Un message indiquant le succès ou l'échec de la suppression.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.deletePatient(id);
        if (deleted) {
            return ResponseEntity.ok("Patient supprimé avec succès.");                       // 200 OK pour suppression réussie
        }
        return ResponseEntity.status(404).body("Échec de la suppression : Aucun patient trouvé."); // 404 Not Found si non trouvé
    }
}
