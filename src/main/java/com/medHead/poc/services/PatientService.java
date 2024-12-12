package com.medHead.poc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medHead.poc.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Service pour gérer les patients et leur attribution à des services médicaux.
 */
@Service
public class PatientService implements PatientServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");

    private final Map<UUID, Patient> patients = Collections.synchronizedMap(new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<UUID, Patient> eldest) {
            return size() > 1000; // Limite à 1000 patients
        }
    });                                                                                         // Stockage en mémoire des patients
    private final Map<String, Integer> specialityDictionary = new HashMap<>();                  // Dictionnaire des spécialités
    private static final String SPECIALITIES_FILE = "src/main/resources/specialities.json";

    /**
     * Constructeur du service. Charge le dictionnaire des spécialités depuis le fichier JSON.
     */
    public PatientService() {
        try {
            loadSpecialitiesFromJson();
        } catch (IOException e) {
            logger.error(HTTP_MARKER, "Erreur lors du chargement des spécialités depuis le fichier JSON : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors du chargement des spécialités : " + e.getMessage(), e);
        }
    }

    /**
     * Charge le dictionnaire des spécialités depuis un fichier JSON.
     * @throws IOException En cas d'erreur lors de la lecture du fichier.
     */
    private void loadSpecialitiesFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> specialities = mapper.readValue(Paths.get(SPECIALITIES_FILE).toFile(), Map.class);
        specialityDictionary.putAll(specialities);

        logger.info(HTTP_MARKER, "Dictionnaire des spécialités chargé avec succès.");
    }

    /**
     * Initialise un patient avec les informations nécessaires avant traitement.
     * Valide les données reçues, assigne un service ID en fonction de la spécialité,
     * et effectue d'autres traitements initiaux si nécessaire.
     *
     * @param patient L'objet Patient à initialiser.
     * @return Le patient initialisé avec toutes les données enrichies.
     * @throws IllegalArgumentException Si la spécialité est inconnue ou si des données requises sont manquantes.
     */
    public Patient initializePatient(Patient patient) {
        // Validation des données de base
        if (patient.getSpecialite() == null || patient.getSpecialite().isEmpty()) {
            logger.error(HTTP_MARKER, "La spécialité du patient est obligatoire.");
            throw new IllegalArgumentException("La spécialité du patient est obligatoire.");
        }
        if (!specialityDictionary.containsKey(patient.getSpecialite())) {
            logger.error(HTTP_MARKER, "Spécialité inconnue : {}", patient.getSpecialite());
            throw new IllegalArgumentException("Spécialité inconnue : " + patient.getSpecialite());
        }
        // Assignation du service ID basé sur la spécialité
        patient.setServiceId(specialityDictionary.get(patient.getSpecialite()));

        // Génération d'un identifiant unique si non défini
        if (patient.getId() == null) {
            patient.setId(UUID.randomUUID());
        }

        logger.info(HTTP_MARKER, "Patient initialisé : {}", patient);

        // Stocker en mémoire ou envoyer au prochain traitement
        savePatient(patient);
        return patient;
    }


    /**
     * Récupère tous les patients enregistrés.
     * @return Une liste contenant tous les patients.
     */
    public List<Patient> getAllPatients() {
        logger.info(HTTP_MARKER, "Récupération de tous les patients.");
        return new ArrayList<>(patients.values());                                              // Renvoie une liste des valeurs de la map
    }

    /**
     * Récupère un patient par son ID.
     * @param id L'ID du patient à rechercher.
     * @return Un Optional contenant le patient s'il existe.
     */
    public Optional<Patient> getPatientById(UUID id) {
        logger.info(HTTP_MARKER, "Recherche du patient avec ID : {}", id);
        return Optional.ofNullable(patients.get(id));                                           // Récupère directement depuis la map
    }

    /**
     * Enregistre un nouveau patient et assigne un service ID en fonction de sa spécialité.
     * @param patient Le patient à enregistrer.
     * @return Le patient enregistré avec ID et service assignés.
     */
    public Patient savePatient(Patient patient) {
        int serviceId = specialityDictionary.getOrDefault(patient.getSpecialite(), -1);// -1 pour spécialité inconnue
        // Vérification de la spécialité
        if (serviceId <= 0) {
            logger.error(HTTP_MARKER, "Spécialité inconnue : {}", patient.getSpecialite());
            throw new IllegalArgumentException("Spécialité inconnue : " + patient.getSpecialite());
        }

        // Détermine le service ID en fonction de la spécialité.
        patient.setServiceId(serviceId);

        // Assigne un ID unique.
        if (patient.getId() == null) {
            patient.setId(UUID.randomUUID());
        }

        logger.info(HTTP_MARKER, "Enregistrement du patient : {}", patient);

        // Sauvegarde le patient.
        patients.put(patient.getId(), patient);                                                 // Ajoute ou met à jour dans la map
        return patient;
    }

    /**
     * Supprime un patient par son ID.
     * @param id L'ID du patient à supprimer.
     * @return true si le patient a été supprimé, false sinon.
     */
    public boolean deletePatient(UUID id) {
        logger.info(HTTP_MARKER, "Suppression du patient avec ID : {}", id);

        return patients.remove(id) != null;                                                     // Supprime de la map et vérifie si un élément a été supprimé
    }

    /**
     * Récupère le dictionnaire des spécialités médicales.
     * Le dictionnaire associe chaque spécialité à un identifiant de service unique.
     *
     * @return Une map contenant les spécialités en tant que clés (String)
     *         et leurs identifiants de service correspondants (Integer).
     */
    public Map<String, Integer> getSpecialityDictionary() {
        logger.info(HTTP_MARKER, "Récupération du dictionnaire des spécialités.");
        return specialityDictionary;
    }
    /**
     * Vide la liste des patients en mémoire.
     * Principalement utilisé pour les tests ou la réinitialisation.
     */
    public void clearPatients() {
        logger.info(HTTP_MARKER, "Vider la liste des patients en mémoire.");
        patients.clear();
    }
}
