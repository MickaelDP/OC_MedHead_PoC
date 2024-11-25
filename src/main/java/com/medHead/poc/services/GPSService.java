package com.medHead.poc.services;

import org.springframework.stereotype.Service;

import java.util.Random;


/**
 * Service pour simuler des appels à une API GPS pour obtenir un délai.
 */
@Service
public class GPSService {

    /**
     * Délai minimum simulé (en minutes).
     */
    private static final int MIN_DELAY = 5;                                 // Délai minimum en minutes

    /**
     * Délai maximum simulé (en minutes).
     */
    private static final int MAX_DELAY = 17;                                // Délai maximum en minutes

    /**
     * Générateur aléatoire utilisé pour simuler les délais.
     */
    private static final Random random = new Random();

    /**
     * Simule un appel API pour obtenir un délai entre le patient et un hôpital.
     *
     * @param patientLatitude   Latitude du patient.
     * @param patientLongitude  Longitude du patient.
     * @param hospitalLatitude  Latitude de l'hôpital.
     * @param hospitalLongitude Longitude de l'hôpital.
     * @return Un délai simulé en minutes (entre 5 et 30 minutes).
     */

    public int getTravelDelay(double patientLatitude, double patientLongitude, double hospitalLatitude, double hospitalLongitude) {
        // Simule un délai aléatoire
        return MIN_DELAY + random.nextInt(MAX_DELAY - MIN_DELAY + 1);
    }
}
