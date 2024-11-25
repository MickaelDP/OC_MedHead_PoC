package com.medHead.poc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medHead.poc.model.Patient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Service pour gérer les patients et leur attribution à des services médicaux.
 */
@Service
public class PatientService {

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
            throw new IllegalArgumentException("La spécialité du patient est obligatoire.");
        }
        if (!specialityDictionary.containsKey(patient.getSpecialite())) {
            throw new IllegalArgumentException("Spécialité inconnue : " + patient.getSpecialite());
        }
        // Assignation du service ID basé sur la spécialité
        patient.setServiceId(specialityDictionary.get(patient.getSpecialite()));

        // Génération d'un identifiant unique si non défini
        if (patient.getId() == null) {
            patient.setId(UUID.randomUUID());
        }

        // Stocker en mémoire ou envoyer au prochain traitement
        savePatient(patient);
        return patient;
    }

    /**
     * Récupère tous les patients enregistrés.
     * @return Une liste contenant tous les patients.
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());                                              // Renvoie une liste des valeurs de la map
    }

    /**
     * Récupère un patient par son ID.
     * @param id L'ID du patient à rechercher.
     * @return Un Optional contenant le patient s'il existe.
     */
    public Optional<Patient> getPatientById(UUID id) {
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
            throw new IllegalArgumentException("Spécialité inconnue : " + patient.getSpecialite());
        }

        // Détermine le service ID en fonction de la spécialité.
        patient.setServiceId(serviceId);

        // Assigne un ID unique.
        if (patient.getId() == null) {
            patient.setId(UUID.randomUUID());
        }

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
        return specialityDictionary;
    }

    /**
     * Vide la liste des patients en mémoire.
     * Principalement utilisé pour les tests ou la réinitialisation.
     */
    public void clearPatients() {
        patients.clear();
    }
}
