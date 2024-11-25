package com.medHead.poc.model;

import java.util.UUID;

/**
 * Représente le résultat d'une recherche d'hôpital pour un patient spécifique,
 * incluant des informations sur la spécialité demandée, le délai pour atteindre
 * l'hôpital, et la disponibilité des ressources.
 */
public class Result {
    private UUID id;                      // Identifiant unique du résultat
    private UUID patientId;               // Identifiant unique du patient concerné
    private String specialite;            // Nom de la spécialité demandée pour le patient
    private String hopitalNom;            // Nom de l'hôpital sélectionné
    private int delai;                    // Temps estimé (en minutes) pour atteindre l'hôpital
    private boolean specialiteDisponible; // Indique si la spécialité est disponible dans l'hôpital
    private boolean litReserve;           // Indique si un lit a été réservé avec succès

    // Constructeurs
    public Result() {
        this.id = UUID.randomUUID();
    }

    /**
     * Constructeur avec tous les paramètres nécessaires, sauf l'identifiant.
     * L'identifiant est généré automatiquement.
     * @param patientId Identifiant du patient
     * @param specialite Spécialité demandée par le patient
     * @param hopitalNom Nom de l'hôpital sélectionné
     * @param delai Temps estimé pour atteindre l'hôpital (en minutes)
     * @param specialiteDisponible Indique si la spécialité est disponible
     * @param litReserve Indique si un lit a été réservé avec succès
     */
    public Result(UUID patientId, String specialite, String hopitalNom, int delai, boolean specialiteDisponible, boolean litReserve) {
        this.id = UUID.randomUUID();      // Génération d'un UUID unique
        setPatientId(patientId);
        setSpecialite(specialite);
        setHopitalNom(hopitalNom);
        setDelai(delai);
        setSpecialiteDisponible(specialiteDisponible);
        setLitReserve(litReserve);
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant unique (UUID) ne peut pas être null.");
        }
        this.id = id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    /**
     * Définit l'identifiant unique du patient concerné par ce résultat.
     *
     * @param patientId L'identifiant unique du patient, qui doit être un nombre positif non nul.
     * @throws IllegalArgumentException si l'identifiant est null ou négatif.
     */
    public void setPatientId(UUID patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("L'identifiant du patient doit être un nombre positif non nul.");
        }
        this.patientId = patientId;
    }

    public String getSpecialite() {
        return specialite;
    }

    /**
     * Définit la spécialité demandée par le patient pour laquelle le résultat a été généré.
     *
     * @param specialite La spécialité demandée, qui ne peut pas être null ou vide.
     * @throws IllegalArgumentException si la spécialité est null ou vide.
     */
    public void setSpecialite(String specialite) {
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité demandée ne peut pas être vide ou null.");
        }
        this.specialite = specialite;
    }

    public String getHopitalNom() {
        return hopitalNom;
    }

    /**
     * Définit le nom de l'hôpital sélectionné pour le patient.
     *
     * @param hopitalNom Le nom de l'hôpital, qui ne peut pas être null ou vide.
     * @throws IllegalArgumentException si le nom de l'hôpital est null ou vide.
     */
    public void setHopitalNom(String hopitalNom) {
        if (hopitalNom == null || hopitalNom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'hôpital sélectionné ne peut pas être vide ou null.");
        }
        this.hopitalNom = hopitalNom;
    }

    public int getDelai() {
        return delai;
    }

    /**
     * Définit le délai estimé pour atteindre l'hôpital sélectionné.
     *
     * @param delai Le délai en minutes, qui doit être un nombre positif ou nul.
     * @throws IllegalArgumentException si le délai est négatif.
     */
    public void setDelai(int delai) {
        if (delai < 0) {
            throw new IllegalArgumentException("Le délai estimé ne peut pas être négatif.");
        }
        this.delai = delai;
    }

    public boolean isSpecialiteDisponible() {
        return specialiteDisponible;
    }

    /**
     * Définit si la spécialité demandée est disponible dans l'hôpital sélectionné.
     *
     * @param specialiteDisponible true si la spécialité est disponible, sinon false.
     */
    public void setSpecialiteDisponible(boolean specialiteDisponible) {
        this.specialiteDisponible = specialiteDisponible;
    }

    public boolean isLitReserve() {
        return litReserve;
    }

    /**
     * Définit si un lit a été réservé avec succès dans l'hôpital sélectionné.
     *
     * @param litReserve true si un lit a été réservé, sinon false.
     */
    public void setLitReserve(boolean litReserve) {
        this.litReserve = litReserve;
    }

    @Override
    public String toString() {
        return "Result {" +
                "id=" + id +
                ", patientId=" + patientId +
                ", spécialité='" + specialite + '\'' +
                ", hôpital='" + hopitalNom + '\'' +
                ", délai=" + delai + " minutes" +
                ", spécialité disponible=" + (specialiteDisponible ? "Oui" : "Non") +
                ", lit réservé=" + (litReserve ? "Oui" : "Non") +
                '}';
    }
}
