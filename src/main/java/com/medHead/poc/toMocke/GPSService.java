package com.medHead.poc.toMocke;

import com.medHead.poc.entities.Hopital;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Service temporaire pour simuler les délais de trajet entre des points GPS.
 * Cette classe sert de placeholder pour remplacer les appels réels à une API GPS
 * dans une version ultérieure du projet.
 */
@Service
public class GPSService {

    /**
     * Délai minimum simulé (en minutes).
     */
    private static final int MIN_DELAY = 5; // Délai minimum en minutes

    /**
     * Délai maximum simulé (en minutes).
     */
    private static final int MAX_DELAY = 30; // Délai maximum en minutes

    /**
     * Générateur aléatoire utilisé pour simuler les délais.
     */
    private final Random random = new Random();

    /**
     * Attribue des délais aléatoires aux hôpitaux fournis.
     * Cette méthode simule les temps de trajet calculés par une API GPS.
     *
     * @param hopitaux La liste des hôpitaux pour lesquels des délais doivent être assignés.
     * @return La liste des hôpitaux mise à jour avec des délais simulés.
     */
    public List<Hopital> assignRandomDelays(List<Hopital> hopitaux) {
        for (Hopital hopital : hopitaux) {
            int randomDelay = MIN_DELAY + random.nextInt(MAX_DELAY - MIN_DELAY + 1);
            hopital.setDelai(randomDelay);
        }
        return hopitaux;
    }
}
