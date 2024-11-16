# Changelog

## [v0.002]
### Ajouts
- Implémentation des tests unitaires pour les entités `Patient`, `Hopital`, et `Result` avec JUnit 5.
- Création des tests pour les services associés :
  - `PatientServiceTest`
  - `HopitalServiceTest`
  - `ResultServiceTest`

### Corrections
- Passage des IDs des entités à des UUIDs pour garantir l'unicité globale et corriger les problèmes de conversion de types dans les tests.
- Correction des tests échoués liés à l'ancien système d'IDs longs (`Long`) remplacé par UUIDs dans :
  - `ResultService`

### Améliorations
- Révision complète des fichiers de test pour inclure une meilleure documentation avec des commentaires JavaDoc explicatifs.
- Refactorisation du code pour une meilleure lisibilité et une couverture unitaire suffisante.
- Validation des tests sur les entités et services uniquement à ce stade.

### Remarque
- Les tests des contrôleurs (`PatientControllerTest` et `ResultControllerTest`) sont préparés mais seront intégrés dans la version `v0.003` avec l'introduction de Mockito et MockMvc.

### Statut
- Couverture des tests validée pour toutes les entités et services à ce stade.
- Tous les tests unitaires passent avec succès pour les entités et services.

## [v0.001] -Functionnal backend with REST endpoints and initial test runner

### Added

- Ajout des placeholders pour mocking futur
  - PopulateHopitalService : simulation des données des hôpitaux.
  - GPSService : simulation des délais de trajet entre hôpitaux et patients.
  - ReserveService: simulation de la réservation du lit de l'hôpital selectionné.
  
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

