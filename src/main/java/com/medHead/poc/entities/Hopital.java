package com.medHead.poc.entities;

import java.util.List;

/**
 * Représente un hôpital avec ses services, localisation, et disponibilité.
 */
public class Hopital {
    private Long id;                                // Identifiant unique de l'hôpital (auto-généré)
    private String nom;                             // Nom de l'hôpital
    private List<Integer> serviceIdsDisponibles;    // Liste des ID des services disponibles dans cet hôpital
    private double latitude;                        // Latitude géographique de l'hôpital
    private double longitude;                       // Longitude géographique de l'hôpital
    private int nombreLitDisponible;                // Nombre de lits disponibles dans l'hôpital
    private int delai = 9999;                       // Délai estimé en minutes pour atteindre cet hôpital (par défaut : 9999, non défini)


    // Constructeurs

    public Hopital() {}

    /**
     * Constructeur avec paramètres.
     * @param nom Nom de l'hôpital
     * @param serviceIdsDisponibles Liste des IDs des services disponibles
     * @param latitude Latitude géographique
     * @param longitude Longitude géographique
     * @param nombreLitDisponible Nombre de lits disponibles
     */
    public Hopital(String nom, List<Integer> serviceIdsDisponibles, double latitude, double longitude, int nombreLitDisponible) {
        this.nom = nom;
        this.serviceIdsDisponibles = serviceIdsDisponibles;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nombreLitDisponible = nombreLitDisponible;
    }

    // getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Integer> getServiceIdsDisponibles() {
        return serviceIdsDisponibles;
    }

    public void setServiceIdsDisponibles(List<Integer> serviceIdsDisponibles) {
        this.serviceIdsDisponibles = serviceIdsDisponibles;
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

    public int getNombreLitDisponible() {
        return nombreLitDisponible;
    }

    public void setNombreLitDisponible(int nombreLitDisponible) {
        this.nombreLitDisponible = nombreLitDisponible;
    }

    public int getDelai() {
        return delai;
    }

    public void setDelai(int delai) {
        this.delai = delai;
    }
}
