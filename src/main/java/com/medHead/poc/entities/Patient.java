package com.medHead.poc.entities;

/**
 * Représente un patient et les informations nécessaires à sa prise en charge.
 */
public class Patient {
    private Long id;                            // Identifiant unique du patient
    private int serviceId = 0;                  // ID du service requis pour la prise en charge (défaut : 0)
    private String specialite;                  // Spécialité ou type de soin requis par le patient
    private String responsable;                 // Nom ou identifiant du responsable de la prise en charge
    private String qualite;                     // Qualité ou spécialité du responsable
    private double latitude;                    // Latitude géographique du patient
    private double longitude;                   // Longitude géographique du patient

    // Constructeurs

    public Patient() {
    }

    /**
     * Constructeur avec paramètres principaux.
     * @param specialite Spécialité ou soin requis
     * @param responsable Nom du responsable de prise en charge
     * @param qualite Qualité ou spécialité du responsable
     * @param latitude Latitude géographique du patient
     * @param longitude Longitude géographique du patient
     */
    public Patient(String specialite, String responsable, String qualite, double latitude, double longitude) {
        this.specialite = specialite;
        this.responsable = responsable;
        this.qualite = qualite;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId){
        this.serviceId = serviceId;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
