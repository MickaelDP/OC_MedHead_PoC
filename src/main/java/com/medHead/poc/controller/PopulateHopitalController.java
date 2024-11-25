package com.medHead.poc.controller;

import com.medHead.poc.entity.Hopital;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contrôleur REST pour gérer les requêtes concernant les hôpitaux.
 */
@RestController
@RequestMapping("/api/hospitals")
public class PopulateHopitalController {

    // Cache local avec thread safety
    private final ConcurrentHashMap<String, List<Hopital>> hospitalCache = new ConcurrentHashMap<>();


    /**
     * Endpoint pour récupérer une liste d'hôpitaux offrant un service spécifique, basés sur une localisation.
     *
     * @param serviceId L'ID du service requis par le patient.
     * @param lat       La latitude de la localisation du patient.
     * @param lon       La longitude de la localisation du patient.
     * @return Une liste d'objets Hopital correspondant aux critères de recherche.
     *         Les données retournées ici sont statiques et simulées.
     */
    @GetMapping
    public List<Hopital> getHospitals(
            @RequestParam int serviceId,
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        String cacheKey = serviceId + "-" + lat + "-" + lon;

        // Vérifie si la réponse est en cache
        if (hospitalCache.containsKey(cacheKey)) {
            return hospitalCache.get(cacheKey);
        }

        // Simule une réponse statique (à remplacer par la logique réelle si nécessaire)
        List<Hopital> hospitals = List.of(
                new Hopital("Hopital A", List.of(1, 2, 4, 5, 6, 7, 8, 11), 48.8566, 2.3522, 15),
                new Hopital("Hopital B", List.of(2, 3, 4, 5, 7, 9, 10, 11, 12), 48.8648, 2.3499, 8),
                new Hopital("Hopital C", List.of(1, 2, 3, 5, 6, 7, 12), 48.8584, 2.2945, 20),
                new Hopital("Hopital D", List.of(2, 3, 4, 5, 9, 10, 11), 48.8600, 2.3270, 12),
                new Hopital("Hopital E", List.of(1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12), 48.8675, 2.3300, 5),
                new Hopital("Hopital F", List.of(1, 2, 5, 6, 7, 9, 10), 48.8619, 2.3364, 18),
                new Hopital("Hopital G", List.of(1, 3, 4, 5, 8, 9, 11), 48.8545, 2.3478, 10),
                new Hopital("Hopital H", List.of(1, 2, 3, 4, 5, 6, 7, 8, 12), 48.8550, 2.3419, 7),
                new Hopital("Hopital I", List.of(1, 2, 3, 4, 5, 6, 9, 10, 11), 48.8590, 2.3540, 25),
                new Hopital("Hopital J", List.of(1, 5, 6, 7, 8, 10), 48.8534, 2.3488, 9)
        );

        // Ajoute la réponse dans le cache
        hospitalCache.put(cacheKey, hospitals);
        return hospitals;
    }

    @DeleteMapping("/cache/clear")
    public void clearCache() {
        hospitalCache.clear();
    }
}
