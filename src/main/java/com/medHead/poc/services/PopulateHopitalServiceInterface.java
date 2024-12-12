package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface PopulateHopitalServiceInterface {

    /**
     * Récupère la liste des hôpitaux correspondant à un service donné
     * et calculer leur délai en fonction des coordonnées.
     *
     * @param serviceId   L'ID du service médical recherché
     * @param latitude    La latitude du patient
     * @param longitude   La longitude du patient
     * @return Liste des hôpitaux correspondant au service recherché
     */
    List<Hopital> getHospitalsByServiceId(int serviceId, double latitude, double longitude);
    void clearCache();
    void setApiUrl(String apiUrl);
    void cleanup();
    void setRestTemplate(RestTemplate restTemplate);
}
