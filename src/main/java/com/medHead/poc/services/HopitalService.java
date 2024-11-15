package com.medHead.poc.services;

import com.medHead.poc.entities.Hopital;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service pour gérer les hôpitaux.
 * Utilise un stockage en mémoire pour les besoins de démonstration ou de développement.
 */
@Service
public class HopitalService {

    private final List<Hopital> hopitaux = new ArrayList<>();                 // Stockage en mémoire des hôpitaux
    private final AtomicLong idGenerator = new AtomicLong(1);       // Générateur d'ID

    /**
     * Ajoute un nouvel hôpital au stockage en mémoire.
     * @param hopital L'hôpital à ajouter
     * @return L'hôpital ajouté avec un ID unique généré
     */
    public Hopital saveHopital(Hopital hopital) {
        hopital.setId(idGenerator.getAndIncrement());                         // Génère un ID unique
        hopitaux.add(hopital);
        return hopital;
    }

    /**
     * Récupère la liste de tous les hôpitaux.
     * @return Une liste contenant tous les hôpitaux
     */
    public List<Hopital> getAllHopitaux() {
        return hopitaux;
    }

    /**
     * Récupère un hôpital par son ID.
     * @param id L'ID de l'hôpital à rechercher
     * @return Un Optional contenant l'hôpital s'il est trouvé, ou vide sinon
     */
    public Optional<Hopital> getHopitalById(Long id) {
        return hopitaux.stream().filter(h -> h.getId().equals(id)).findFirst();
    }

    /**
     * Récupère un hôpital par son ID.
     * @param id L'ID de l'hôpital à rechercher
     * @return Un Optional contenant l'hôpital s'il est trouvé, ou vide sinon
     */
    public boolean deleteHopital(Long id) {
        return hopitaux.removeIf(h -> h.getId().equals(id));
    }
}
