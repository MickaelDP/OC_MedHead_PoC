package com.medHead.poc.entities;

import java.util.UUID;

/**
 * Représente le résultat d'une recherche d'hôpital pour un patient spécifique,
 * incluant des informations sur la spécialité demandée, le délai pour atteindre
 * l'hôpital, et la disponibilité des ressources.
 */
public class Result {
    private UUID id;                      // Identifiant unique du résultat
    private Long patientId;               // Identifiant unique du patient concerné
    private String specialite;            // Nom de la spécialité demandée pour le patient
    private String hopitalNom;            // Nom de l'hôpital sélectionné
    private int delai;                    // Temps estimé (en minutes) pour atteindre l'hôpital
    private boolean specialiteDisponible; // Indique si la spécialité est disponible dans l'hôpital
    private boolean litDisponible;        // Indique si un lit est disponible dans l'hôpital

    // Constructeurs

    public Result() {
    }

    /**
     * Constructeur avec tous les paramètres nécessaires, sauf l'identifiant.
     * L'identifiant est généré automatiquement.
     * @param patientId Identifiant du patient
     * @param specialite Spécialité demandée par le patient
     * @param hopitalNom Nom de l'hôpital sélectionné
     * @param delai Temps estimé pour atteindre l'hôpital (en minutes)
     * @param specialiteDisponible Indique si la spécialité est disponible
     * @param litDisponible Indique si un lit est disponible
     */
    public Result(Long patientId, String specialite, String hopitalNom, int delai, boolean specialiteDisponible, boolean litDisponible) {
        this.id = UUID.randomUUID();      // Génération d'un UUID
        this.patientId = patientId;
        this.specialite = specialite;
        this.hopitalNom = hopitalNom;
        this.delai = delai;
        this.specialiteDisponible = specialiteDisponible;
        this.litDisponible = litDisponible;
    }

    /**
     * Méthode statique pour générer un identifiant unique.
     * À remplacer par une solution plus robuste avec persistance si nécessaire.
     * @return Identifiant unique basé sur l'horodatage
     */
    private static Long generateUniqueId() {
        return System.currentTimeMillis();  // Simple exemple d'ID unique basé sur le timestamp
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getHopitalNom() {
        return hopitalNom;
    }

    public void setHopitalNom(String hopitalNom) {
        this.hopitalNom = hopitalNom;
    }

    public int getDelai() {
        return delai;
    }

    public void setDelai(int delai) {
        this.delai = delai;
    }

    public boolean isSpecialiteDisponible() {
        return specialiteDisponible;
    }

    public void setSpecialiteDisponible(boolean specialiteDisponible) {
        this.specialiteDisponible = specialiteDisponible;
    }

    public boolean isLitDisponible() {
        return litDisponible;
    }

    public void setLitDisponible(boolean litDisponible) {
        this.litDisponible = litDisponible;
    }
}
