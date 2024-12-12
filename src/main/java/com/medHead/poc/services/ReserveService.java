package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les réservations de lits dans un hôpital.
 * Ce service simule une réservation réussie ou échouée en fonction de l'entrée.
 */
@Service
public class ReserveService implements ReserveServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(ReserveService.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("http");
    private static final Marker APP_MARKER = MarkerFactory.getMarker("app");

    /**
     * Simule la réservation d'un lit dans un hôpital.
     * @param hopital L'hôpital cible pour la réservation.
     * @param success Indique si la réservation doit réussir (true) ou échouer (false).
     * @return true si la réservation réussit, false sinon.
     */
    public boolean reserveBed(Hopital hopital, boolean success) {
        logger.info(APP_MARKER, "Tentative de réservation d'un lit pour l'hôpital : {}", hopital.getNom());

        // Simulation de la réussite ou de l'échec
        if (success) {
            // Log en cas de succès
            logger.info(APP_MARKER, "Réservation réussie pour l'hôpital : {}", hopital.getNom());
            return true;
        } else {
            // Log en cas d'échec
            logger.warn(APP_MARKER, "Réservation échouée pour l'hôpital : {}", hopital.getNom());
            return false;
        }
    }
}