package com.medHead.poc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.UUID;

/**
 * Représente le résultat d'une recherche d'hôpital pour un patient spécifique,
 * incluant des informations sur la spécialité demandée, le délai pour atteindre
 * l'hôpital, et la disponibilité des ressources.
 */
public class Result {
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("APP_FILE");

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
        logger.info(APP_MARKER, "Création d'un résultat avec ID généré : {}", id);
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
        logger.info(APP_MARKER, "Résultat créé pour le patient avec ID : {} et hôpital : {}", patientId, hopitalNom);
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            logger.error(APP_MARKER, "Tentative de définir un ID null pour le résultat.");
            throw new IllegalArgumentException("L'identifiant unique (UUID) ne peut pas être null.");
        }
        this.id = id;
        logger.info(APP_MARKER, "ID du résultat défini : {}", id);
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
            logger.error(APP_MARKER, "Tentative de définir un patientId null.");
            throw new IllegalArgumentException("L'identifiant du patient doit être un nombre positif non nul.");
        }
        this.patientId = patientId;
        logger.info(APP_MARKER, "ID du patient dans le résultat défini : {}", patientId);
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
            logger.error(APP_MARKER, "Tentative de définir une spécialité vide ou nulle.");
            throw new IllegalArgumentException("La spécialité demandée ne peut pas être vide ou null.");
        }
        this.specialite = specialite;
        logger.info(APP_MARKER, "Spécialité du patient définie : {}", specialite);
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
            logger.error(APP_MARKER, "Tentative de définir un nom d'hôpital vide ou nulle.");
            throw new IllegalArgumentException("Le nom de l'hôpital sélectionné ne peut pas être vide ou null.");
        }
        this.hopitalNom = hopitalNom;
        logger.info(APP_MARKER, "Nom de l'hôpital défini : {}", hopitalNom);
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
            logger.error(APP_MARKER, "Tentative de définir un délai négatif : {}", delai);
            throw new IllegalArgumentException("Le délai estimé ne peut pas être négatif.");
        }
        this.delai = delai;
        logger.info(APP_MARKER, "Délai pour l'hôpital défini : {} minutes", delai);
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
        logger.info(APP_MARKER, "Spécialité disponible dans l'hôpital : {}", specialiteDisponible ? "Oui" : "Non");
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
        logger.info(APP_MARKER, "Lit réservé dans l'hôpital : {}", litReserve ? "Oui" : "Non");
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
