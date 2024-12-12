package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(HopitalService.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");


    /**
     * Trie une liste d'hôpitaux par délai (ascendant) avec des lits disponibles.
     * Si aucun hôpital avec des lits disponibles n'est trouvé, retourne la liste triée par délai uniquement.
     *
     * @param hopitaux La liste des hôpitaux à trier.
     * @return Une liste triée des hôpitaux.
     */
    public List<Hopital> trierHopitauxParDelaiEtLits(List<Hopital> hopitaux) {

        // Vérification que la liste n'est pas nulle
        if (hopitaux == null) {
            logger.error(HTTP_MARKER, "La liste des hôpitaux ne peut pas être null.");
            throw new IllegalArgumentException("La liste des hôpitaux ne peut pas être null.");
        }

        // Filtre les hôpitaux avec des lits disponibles et trie par délai.
        List<Hopital> avecLitsDisponibles = hopitaux.stream()
                .filter(h -> h.getNombreLitDisponible() > 0)
                .sorted(Comparator.comparingInt(Hopital::getDelai))
                .collect(Collectors.toList());

        // Si aucun hôpital avec lits disponibles, trie uniquement par délai.
        if (avecLitsDisponibles.isEmpty()) {
            // Log de la situation où aucun hôpital avec lits disponibles n'est trouvé
            logger.info(HTTP_MARKER, "Aucun hôpital avec lits disponibles trouvé. Tri par délai uniquement.");
            return hopitaux.stream()
                    .sorted(Comparator.comparingInt(Hopital::getDelai))
                    .collect(Collectors.toList());
        }
        // Log indiquant que des hôpitaux avec lits disponibles ont été trouvés et triés
        logger.info(HTTP_MARKER, "Hôpitaux avec lits disponibles triés par délai.");
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
        if (hopital == null) {
            logger.error(HTTP_MARKER, "L'hôpital ne peut pas être null.");
            throw new IllegalArgumentException("L'hôpital ne peut pas être null.");
        }

        if (nombreLits < 0) {
            logger.error(HTTP_MARKER, "Le nombre de lits disponibles ne peut pas être négatif.");
            throw new IllegalArgumentException("Le nombre de lits disponibles ne peut pas être négatif.");
        }

        // Log de la mise à jour du nombre de lits
        logger.info(HTTP_MARKER, "Mise à jour du nombre de lits disponibles pour l'hôpital: {}. Nouveau nombre de lits: <masqué>", hopital.getNom());

        hopital.setNombreLitDisponible(nombreLits);
    }
}
