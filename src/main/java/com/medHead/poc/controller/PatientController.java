package com.medHead.poc.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.model.Patient;
import com.medHead.poc.model.Result;
import com.medHead.poc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des patients (CRUD).
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;
    private final PopulateHopitalService populateHopitalService;
    private final GPSService gpsService;
    private final HopitalService hopitalService;
    private final ReserveService reserveService;
    private final ResultService resultService;
    private final PatientTrackerService tracker;

    @Autowired
    public PatientController(
            PatientService patientService,
            PopulateHopitalService populateHopitalService,
            GPSService gpsService,
            HopitalService hopitalService,
            ReserveService reserveService,
            ResultService resultService,
            PatientTrackerService tracker) {
        this.patientService = patientService;
        this.populateHopitalService = populateHopitalService;
        this.gpsService = gpsService;
        this.hopitalService = hopitalService;
        this.reserveService = reserveService;
        this.resultService = resultService;
        this.tracker = tracker;
    }

    /**
     * Récupère tous les patients.
     * @return Une liste de tous les patients enregistrés ou un statut HTTP 204 si aucun n'existe.
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }

    /**
     * Récupère un patient par ID.
     * @param id L'ID du patient à rechercher.
     * @return Le patient correspondant à l'ID ou un statut HTTP 404 s'il n'est pas trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable UUID id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau patient.
     * @param patient Les informations du patient fournies dans la requête.
     * @return Le patient créé avec un statut HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.status(201).body(savedPatient);
    }

    /**
     * Supprime un patient par ID.
     * @param id L'ID du patient à supprimer.
     * @return Un message indiquant le succès ou l'échec de la suppression.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        boolean deleted = patientService.deletePatient(id);
        if (deleted) {
            return ResponseEntity.ok("Patient supprime avec succes.");
        }
        return ResponseEntity.status(404).body("Echec de la suppression : Patient non trouve.");
    }

    /**
     * Endpoint pour traiter un patient et générer un résultat.
     * @param patient Les informations du patient fournies dans la requête.
     * @return Le résultat généré après traitement en réponse.
     */
    @PostMapping("/process")
    public ResponseEntity<Result> processPatient(@RequestBody Patient patient) {
        UUID patientId = patient.getId();

        try {
            // Verifier si le patient est deja en cours de traitement
            if (!tracker.startProcessing(patientId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            // 1. Initialiser le patient
            patient = patientService.initializePatient(patient);
            logger.info("Patient initialized: {}", patient);

            // 2. Recuperer les hopitaux correspondants
            List<Hopital> hopitaux = populateHopitalService.getHospitalsByServiceId(
                    patient.getServiceId(),
                    patient.getLatitude(),
                    patient.getLongitude()
            );

            if (hopitaux.isEmpty()) {
                logger.warn("Aucun hopital trouve pour le service demande par le patient.");
                return ResponseEntity.noContent().build();
            }

            // 3. Calculer les delais pour chaque hopital
            for (Hopital hopital : hopitaux) {
                int delai = gpsService.getTravelDelay(
                        patient.getLatitude(),
                        patient.getLongitude(),
                        hopital.getLatitude(),
                        hopital.getLongitude()
                );
                hopital.setDelai(delai);
            }

            // 4. Trier les hopitaux par delais et lits disponibles
            List<Hopital> sortedHospitals = hopitalService.trierHopitauxParDelaiEtLits(hopitaux);

            // 5. Tenter une reservation
            Hopital reservedHospital = null;
            boolean bedReserved = false;

            for (Hopital hopital : sortedHospitals) {
                if (reserveService.reserveBed(hopital, true)) {
                    reservedHospital = hopital;
                    bedReserved = true;
                    logger.info("Reservation reussie pour l'hopital : {}", hopital.getNom());
                    break;
                } else {
                    logger.warn("Echec de la reservation pour l'hopital : {}", hopital.getNom());
                }
            }

            // 6. Fallback sur le premier hopital si aucune reservation n'a reussi
            if (reservedHospital == null) {
                reservedHospital = sortedHospitals.getFirst();
                logger.info("Fallback sur l'hopital : {}", reservedHospital.getNom());
            }

            // 7. Creer un resultat
            Result result = new Result(
                    patient.getId(),
                    patient.getSpecialite(),
                    reservedHospital.getNom(),
                    reservedHospital.getDelai(),
                    true,
                    bedReserved
            );

            // 8. Sauvegarder le resultat
            resultService.saveResult(result);

            // 9. Nettoyer les donnees du patient
            patientService.deletePatient(patientId);

            // 10. Retourner le resultat
            logger.info("Resultat genere pour le patient : {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors du traitement du patient : {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        } finally {
            // Supprimer l'ID du patient du tracker
            tracker.endProcessing(patientId);
        }
    }
}
