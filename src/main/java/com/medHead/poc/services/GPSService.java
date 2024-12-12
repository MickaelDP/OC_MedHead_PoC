package com.medHead.poc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;


/**
 * Service pour simuler des appels à une API GPS pour obtenir un délai.
 */
@Service
public class GPSService {

    private static final Logger logger = LoggerFactory.getLogger(GPSService.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");


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
        // Log de la demande, masquage des coordonnées pour respecter la RGPD
        logger.info(HTTP_MARKER, "Demande de délai de trajet entre patient et hôpital. Paramètres: patientLatitude=<masqué>, patientLongitude=<masqué>, hospitalLatitude=<masqué>, hospitalLongitude=<masqué>");


        int delay = MIN_DELAY + random.nextInt(MAX_DELAY - MIN_DELAY + 1);

        // Log du délai calculé avec le marqueur HTTP_FILE
        logger.info(HTTP_MARKER, "Délai de trajet calculé: {} minutes.", delay);

        return delay;
    }
}
