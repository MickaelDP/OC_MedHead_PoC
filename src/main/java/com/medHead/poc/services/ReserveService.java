package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les réservations de lits dans un hôpital.
 * Ce service simule une réservation réussie ou échouée en fonction de l'entrée.
 */
@Service
public class ReserveService {

    /**
     * Simule la réservation d'un lit dans un hôpital.
     *
     * @param hopital L'hôpital cible pour la réservation.
     * @param success Indique si la réservation doit réussir (true) ou échouer (false).
     * @return true si la réservation réussit, false sinon.
     */
    public boolean reserveBed(Hopital hopital, boolean success) {
        // Retourne directement le paramètre success pour contrôler la simulation
        return success;
    }
}