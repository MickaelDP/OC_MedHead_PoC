package com.medHead.poc.services;

import com.medHead.poc.entities.Hopital;
import com.medHead.poc.entities.Patient;
import com.medHead.poc.entities.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les résultats de recherche d'hôpitaux pour les patients.
 * Utilise un stockage en mémoire pour les besoins de démonstration ou de développement.
 */
@Service
public class ResultService {

    private List<Result> results = new ArrayList<>();                                           // Stockage en mémoire des résultats

    /**
     * Crée un résultat pour un patient donné en sélectionnant le meilleur hôpital.
     * Critères : disponibilité de lits, délai le plus court.
     *
     * @param patient Le patient pour lequel le résultat est généré
     * @param hopitaux La liste des hôpitaux disponibles
     * @return Un objet Result représentant le résultat de la recherche
     */
    public Result createResultForPatient(Patient patient, List<Hopital> hopitaux) {
        // Chercher le meilleur hôpital avec des lits disponibles
        Optional<Hopital> bestWithBeds = hopitaux.stream()
                .filter(h -> h.getNombreLitDisponible() > 0)
                .min((h1, h2) -> Integer.compare(h1.getDelai(), h2.getDelai()));

        /* Priorité de sélection :
         * 1 - Hôpital offrant le service requis ET disposant de lits disponibles (le plus proche).
         * 2 - Hôpital offrant le service requis mais sans lits disponibles (le plus proche).
         */
        if (bestWithBeds.isPresent()) {
            // Hôpital avec lits disponibles trouvé
            Hopital hopital = bestWithBeds.get();
            return new Result(patient.getId(), patient.getSpecialite(), hopital.getNom(), hopital.getDelai(), true, true);
        } else {
            // Aucun lit disponible, trouver l'hôpital avec le délai le plus court
            Hopital bestWithoutBeds = hopitaux.stream()
                    .min((h1, h2) -> Integer.compare(h1.getDelai(), h2.getDelai()))
                    .orElse(null);

            if (bestWithoutBeds != null) {
                // Hôpital trouvé mais sans lits disponibles
                return new Result(patient.getId(), patient.getSpecialite(), bestWithoutBeds.getNom(), bestWithoutBeds.getDelai(), true, false);
            } else {
                // Aucun hôpital trouvé (liste vide)
                return null;
            }
        }
    }

    /**
     * Récupère la liste de tous les résultats.
     * @return Une liste contenant tous les résultats enregistrés
     */
    public List<Result> getAllResults() {
        return results;
    }

    /**
     * Récupère un résultat par son ID.
     * @param id L'ID du résultat à rechercher
     * @return Un objet Result si trouvé, null sinon
     */
    public Result getResultById(Long id) {
        return results.stream()
                .filter(result -> result.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sauvegarde un nouveau résultat.
     * @param result Le résultat à sauvegarder
     * @return Le résultat sauvegardé avec un ID unique généré
     */
    public Result saveResult(Result result) {
        result.setId(generateUniqueId()); // Générer un ID unique pour le résultat
        results.add(result);
        return result;
    }

    /**
     * Supprime un résultat par son ID.
     * @param id L'ID du résultat à supprimer
     * @return true si le résultat a été supprimé, false sinon
     */
    public boolean deleteResult(Long id) {
        return results.removeIf(result -> result.getId().equals(id));
    }

    /**
     * Génère un ID unique pour un résultat.
     * Méthode simplifiée pour une PoC.
     * @return Un ID unique basé sur le timestamp
     */
    private Long generateUniqueId() {
        return System.currentTimeMillis(); // Génération d'ID simplifiée pour la PoC
    }
}
