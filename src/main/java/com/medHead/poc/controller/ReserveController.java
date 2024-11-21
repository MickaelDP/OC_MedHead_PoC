package com.medHead.poc.controller;

import com.medHead.poc.entity.Hopital;
import com.medHead.poc.services.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les réservations de lits dans un hôpital.
 * Expose un endpoint permettant d'effectuer une réservation de lit.
 */
@RestController
@RequestMapping("/api/reserve")
public class ReserveController {

    private final ReserveService reserveService;

    /**
     * Constructeur avec injection de dépendance.
     *
     * @param reserveService Le service de réservation utilisé pour traiter les demandes.
     */
    @Autowired
    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    /**
     * Endpoint pour réserver un lit dans un hôpital.
     * La requête attend un objet Hopital en tant que corps JSON.
     *
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

        // Appel du service avec le paramètre de simulation
        boolean success = reserveService.reserveBed(hopital, simulateSuccess);

        return success ?
                ResponseEntity.ok("Réservation de lit réussie.") :
                ResponseEntity.badRequest().body("Échec de la réservation de lit.");
    }
}