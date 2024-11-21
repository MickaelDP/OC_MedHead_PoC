# Changelog
## [v0.003] - Refactorisation et Adoption des Standards Spring

### Refactorisation

- **Adoption des standards Spring** :
  - Revue complète de l'architecture des services pour mieux respecter les conventions Spring (separation of concerns, annotations, injection de dépendances).
  - Refactorisation des contrôleurs pour une meilleure conformité aux pratiques REST :
    - `PatientController` : Clarification des endpoints et gestion améliorée des exceptions.
    - `ResultController` : Gestion des réponses standardisées et meilleure documentation des points d'entrée.

- **Révision des services** :
  - `PatientService` et `ResultService` ont été adaptés pour centraliser les validations métier.
  - `HopitalService` : Optimisation de la logique de tri et ajout de validations.

- **Uniformisation des entités** :
  - Les entités (`Patient`, `Hopital`, `Result`) ont été standardisées pour utiliser des UUIDs comme identifiants uniques.
  - Ajustement des constructeurs pour éviter les cas où des validations sont déjà vérifiées au niveau du service.

### Implémentations principales

- **Complétion de la logique backend** :
  - Tous les endpoints REST sont fonctionnels et permettent de simuler des interactions API réalistes.
  - Ajout d'une gestion centralisée des exceptions via un `AdviceController`.

- **Tests d'intégration avec Spring MockMvc** :
  - Tests approfondis pour valider les scénarios d'utilisation via les contrôleurs.
  - Ajout de `MockMvc` pour simuler les requêtes HTTP et valider les réponses des endpoints REST.

- **Couverture des tests avec Jacoco** :
  - Vérification d’une couverture de code de 94 %.
  - Tests d'exception, flux standards et cas limites validés.

### Tests

- **Tests unitaires** :
  - Validation complète des entités avec des tests ciblés sur les validations.
  - Renforcement des tests de service pour inclure davantage de scénarios métier (valides et non valides).

- **Tests d'intégration** :
  - Tests pour tous les contrôleurs via `MockMvc` :
    - `PatientControllerITest`
    - `ResultControllerITest`
  - Scénarios réalistes simulés avec des données valides et invalides.

### Documentation

- **Documentation améliorée** :
  - Ajout de JavaDoc pour toutes les classes et méthodes critiques.
  - Explication des étapes pour exécuter les tests avec Spring et générer le rapport de couverture Jacoco.

### Statut

- **Finalisation backend** :
  - La logique backend est complètement implémentée, permettant une simulation réaliste des échanges API REST.
  - Tous les tests unitaires et d'intégration passent avec succès.
  - Couverture confirmée à 94 % avec Jacoco.

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

