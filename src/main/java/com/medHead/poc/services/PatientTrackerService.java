package com.medHead.poc.services;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

/**
 * Classe utilitaire pour suivre les patients en cours de traitement.
 * Utilise une structure concurrente pour gérer les appels simultanés.
 */
@Component
public class PatientTrackerService {
    private final Set<UUID> processingPatients = ConcurrentHashMap.newKeySet();

    /**
     * Ajoute l'ID d'un patient en cours de traitement.
     *
     * @param patientId L'ID du patient.
     * @return true si l'ID a été ajouté, false si le patient est déjà en traitement.
     */
    public boolean startProcessing(UUID patientId) {
        if (processingPatients.contains(patientId)) {
            throw new RuntimeException("Patient déjà en cours de traitement");
        }
        return processingPatients.add(patientId);
    }
    /**
     * Retire l'ID d'un patient après traitement.
     *
     * @param patientId L'ID du patient.
     */
    public void endProcessing(UUID patientId) {
        processingPatients.remove(patientId);
    }

    /**
     * Vérifie si un patient est en cours de traitement.
     *
     * @param patientId L'ID du patient.
     * @return true si le patient est en traitement, sinon false.
     */
    public boolean isProcessing(UUID patientId) {
        return processingPatients.contains(patientId);
    }
}
