# Changelog

## [v0.001] -Functionnal backend with REST endpoints and initial test runner

### Added

- Ajout des placeholders pour mocking futur
  - PopulateHopitalService : simulation des données des hôpitaux.
  - GPSService : simulation des délais de trajet entre hôpitaux et patients.
  - ReserveSerice: simulation de la réservation du lit de l'hôpital selectionné.
  - 
- Création des endpoints REST (CRUD) pour :
  - Patients (PatientController): Point d'entrée principal
  - Résultats (ResultController): Point de sortie principal
 
- Core services:
  - PatientService
  - HopitalService
  - ResultService
  
- Ajout d'un CommandLineRunner pour exécuter un test initial :
  - Validation des flux : ajout, suppression, recherche, sélection d'un hôpital.
  - Simulation de réservation et décrémentation des lits disponibles etc.

- Structure du projet prête pour extensions futures :
  - Mocking des services externes.
  - Ajout de tests unitaires et de non-régression.

- Documentation minimale incluse pour guider le développement futur.

