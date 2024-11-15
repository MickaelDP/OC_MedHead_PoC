package com.medHead.poc.toMocke;

import com.medHead.poc.entities.Hopital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Service temporaire pour populer et gérer une matrice d'hôpitaux par spécialité.
 * Cette classe sert de placeholder pour la récupération des hôpitaux et de leurs données
 * via des API externes ou une base de données, à remplacer dans les prochaines étapes du projet.
 */
@Service
public class PopulateHopitalService {

    /**
     * Matrice des spécialités, où chaque clé représente un service ID,
     * et chaque valeur est une liste des hôpitaux qui offrent ce service.
     */
    private final Map<Integer, List<Hopital>> specialityMatrix = new HashMap<>();

    /**
     * Service GPS simulé pour assigner des délais aléatoires aux hôpitaux.
     */
    @Autowired
    private GPSService gpsService;

    /**
     * Constructeur par défaut, initialisant la matrice avec des données factices.
     * Les données ici sont statiques et seront remplacées par des appels API ou une base de données dans une version ultérieure.
     */
    public PopulateHopitalService() {
        initializeMatrixWithSampleData();
    }

    /**
     * Initialise la matrice des spécialités avec des données factices pour les tests.
     * Cette méthode simule des hôpitaux avec des services et des coordonnées GPS.
     */
    private void initializeMatrixWithSampleData() {
        List<Hopital> sampleHospitals = List.of(
                new Hopital("Hopital A", List.of(1,2,4,5,6,7,8,11), 48.8566, 2.3522, 15),
                new Hopital("Hopital B", List.of(2,3,4,5,7,9,10,11,12), 48.8648, 2.3499, 8),
                new Hopital("Hopital C", List.of(1,2,3,5,6,7,12), 48.8584, 2.2945, 20),
                new Hopital("Hopital D", List.of(2,3,4,5,9,10,11), 48.8600, 2.3270, 12),
                new Hopital("Hopital E", List.of(1,2,3,4,5,6,7,8,10,11,12), 48.8675, 2.3300, 5),
                new Hopital("Hopital F", List.of(1,2,5,6,7,9,10), 48.8619, 2.3364, 18),
                new Hopital("Hopital G", List.of(1,3,4,5,8,9,11), 48.8545, 2.3478, 10),
                new Hopital("Hopital H", List.of(1,2,3,4,5,6,7,8,12), 48.8550, 2.3419, 7),
                new Hopital("Hopital I", List.of(1,2,3,4,5,6,9,10,11), 48.8590, 2.3540, 25),
                new Hopital("Hopital J", List.of(1,5,6,7,8,10), 48.8534, 2.3488, 9)
        );

        // Ajoute chaque hôpital à la matrice en fonction des services qu'il offre
        for (Hopital hospital : sampleHospitals) {
            for (int serviceId : hospital.getServiceIdsDisponibles()) {
                specialityMatrix
                        .computeIfAbsent(serviceId, k -> new ArrayList<>())
                        .add(hospital);
            }
        }
    }

    /**
     * Récupère la liste des hôpitaux pour un service spécifique.
     * Cette méthode ne simule pas encore les appels API pour mettre à jour les lits disponibles en temps réel.
     *
     * @param serviceId L'ID du service recherché.
     * @return Une liste des hôpitaux offrant ce service, ou une liste vide si aucun n'est trouvé.
     */
    public List<Hopital> getHospitalsByServiceId(int serviceId) {
        return specialityMatrix.getOrDefault(serviceId, new ArrayList<>());
    }

    /**
     * Récupère les hôpitaux pour un service spécifique, triés par délai estimé.
     * Cette méthode utilise le service GPS pour assigner des délais aléatoires aux hôpitaux.
     *
     * @param serviceId L'ID du service recherché.
     * @return Une liste triée des hôpitaux offrant ce service, avec priorité aux hôpitaux ayant des lits disponibles.
     */
    public List<Hopital> getHospitalsByServiceIdSortedByDelai(int serviceId) {
        // Récupère les hôpitaux pour le service donné
        List<Hopital> hospitalsWithService = specialityMatrix.getOrDefault(serviceId, new ArrayList<>());

        // Assigne des délais aléatoires via le service GPS simulé
        gpsService.assignRandomDelays(hospitalsWithService);

        // Filtre les hôpitaux avec des lits disponibles et trie par délai
        return hospitalsWithService.stream()
                .filter(h -> h.getNombreLitDisponible() > 0)
                .sorted(Comparator.comparingInt(Hopital::getDelai))
                .collect(Collectors.toList());
    }

    /**
     * Récupère la matrice actuelle des spécialités pour les tests ou le débogage.
     *
     * @return La matrice des spécialités.
     */
    public Map<Integer, List<Hopital>> getSpecialityMatrix() {
        return specialityMatrix;
    }
}
