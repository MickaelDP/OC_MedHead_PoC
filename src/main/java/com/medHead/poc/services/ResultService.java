package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.model.Patient;
import com.medHead.poc.model.Result;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.*;

/**
 * Service pour gérer les résultats associés aux patients.
 * Conteneur et validateur pour les résultats calculés dans l'application principale.
 */
@Service
public class ResultService {

    private static final Logger logger = LoggerFactory.getLogger(ResultService.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("app");

    // Cache avec limite de taille
    private final Map<UUID, Result> results = Collections.synchronizedMap(new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<UUID, Result> eldest) {
            return size() > 100; // Limite à 100 résultats
        }
    });

    /**
     * Crée un objet `Result` basé sur les informations calculées dans l'application.
     * Les données doivent être validées avant la création du résultat.
     *
     * @param patient   Le patient associé au résultat
     * @param hopital   L'hôpital sélectionné pour le patient
     * @param litReserve Indique si un lit a été réservé avec succès
     * @return Le résultat créé et sauvegardé
     * @throws IllegalArgumentException si les informations sont invalides
     */
    public Result createAndSaveResult(Patient patient, Hopital hopital, boolean litReserve) {
        try {
            // Vérification que le patient n'est pas nul
            if (patient == null) {
                logger.error(APP_MARKER, "Patient est null dans la méthode createAndSaveResult.");
                throw new IllegalArgumentException("Les informations du patient sont invalides ou incomplètes.");
            }

            // Vérification que l'hôpital n'est pas nul
            if (hopital == null) {
                logger.error(APP_MARKER, "Hopital est null dans la méthode createAndSaveResult.");
                throw new IllegalArgumentException("Les informations de l'hôpital sont invalides ou incomplètes.");
            }

            validateInputs(patient, hopital);

            Result result = new Result(
                    patient.getId(),
                    patient.getSpecialite(),
                    hopital.getNom(),
                    hopital.getDelai(),
                    true,
                    litReserve
            );
            logger.info(APP_MARKER, "Résultat créé pour le patient avec ID : {}", patient.getId());
            return saveResult(result);
        } catch (IllegalArgumentException e) {
            logger.error(APP_MARKER, "Erreur lors de la création d'un résultat : {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Valide les informations nécessaires pour la création d'un résultat.
     * @param patient Le patient à valider
     * @param hopital L'hôpital à valider
     * @throws IllegalArgumentException si des informations sont manquantes ou invalides
     */
    private void validateInputs(Patient patient, Hopital hopital) {
        logger.info(APP_MARKER, "Validation des informations du patient et de l'hôpital.");

        if (patient == null || patient.getId() == null || patient.getSpecialite() == null || patient.getSpecialite().isEmpty()) {
            logger.error(APP_MARKER, "Informations du patient invalides : {}", patient);
            throw new IllegalArgumentException("Les informations du patient sont invalides ou incomplètes.");
        }

        if (hopital == null || hopital.getNom() == null || hopital.getNom().isEmpty() || hopital.getDelai() < 0) {
            logger.error(APP_MARKER, "Informations de l'hôpital invalides : {}", hopital);
            throw new IllegalArgumentException("Les informations de l'hôpital sont invalides ou incomplètes.");
        }
    }

    /**
     * Sauvegarde un nouveau résultat dans le stockage en mémoire.
     * @param result Le résultat à sauvegarder
     * @return Le résultat sauvegardé
     */
    public Result saveResult(Result result) {
        logger.info(APP_MARKER, "Sauvegarde du résultat avec ID : {}", result.getId());

        if (result.getId() == null) {
            result.setId(UUID.randomUUID());
        }
        results.put(result.getId(), result); // Ajoute le résultat au cache
        return result;
    }

    /**
     * Récupère tous les résultats stockés.
     * @return Une liste de résultats
     */
    public List<Result> getAllResults() {
        logger.info(APP_MARKER, "Récupération de tous les résultats.");
        return new ArrayList<>(results.values());
    }

    /**
     * Récupère un résultat par son ID.
     * @param id L'ID du résultat recherché
     * @return Le résultat correspondant, ou null si non trouvé
     */
    public Result getResultById(UUID id) {
        logger.info(APP_MARKER, "Récupération du résultat avec ID : {}", id);
        return results.get(id);
    }
    /**
     * Supprime un résultat par son ID.
     * @param id L'ID du résultat à supprimer
     * @return true si le résultat a été supprimé, false sinon
     */
    public boolean deleteResult(UUID id) {
        logger.info(APP_MARKER, "Suppression du résultat avec ID : {}", id);
        return results.remove(id) != null;
    }

    public void clearResults() {
        logger.info(APP_MARKER, "Vider la liste des résultats en mémoire.");
        results.clear(); // Vide la liste des résultats pour libérer la mémoire
    }
}