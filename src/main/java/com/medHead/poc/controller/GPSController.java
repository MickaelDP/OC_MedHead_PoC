package com.medHead.poc.controller;

import com.medHead.poc.services.GPSService;
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

    private final GPSService gpsService;



    @Autowired
    public GPSController(GPSService gpsService) {
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

        // Valider les coordonnées
        if (patientLat > 90 || patientLat < -90 || patientLon > 180 || patientLon < -180 ||
                hospitalLat > 90 || hospitalLat < -90 || hospitalLon > 180 || hospitalLon < -180) {
            return ResponseEntity.badRequest().build();                                             // Retourne une erreur 400 (Bad Request)
        }

        // Appelle le service pour simuler un délai
        int delay = gpsService.getTravelDelay(patientLat, patientLon, hospitalLat, hospitalLon);
        return ResponseEntity.ok(delay);                                                           // Retourne un ResponseEntity contenant le délai
    }
}
