package com.medHead.poc.services;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import com.medHead.poc.entity.Hopital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service pour récupérer les hôpitaux en fonction d'un serviceId et d'une localisation.
 * Utilise un cache local pour optimiser les performances et réduire les appels à l'API externe.
 */
@Service
public class PopulateHopitalService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.api.url}")
    private String apiUrl;                                                                                  // URL de l'API externe, configurée dans l'application.properties

    private static Logger logger = LoggerFactory.getLogger(PopulateHopitalService.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("http");
    private static final Marker APP_MARKER = MarkerFactory.getMarker("app");

    /**
     * Cache local pour stocker les résultats des appels API en fonction de la clé unique.
     * Utilise un ConcurrentHashMap pour garantir la sécurité des threads.
     */
    private final ConcurrentHashMap<String, List<Hopital>> hospitalCache = new ConcurrentHashMap<>();

    /**
     * Définit l'URL de l'API externe.
     * @param apiUrl URL de l'API externe.
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public PopulateHopitalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Initialise le service avec des données fictives.
     */
    public PopulateHopitalService() {}

    /**
     * Récupère la liste des hôpitaux offrant un service spécifique à partir de leur ID.
     * @param serviceId L'ID du service à rechercher (doit être > 0).
     * @param latitude  Latitude géographique du patient (entre -90 et 90).
     * @param longitude Longitude géographique du patient (entre -180 et 180).
     * @return Une liste d'objets Hopital offrant le service demandé ou une liste vide.
     * @throws IllegalArgumentException Si les entrées sont invalides.
     */
    public List<Hopital> getHospitalsByServiceId(int serviceId, double latitude, double longitude) {
        logger.info(APP_MARKER, "Requête pour serviceId={} avec latitude={}, longitude={}", serviceId, latitude, longitude);

        // Validation des entrées
        validateInputs(serviceId, latitude, longitude);

        // Génération de la clé unique pour le cache
        String cacheKey = generateCacheKey(serviceId, latitude, longitude);

        // Vérification dans le cache
        if (hospitalCache.containsKey(cacheKey)) {
            logger.info(APP_MARKER, "Résultat trouvé dans le cache pour la clé : {}", cacheKey);
            return hospitalCache.get(cacheKey);
        }

        // Construction de l'URL de la requête
        String requestUrl = String.format("%s/hospitals?serviceId=%d&lat=%.6f&lon=%.6f", apiUrl, serviceId, latitude, longitude);
        logger.info(HTTP_MARKER, "Appel à l'API externe avec URL : {}", requestUrl);

        // Simulation d'un appel à une API externe
        Hopital[] response;
        try {
            response = restTemplate.getForObject(requestUrl, Hopital[].class);
            logger.info(HTTP_MARKER, "Réponse reçue de l'API : {}", (Object) response);
        } catch (Exception e) {
            logger.error(HTTP_MARKER, "Erreur lors de l'appel à l'API : {}", e.getMessage());
            // Gestion des erreurs de connexion ou de réponse
            throw new RuntimeException("Erreur lors de l'appel à l'API externe : " + e.getMessage(), e);
        }

        // Si aucune réponse n'est retournée, renvoyer une liste vide
        if (response == null) {
            logger.warn(HTTP_MARKER, "Aucune réponse de l'API externe.");
            return List.of();
        }

        // Conversion du tableau en liste
        return List.of(response);
    }

    /**
     * Nettoyage des ressources avant la destruction du bean.
     */
    @PreDestroy
    public void cleanup() {
        logger.info(APP_MARKER, "Aucune ressource spécifique à nettoyer dans RestTemplate (SimpleClientHttpRequestFactory).");
    }

    /**
     * Génère une clé unique pour le cache basée sur le serviceId, latitude et longitude.
     * @param serviceId L'ID du service demandé.
     * @param latitude  Latitude géographique.
     * @param longitude Longitude géographique.
     * @return Une chaîne unique représentant la clé pour le cache.
     */
    private String generateCacheKey(int serviceId, double latitude, double longitude) {
        return serviceId + "_" + latitude + "_" + longitude;
    }

    /**
     * Valide les entrées utilisateur pour éviter les valeurs incorrectes.
     * @param serviceId L'ID du service demandé (doit être > 0).
     * @param latitude  Latitude géographique (entre -90 et 90).
     * @param longitude Longitude géographique (entre -180 et 180).
     * @throws IllegalArgumentException Si une entrée est invalide.
     */
    private void validateInputs(int serviceId, double latitude, double longitude) {
        if (serviceId <= 0) {
            logger.error(APP_MARKER, "L'ID du service doit être un entier positif. Entrée invalide: serviceId={}", serviceId);
            throw new IllegalArgumentException("L'ID du service doit être un entier positif.");
        }
        if (latitude < -90 || latitude > 90) {
            logger.error(APP_MARKER, "La latitude doit être comprise entre -90 et 90. Entrée invalide: latitude={}", latitude);
            throw new IllegalArgumentException("La latitude doit être comprise entre -90 et 90.");
        }
        if (longitude < -180 || longitude > 180) {
            logger.error(APP_MARKER, "La longitude doit être comprise entre -180 et 180. Entrée invalide: longitude={}", longitude);
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180.");
        }
    }

    /**
     * Vide le cache local.
     */
    public void clearCache() {
        hospitalCache.clear();
        logger.info(APP_MARKER, "Cache vidé.");
    }
}