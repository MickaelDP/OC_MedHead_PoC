package com.medHead.poc.services;

import com.medHead.poc.PoCMedHeadApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medHead.poc.entity.Hopital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Service pour récupérer les hôpitaux en fonction d'un serviceId et d'une localisation.
 */
@Service
public class PopulateHopitalService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.api.url}")
    private String apiUrl;                                                                                  // URL de l'API externe, configurée dans l'application.properties

    public PopulateHopitalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //setter apiURl
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * Initialise le service avec des données fictives.
     */
    public PopulateHopitalService() {}

    /**
     * Récupère la liste des hôpitaux offrant un service spécifique à partir de leur ID.
     * Cette méthode retourne une liste d'hôpitaux correspondant à l'ID de service demandé.
     * Si aucun hôpital n'offre ce service, une liste vide est retournée.
     * L'ID de service doit être un entier positif, sinon une exception est levée.
     *
     * @param serviceId L'ID du service à rechercher (doit être > 0).
     * @return Une liste d'objets Hopital qui offrent le service correspondant.
     *         Retourne une liste vide si aucun hôpital n'offre ce service.
     * @throws IllegalArgumentException Si l'ID du service est inférieur ou égal à zéro.
     */
    public List<Hopital> getHospitalsByServiceId(int serviceId, double latitude, double longitude) {
        Logger logger = LoggerFactory.getLogger(PoCMedHeadApplication.class);

        // Validation des entrées
        if (serviceId <= 0) {
            throw new IllegalArgumentException("L'ID du service doit être un entier positif.");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("La latitude doit être comprise entre -90 et 90.");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180.");
        }

        // Construction de l'URL de la requête
        String requestUrl = String.format("%s/hospitals?serviceId=%d&lat=%.6f&lon=%.6f", apiUrl, serviceId, latitude, longitude);
        logger.info("Appel à l'API externe avec URL : {}", requestUrl);

        // Simulation d'un appel à une API externe
        Hopital[] response;
        try {
            response = restTemplate.getForObject(requestUrl, Hopital[].class);
            logger.info("Réponse reçue de l'API : {}", (Object) response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'appel à l'API : {}", e.getMessage());
            // Gestion des erreurs de connexion ou de réponse
            throw new RuntimeException("Erreur lors de l'appel à l'API externe : " + e.getMessage(), e);
        }

        // Si aucune réponse n'est retournée, renvoyer une liste vide
        if (response == null) {
            logger.warn("Aucune réponse de l'API externe.");
            return List.of();
        }

        // Conversion du tableau en liste
        return List.of(response);
    }
}