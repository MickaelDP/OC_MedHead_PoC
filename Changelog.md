# Changelog

## [v0.005] - Optimisation des performances et robustesse du backend

### **Optimisations fonctionnelles**

- **Mise en cache des requêtes fréquentes** :
  - Introduction d'un mécanisme de mise en cache pour les réponses souvent identiques, telles que la liste des hôpitaux par service.
  - Implémentation d'un cache local concurrent (`ConcurrentHashMap`) pour optimiser les performances en évitant des traitements répétitifs inutiles.

- **Gestion de la concurrence** :
  - Révision de la gestion des threads pour garantir la sécurité des accès simultanés et éviter les conflits dans les processus critiques.

### **Validation des tests**

- **Tests unitaires et d'intégration** :
  - Les nouvelles fonctionnalités, comme la mise en cache et la gestion de la concurrence, ont fait l'objet d'une révision complète via les tests unitaires et d'intégration.
  - Introduction de tests supplémentaires pour valider les cas spécifiques :
    - Vérification des performances du cache.
    - Tests de suppression et re-remplissage du cache.
  - Couverture de code maintenue à un niveau élevé avec Jacoco.

- **Tests de performance** :
  - Tests de charge effectués avec **JMeter** (environnement externe en raison de limitations de compatibilité des versions).
  - **Résultats des tests de performance** :
    - **Throughput** : 800 requêtes/secondes.
    - Temps de réponse moyen inférieur à **200 ms** pour les requêtes principales.
    - Délai moyen d'attribution des patients : **< 12 minutes**.
    - Taux de succès des affectations correctes : **+ de 90%**.

### **Résultats et robustesse**

- **Amélioration de la robustesse** :
  - Le code est désormais adapté aux contraintes réelles d'un environnement web.
  - Les tests de charge confirment la capacité du backend à gérer une charge élevée avec des performances stables.

- **Préparation à l'intégration front-end** :
  - Le backend est prêt pour une intégration avec une interface utilisateur basée sur Angular ou une autre technologie front-end.

### **Statut**

- **Stabilité** :
  - Le backend atteint une stabilité suffisante pour les scénarios de production simulés.
  - Tous les tests passent avec succès, garantissant un comportement conforme aux attentes.


## [v0.004] - Sécurisation et documentation avec HTTPS & Swagger

### **Sécurisation**

- **Mise en place de HTTPS** :
  - Activation de HTTPS pour sécuriser les échanges avec l'API.
  - Ajout des fichiers nécessaires dans le dossier `resources` :
    - `keystore.p12` : Keystore contenant le certificat auto-signé.
    - `selfsigned.crt` : Certificat auto-signé pour usage local.
  - Configuration dans `application.properties` pour utiliser le keystore avec le mot de passe et activer HTTPS :
    - `server.ssl.enabled=true`.
    - `server.ssl.key-store` et `server.ssl.key-store-password` ajoutés.

- **Configuration de Tomcat** :
  - Limitation du nombre maximal de threads pour renforcer la sécurité et l'évolutivité :
    - `server.tomcat.threads.max=300`.
    - `server.tomcat.threads.min-spare=100`.

### **Documentation avec Swagger**

- **Activation de SpringDoc OpenAPI** :
  - Activation et configuration de Swagger pour documenter les endpoints REST.
  - Ajout de la classe `SwaggerConfig` pour personnaliser la documentation de l'API :
    - **Titre** : `API Documentation`.
    - **Version** : `1.0`.
    - **Description** : `Documentation pour l'API MedHead`.

- **Accès à Swagger** :
  - Documentation de l'API disponible aux URL suivantes :
    - **Documentation JSON** : [`https://localhost:8443/v3/api-docs`](https://localhost:8443/v3/api-docs).
    - **Interface utilisateur Swagger** : [`https://localhost:8443/swagger-ui.html`](https://localhost:8443/swagger-ui.html).

### **Postman**

- **Export de la collection Postman** :
  - Export de la collection de requêtes Postman au format v2.1 pour simplifier le test de l'API.
  - Fichier ajouté dans `resources` :
    - `MedHead API.postman_collection.json`.

- **Requêtes testées et validées** :
  - Les endpoints principaux de l'API ont été testés avec Postman pour garantir leur bon fonctionnement en HTTPS.

### **Statut**

- **Stabilité** :
  - Version stable pour le backend, permettant des interactions sécurisées avec l'API via HTTPS.
  - Tous les tests unitaires et d'intégration sont réussis avec cette configuration.

- **Couverture** :
  - Les modifications liées à HTTPS et Swagger n'ont pas affecté la couverture de code.

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

