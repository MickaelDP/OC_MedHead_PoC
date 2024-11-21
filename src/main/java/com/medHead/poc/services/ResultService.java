package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.model.Patient;
import com.medHead.poc.model.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service pour gérer les résultats associés aux patients.
 * Conteneur et validateur pour les résultats calculés dans l'application principale.
 */
@Service
public class ResultService {

    private final List<Result> results = new ArrayList<>(); // Stockage en mémoire des résultats (pour PoC)

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
        validateInputs(patient, hopital);

        Result result = new Result(
                patient.getId(),
                patient.getSpecialite(),
                hopital.getNom(),
                hopital.getDelai(),
                true, // La spécialité est nécessairement disponible, sinon cet hôpital ne serait pas sélectionné
                litReserve
        );

        saveResult(result);
        return result;
    }

    /**
     * Valide les informations nécessaires pour la création d'un résultat.
     * @param patient Le patient à valider
     * @param hopital L'hôpital à valider
     * @throws IllegalArgumentException si des informations sont manquantes ou invalides
     */
    private void validateInputs(Patient patient, Hopital hopital) {
        if (patient == null || patient.getId() == null || patient.getSpecialite() == null || patient.getSpecialite().isEmpty()) {
            throw new IllegalArgumentException("Les informations du patient sont invalides ou incomplètes.");
        }

        if (hopital == null || hopital.getNom() == null || hopital.getNom().isEmpty() || hopital.getDelai() < 0) {
            throw new IllegalArgumentException("Les informations de l'hôpital sont invalides ou incomplètes.");
        }
    }

    /**
     * Sauvegarde un nouveau résultat dans le stockage en mémoire.
     * @param result Le résultat à sauvegarder
     * @return Le résultat sauvegardé
     */
    public Result saveResult(Result result) {
        result.setId(UUID.randomUUID()); // Génère un ID unique pour chaque résultat
        results.add(result);
        return result;
    }

    /**
     * Récupère tous les résultats stockés.
     * @return Une liste de résultats
     */
    public List<Result> getAllResults() {
        return new ArrayList<>(results); // Retourne une copie pour éviter les modifications externes
    }

    /**
     * Récupère un résultat par son ID.
     * @param id L'ID du résultat recherché
     * @return Le résultat correspondant, ou null si non trouvé
     */
    public Result getResultById(UUID id) {
        return results.stream()
                .filter(result -> result.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Supprime un résultat par son ID.
     * @param id L'ID du résultat à supprimer
     * @return true si le résultat a été supprimé, false sinon
     */
    public boolean deleteResult(UUID id) {
        return results.removeIf(result -> result.getId().equals(id));
    }
}