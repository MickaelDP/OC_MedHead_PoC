package com.medHead.poc.toMocke;

import com.medHead.poc.entities.Hopital;
import org.springframework.stereotype.Service;

@Service
public class ReserveService {
    // Méthode pour simuler une réservation de lit dans un hôpital
    public boolean reserveBed(Hopital hopital) {
        if (hopital.getNombreLitDisponible() > 0) {
            // Simule la soustraction d'un lit disponible
            hopital.setNombreLitDisponible(hopital.getNombreLitDisponible() - 1);
            return true; // Réservation réussie
        } else {
            return false; // Réservation échouée, aucun lit disponible
        }
    }
}
