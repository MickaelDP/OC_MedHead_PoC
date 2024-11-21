package com.medHead.poc.model;
import java.util.UUID;

/**
 * Représente une prise en charge urgente d'un patient et les informations nécessaires à son aiguillage.
 * Cette classe est utilisée pour gérer les données d'un patient dans le cadre d'un processus d'orientation
 * vers un service ou un établissement de soins.
 */
public class Patient {
    private UUID id;                                                            // Identifiant unique du patient
    private int serviceId = 0;                                                  // ID du service requis pour la prise en charge (défaut : 0)
    private String specialite;                                                  // Spécialité ou type de soin requis par le patient
    private String responsable;                                                 // Nom ou identifiant du responsable de la prise en charge
    private String qualite;                                                     // Qualité ou spécialité du responsable
    private double latitude;                                                    // Latitude géographique du patient
    private double longitude;                                                   // Longitude géographique du patient

    /**
     * Constructeur par défaut.
     * Initialise un patient avec un identifiant unique généré automatiquement.
     */
    public Patient() {
        this.id = UUID.randomUUID();
    }

    /**
     * Constructeur permettant de créer un patient avec des informations principales en plus de son identifiant.
     *
     * @param specialite  Spécialité ou soin requis par le patient
     * @param responsable Nom du responsable de la prise en charge
     * @param qualite     Qualité ou spécialité du responsable
     * @param latitude    Latitude géographique du patient
     * @param longitude   Longitude géographique du patient
     * @throws IllegalArgumentException si latitude ou longitude est invalide.
     */
    public Patient(String specialite, String responsable, String qualite, double latitude, double longitude) {
        this.id = UUID.randomUUID();
        setSpecialite(specialite);                                              // Validation via setter
        setResponsable(responsable);                                            // Validation via setter
        setQualite(qualite);                                                    // Validation via setter
        setLatitude(latitude);                                                  // Validation via setter
        setLongitude(longitude);                                                // Validation via setter
    }


    // Getters et Setters
    public UUID getId() {
        return id;
    }

    /**
     * Définit un nouvel identifiant pour le patient.
     *
     * @param id Identifiant unique du patient.
     * @throws IllegalArgumentException si l'ID est nul ou non positif.
     */
    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID doit être un entier positif non nul.");
        }
        this.id = id;
    }


    public int getServiceId() {
        return serviceId;
    }

    /**
     * Définit l'ID du service requis pour la prise en charge du patient.
     * L'ID doit être supérieur à 0, sinon une exception sera levée.
     *
     * @param serviceId ID du service requis.
     * @throws IllegalArgumentException si l'ID est négatif ou nul.
     */
    public void setServiceId(int serviceId) {
        if (serviceId <= 0) {
            throw new IllegalArgumentException("L'ID du service doit être positif.");
        }
        this.serviceId = serviceId;
    }

    public String getSpecialite() {
        return specialite;
    }

    /**
     * Définit la spécialité ou le type de soin requis par le patient.
     *
     * @param specialite Spécialité ou soin requis.
     * @throws IllegalArgumentException si la spécialité est vide ou nulle.
     */
    public void setSpecialite(String specialite) {
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide ou nulle.");
        }
        this.specialite = specialite;
    }

    public String getResponsable() {
        return responsable;
    }

    /**
     * Définit le nom ou l'identifiant du responsable de la prise en charge.
     *
     * @param responsable Nom ou identifiant du responsable.
     * @throws IllegalArgumentException si le responsable est vide ou nul.
     */
    public void setResponsable(String responsable) {
        if (responsable == null || responsable.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du responsable ne peut pas être vide ou nul.");
        }
        this.responsable = responsable;
    }

    public String getQualite() {
        return qualite;
    }

    /**
     * Définit la qualité ou spécialité du responsable.
     *
     * @param qualite Qualité ou spécialité du responsable.
     * @throws IllegalArgumentException si la qualité est vide ou nulle.
     */
    public void setQualite(String qualite) {
        if (qualite == null || qualite.trim().isEmpty()) {
            throw new IllegalArgumentException("La qualité ne peut pas être vide ou nulle.");
        }
        this.qualite = qualite;
    }

    public double getLatitude() {
        return latitude;
    }

    /**
     * Définit la latitude géographique du patient.
     * La latitude doit être comprise entre -90 et 90.
     *
     * @param latitude Latitude géographique.
     * @throws IllegalArgumentException si la latitude est hors des limites valides.
     */
    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("La latitude doit être comprise entre -90 et 90.");
        }
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Définit la longitude géographique du patient.
     * La longitude doit être comprise entre -180 et 180.
     *
     * @param longitude Longitude géographique.
     * @throws IllegalArgumentException si la longitude est hors des limites valides.
     */
    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180.");
        }
        this.longitude = longitude;
    }

    /**
     * Retourne une représentation textuelle des informations du patient.
     *
     * @return Chaîne de caractères représentant les informations du patient.
     */
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", serviceId=" + serviceId +
                ", specialite='" + specialite + '\'' +
                ", responsable='" + responsable + '\'' +
                ", qualite='" + qualite + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
