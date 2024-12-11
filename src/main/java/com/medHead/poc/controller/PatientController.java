package com.medHead.poc.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.model.Patient;
import com.medHead.poc.model.Result;
import com.medHead.poc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Contrôleur REST pour la gestion des patients (CRUD).
 */
@CrossOrigin(origins = "http://localhost:4200")
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


    @Autowired
    public PatientController(
            PatientService patientService,
            PopulateHopitalService populateHopitalService,
            GPSService gpsService,
            HopitalService hopitalService,
            ReserveService reserveService,
            ResultService resultService) {
        this.patientService = patientService;
        this.populateHopitalService = populateHopitalService;
        this.gpsService = gpsService;
        this.hopitalService = hopitalService;
        this.reserveService = reserveService;
        this.resultService = resultService;
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
        try {
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
            // Créer un thread pool avec un nombre fixe de threads
            int nThreads = 10;
            try (ExecutorService executorService = Executors.newFixedThreadPool(nThreads)) {
                List<Future<Void>> futures = new ArrayList<>();
                // Soumettre chaque tâche de calcul dans le pool
                for (Hopital hopital : hopitaux) {
                    final Patient localPatient = patient;
                    futures.add(executorService.submit(() -> {
                        try {
                            int delai = gpsService.getTravelDelay(
                                    localPatient.getLatitude(),
                                    localPatient.getLongitude(),
                                    hopital.getLatitude(),
                                    hopital.getLongitude()
                            );
                            hopital.setDelai(delai);
                        } catch (Exception e) {
                            e.printStackTrace();  // Afficher l'erreur dans les logs
                        }
                        return null; // Nous retournons null car il s'agit d'une tâche void
                    }));
                }
                // Attendre que toutes les tâches soient terminées
                for (Future<Void> future : futures) {
                    future.get(); // Ceci bloque jusqu'à ce que la tâche soit terminée
                }
                // Fermer le pool de threads
                executorService.shutdown();
            }

            // 4. Trier les hopitaux par delais et lits disponibles
            List<Hopital> sortedHospitals = hopitalService.trierHopitauxParDelaiEtLits(hopitaux);

            // 5. Tenter une reservation
            Hopital reservedHospital = sortedHospitals.stream()
                    .filter(hopital -> reserveService.reserveBed(hopital, true))
                    .findFirst()
                    .orElse(null);

            boolean bedReserved = reservedHospital != null;

            // 6. Fallback sur le premier hopital si aucune reservation n'a reussi
            if (reservedHospital == null && !sortedHospitals.isEmpty()) {
                reservedHospital = sortedHospitals.get(0);
                logger.info("Fallback sur l'hôpital : {}", reservedHospital.getNom());
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

            //8. Retourner le resultat
            logger.info("Resultat genere pour le patient : {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors du traitement du patient : {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
