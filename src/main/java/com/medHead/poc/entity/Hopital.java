package com.medHead.poc.entity;

import java.util.List;

/**
 * Représente un hôpital avec ses services, localisation, et disponibilité.
 * Cette entité est utilisée pour modéliser un hôpital dans le système.
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
     * @param nom Nom de l'hôpital (non null et non vide)
     * @param serviceIdsDisponibles Liste des IDs des services disponibles (non null et non vide)
     * @param latitude Latitude géographique (entre -90 et 90)
     * @param longitude Longitude géographique (entre -180 et 180)
     * @param nombreLitDisponible Nombre de lits disponibles (doit être >= 0)
     * @throws IllegalArgumentException Si un paramètre est invalide
     */
    public Hopital(String nom, List<Integer> serviceIdsDisponibles, double latitude, double longitude, int nombreLitDisponible) {
        setNom(nom);
        setServiceIdsDisponibles(serviceIdsDisponibles);
        setLatitude(latitude);
        setLongitude(longitude);
        setNombreLitDisponible(nombreLitDisponible);
    }

    // Getters et setters avec validations

    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'hôpital.
     * @param id Identifiant unique (doit être > 0).
     * @throws IllegalArgumentException Si l'identifiant est <= 0.
     */
    public void setId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("L'ID doit être supérieur à 0.");
        }
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'hôpital.
     * @param nom Nom de l'hôpital (non null et non vide).
     * @throws IllegalArgumentException Si le nom est null ou vide.
     */
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'hôpital ne peut pas être null ou vide.");
        }
        this.nom = nom;
    }

    public List<Integer> getServiceIdsDisponibles() {
        return serviceIdsDisponibles;
    }

    /**
     * Définit la liste des services disponibles.
     * @param serviceIdsDisponibles Liste des IDs des services (non null et non vide).
     * @throws IllegalArgumentException Si la liste est null ou vide.
     */
    public void setServiceIdsDisponibles(List<Integer> serviceIdsDisponibles) {
        if (serviceIdsDisponibles == null || serviceIdsDisponibles.isEmpty()) {
            throw new IllegalArgumentException("La liste des services disponibles ne peut pas être null ou vide.");
        }
        this.serviceIdsDisponibles = serviceIdsDisponibles;
    }

    public double getLatitude() {
        return latitude;
    }

    /**
     * Définit la latitude géographique.
     * @param latitude Latitude (doit être entre -90 et 90).
     * @throws IllegalArgumentException Si la latitude est hors des limites [-90, 90].
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
     * Définit la longitude géographique.
     * @param longitude Longitude (doit être entre -180 et 180).
     * @throws IllegalArgumentException Si la longitude est hors des limites [-180, 180].
     */
    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("La longitude doit être comprise entre -180 et 180.");
        }
        this.longitude = longitude;
    }

    public int getNombreLitDisponible() {
        return nombreLitDisponible;
    }

    /**
     * Définit le nombre de lits disponibles.
     * @param nombreLitDisponible Nombre de lits (doit être >= 0).
     * @throws IllegalArgumentException Si le nombre est négatif.
     */
    public void setNombreLitDisponible(int nombreLitDisponible) {
        if (nombreLitDisponible < 0) {
            throw new IllegalArgumentException("Le nombre de lits disponibles ne peut pas être négatif.");
        }
        this.nombreLitDisponible = nombreLitDisponible;
    }

    public int getDelai() {
        return delai;
    }

    /**
     * Définit le délai estimé pour atteindre l'hôpital.
     * @param delai Délai en minutes (doit être >= 0).
     * @throws IllegalArgumentException Si le délai est négatif.
     */
    public void setDelai(int delai) {
        if (delai < 0) {
            throw new IllegalArgumentException("Le délai ne peut pas être négatif.");
        }
        this.delai = delai;
    }

    @Override
    public String toString() {
        return "Hopital{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", serviceIdsDisponibles=" + serviceIdsDisponibles +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nombreLitDisponible=" + nombreLitDisponible +
                ", delai=" + delai +
                '}';
    }
}
