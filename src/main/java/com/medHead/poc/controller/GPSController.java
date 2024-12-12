package com.medHead.poc.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import com.medHead.poc.services.GPSServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Contrôleur REST pour le service GPS.
 * Simule la récupération des délais de trajet.
 */
@RestController
@RequestMapping("/api/gps")
public class GPSController {

    private static final Logger logger = LoggerFactory.getLogger(GPSController.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");
    private static final Marker AP_MARKER = MarkerFactory.getMarker("APPLICATION_FILE");  // Marqueur pour logs généraux de l'application
    private final GPSServiceInterface gpsService;

    @Autowired
    public GPSController(GPSServiceInterface gpsService) {
        this.gpsService = gpsService;
    }

    /**
     * Récupère un délai de trajet simulé entre un patient et un hôpital.
     * Effectue des validations pour s'assurer que les coordonnées sont valides.
     *
     * @param patientLat   Latitude du patient.
     * @param patientLon   Longitude du patient.
     * @param hospitalLat  Latitude de l'hôpital.
     * @param hospitalLon  Longitude de l'hôpital.
     * @return Un délai simulé en minutes ou une erreur si les coordonnées sont invalides.
     */
    @GetMapping("/delay")
    @PreAuthorize("hasRole('USER')") // Accès réservé aux utilisateurs avec le rôle USER
    public ResponseEntity<Integer> getTravelDelay(
            @RequestParam double patientLat,
            @RequestParam double patientLon,
            @RequestParam double hospitalLat,
            @RequestParam double hospitalLon) {

        // Log de la demande, masquage des coordonnées pour respecter la RGPD
        logger.info(HTTP_MARKER, "Demande de délai de trajet entre patient et hôpital. Paramètres: patientLatitude=<masqué>, patientLongitude=<masqué>, hospitalLatitude=<masqué>, hospitalLongitude=<masqué>");

        // Valider les coordonnées
        if (patientLat > 90 || patientLat < -90 || patientLon > 180 || patientLon < -180 ||
                hospitalLat > 90 || hospitalLat < -90 || hospitalLon > 180 || hospitalLon < -180) {
            // Log d'erreur
            logger.warn(AP_MARKER, "Coordonnées invalides reçues pour le calcul du délai de trajet. PatientLat=<masqué>, PatientLon=<masqué>, HospitalLat=<masqué>, HospitalLon=<masqué>");
            return ResponseEntity.badRequest().build();                                             // Retourne une erreur 400 (Bad Request)
        }

        // Appelle le service pour simuler un délai
        int delay = gpsService.getTravelDelay(patientLat, patientLon, hospitalLat, hospitalLon);

        // Log du délai calculé avec le marqueur HTTP_FILE
        logger.info(HTTP_MARKER, "Délai de trajet calculé: {} minutes.", delay);

        return ResponseEntity.ok(delay);                                                           // Retourne un ResponseEntity contenant le délai
    }
}
