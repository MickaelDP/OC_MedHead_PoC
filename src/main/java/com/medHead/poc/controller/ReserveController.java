package com.medHead.poc.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.ReserveServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Contrôleur REST pour gérer les réservations de lits dans un hôpital.
 * Expose un endpoint permettant d'effectuer une réservation de lit.
 */
@RestController
@RequestMapping("/api/reserve")
public class ReserveController {

    private static final Logger logger = LoggerFactory.getLogger(ReserveController.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");

    private final ReserveServiceInterface reserveService;

    /**
     * Constructeur avec injection de dépendance.
     *
     * @param reserveService Le service de réservation utilisé pour traiter les demandes.
     */
    @Autowired
    public ReserveController(ReserveServiceInterface reserveService) {
        this.reserveService = reserveService;
    }

    /**
     * Endpoint pour réserver un lit dans un hôpital.
     * La requête attend un objet Hopital en tant que corps JSON.
     * @param hopital L'hôpital cible pour la réservation.
     * @param simulateSuccess Un paramètre boolean pour simuler le succès ou l'échec de la réservation.
     * @return Une réponse HTTP :
     * - 200 (OK) si la réservation a réussi.
     * - 400 (Bad Request) si la réservation a échoué.
     */
    @PostMapping
    public ResponseEntity<String> reserveBed(
            @RequestBody Hopital hopital,
            @RequestParam boolean simulateSuccess) {

        // Log avant d'effectuer la réservation, sans loguer de données sensibles - hopital donnée professionnelle publique
        logger.info(HTTP_MARKER, "Tentative de réservation de lit pour l'hôpital : {}", hopital.getNom());

        // Appel du service avec le paramètre de simulation
        boolean success = reserveService.reserveBed(hopital, simulateSuccess);

        // Log après avoir traité la demande
        if (success) {
            logger.info(HTTP_MARKER, "Réservation réussie pour l'hôpital : {}", hopital.getNom());
            return ResponseEntity.ok("Réservation de lit réussie.");
        } else {
            logger.warn(HTTP_MARKER, "Échec de la réservation de lit pour l'hôpital : {}", hopital.getNom());
            return ResponseEntity.badRequest().body("Échec de la réservation de lit.");
        }
    }
}