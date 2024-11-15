package com.medHead.poc.runner;

import com.medHead.poc.entities.Patient;
import com.medHead.poc.services.PatientService;
import com.medHead.poc.entities.Hopital;
import com.medHead.poc.services.HopitalService;
import com.medHead.poc.entities.Result;
import com.medHead.poc.services.ResultService;
import com.medHead.poc.toMocke.PopulateHopitalService;
import com.medHead.poc.toMocke.ReserveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * CommandLineRunner pour tester le déroulement initial du backend.
 * Ce composant exécute une série de tests basiques au démarrage de l'application pour valider
 * les fonctionnalités principales des services et simuler un cas d'usage général.
 */
// @component -> composant Spring & CommandLineRunner execute lorsque l'application est run
@Component
public class CommandLRunner implements CommandLineRunner {

    @Autowired
    private PatientService patientService;
    @Autowired
    private HopitalService hopitalService;
    @Autowired
    private PopulateHopitalService populateHopitalService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private ReserveService reserveService;

    /**
     * Variable pour activer ou désactiver l'exécution du runner.
     */
    private final boolean ENABLE_RUNNER = true;

    @Override
    public void run(String... args) throws Exception {
        if (ENABLE_RUNNER) {

            System.out.println("DÉMARRAGE DU RUNNER : TEST DES FONCTIONNALITÉS DU BACKEND");

            // ** Étape 1 : Ajout et affichage des patients **
            System.out.println("\n=== Étape 1 : Ajout des patients ===");
            patientService.savePatient(new Patient("Urgence", "Dr. Smith", "Qualité A", 48.8566, 2.3522));
            patientService.savePatient(new Patient("Pédiatrie", "Infirmière Anna", "Qualité B", 43.8336, 4.3652));
            System.out.println("Liste des patients enregistrés :");
            patientService.getAllPatients().forEach(p ->
                    System.out.println("Patient ID : " + p.getId() + ", Spécialité : " + p.getSpecialite()));

            // ** Étape 2 : Suppression d'un patient et affichage après suppression **
            System.out.println("\n=== Étape 2 : Suppression d'un patient ===");
            boolean deleted = patientService.deletePatient(1L);
            System.out.println(deleted ? "Patient avec ID 1 supprimé." : "Échec de la suppression du patient avec ID 1.");
            System.out.println("Liste des patients après suppression :");
            patientService.getAllPatients().forEach(p ->
                    System.out.println("Patient ID : " + p.getId() + ", Spécialité : " + p.getSpecialite()));

            // ** Étape 3 : Ajout et affichage des hôpitaux **
            System.out.println("\n=== Étape 3 : Ajout des hôpitaux ===");
            hopitalService.saveHopital(new Hopital("Hopital Central", Arrays.asList(1, 2), 48.8566, 2.3522, 10));
            hopitalService.saveHopital(new Hopital("Clinique du Sud", Arrays.asList(2, 3), 43.8336, 4.3652, 5));
            System.out.println("Liste des hôpitaux enregistrés :");
            hopitalService.getAllHopitaux().forEach(h ->
                    System.out.println("Hôpital ID : " + h.getId() + ", Nom : " + h.getNom() + ", Services : " + h.getServiceIdsDisponibles()));

            // ** Étape 4 : Suppression d'un hôpital et affichage après suppression **
            System.out.println("\n=== Étape 4 : Suppression d'un hôpital ===");
            deleted = hopitalService.deleteHopital(1L);
            System.out.println(deleted ? "Hôpital avec ID 1 supprimé." : "Échec de la suppression de l'hôpital avec ID 1.");
            System.out.println("Liste des hôpitaux après suppression :");
            hopitalService.getAllHopitaux().forEach(h ->
                    System.out.println("Hôpital ID : " + h.getId() + ", Nom : " + h.getNom()));

            // ** Étape 5 : Recherche du meilleur hôpital pour un patient donné **
            System.out.println("\n=== Étape 5 : Recherche du meilleur hôpital pour un patient ===");
            Optional<Patient> optionalPatient = patientService.getPatientById(2L);
            if (optionalPatient.isPresent()) {
                Patient patient = optionalPatient.get();
                int serviceId = patient.getServiceId();

                System.out.println("Patient ID : " + patient.getId() + ", Spécialité : " + patient.getSpecialite());

                // Recherche des hôpitaux par service
                List<Hopital> hospitals = populateHopitalService.getHospitalsByServiceId(serviceId);
                System.out.println("Hôpitaux offrant le service ID " + serviceId + " :");
                hospitals.forEach(h -> System.out.println("Nom : " + h.getNom() + ", Lits disponibles : " + h.getNombreLitDisponible()));

                // Recherche des hôpitaux les plus proches
                List<Hopital> nearestHospitals = populateHopitalService.getHospitalsByServiceIdSortedByDelai(serviceId);
                System.out.println("Hôpitaux les plus proches :");
                nearestHospitals.forEach(h -> System.out.println("Nom : " + h.getNom() + ", Délai : " + h.getDelai() + " minutes"));

                // Création du résultat et simulation de réservation
                Result result = resultService.createResultForPatient(patient, hospitals);
                if (result != null) {
                    System.out.println("Hôpital sélectionné : " + result.getHopitalNom() + ", Délai : " + result.getDelai());
                    Hopital selectedHospital = hospitals.stream()
                            .filter(h -> h.getNom().equals(result.getHopitalNom()))
                            .findFirst()
                            .orElse(null);
                    if (selectedHospital != null) {
                        boolean reservationSuccess = reserveService.reserveBed(selectedHospital);
                        if (reservationSuccess) {
                            System.out.println("Réservation réussie !");
                            System.out.println("État de l'hôpital après réservation :");
                            System.out.println("Nom : " + selectedHospital.getNom() + ", Lits disponibles : " + selectedHospital.getNombreLitDisponible());
                        } else {
                            System.out.println("Échec de la réservation. Aucun lit disponible.");
                        }
                    } else {
                        System.out.println("Erreur : Hôpital sélectionné introuvable pour le patient.");
                    }
                } else {
                    System.out.println("Aucun hôpital trouvé pour le patient.");
                }
            } else {
                System.out.println("Aucun patient trouvé avec l'ID 2.");
            }
        }
    }
}
