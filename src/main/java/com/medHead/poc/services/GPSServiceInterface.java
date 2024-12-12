package com.medHead.poc.services;

public interface GPSServiceInterface {
    // Déclarez ici les méthodes que vous souhaitez exposer à travers l'interface
    int getTravelDelay(double patientLatitude, double patientLongitude, double hospitalLatitude, double hospitalLongitude);
}
