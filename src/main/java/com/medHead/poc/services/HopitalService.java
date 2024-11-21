package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour manipuler les objets Hopital.
 * Fournit des méthodes pour trier et gérer les hôpitaux selon les besoins de l'application.
 */
@Service
public class HopitalService {

    /**
     * Trie une liste d'hôpitaux par délai (ascendant) avec des lits disponibles.
     * Si aucun hôpital avec des lits disponibles n'est trouvé, retourne la liste triée par délai uniquement.
     *
     * @param hopitaux La liste des hôpitaux à trier.
     * @return Une liste triée des hôpitaux.
     */
    public List<Hopital> trierHopitauxParDelaiEtLits(List<Hopital> hopitaux) {
        // Filtre les hôpitaux avec des lits disponibles et trie par délai.
        List<Hopital> avecLitsDisponibles = hopitaux.stream()
                .filter(h -> h.getNombreLitDisponible() > 0)
                .sorted(Comparator.comparingInt(Hopital::getDelai))
                .collect(Collectors.toList());

        // Si aucun hôpital avec lits disponibles, trie uniquement par délai.
        if (avecLitsDisponibles.isEmpty()) {
            return hopitaux.stream()
                    .sorted(Comparator.comparingInt(Hopital::getDelai))
                    .collect(Collectors.toList());
        }

        return avecLitsDisponibles;
    }

    /**
     * Mise à jour des lits disponibles pour un hôpital spécifique.
     *
     * @param hopital L'hôpital cible.
     * @param nombreLits Le nouveau nombre de lits disponibles (doit être >= 0).
     * @throws IllegalArgumentException Si le nombre de lits est négatif.
     */
    public void mettreAJourLits(Hopital hopital, int nombreLits) {
        if (nombreLits < 0) {
            throw new IllegalArgumentException("Le nombre de lits disponibles ne peut pas être négatif.");
        }
        hopital.setNombreLitDisponible(nombreLits);
    }
}
