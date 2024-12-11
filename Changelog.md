# Changelog
## [v1.0.1] - Correction et amélioration de l'intégration CI/CD et des tokens JWT et CSRF

Cette version apporte des ajustements importants à l'intégration des tokens JWT et CSRF ainsi qu'à l'amélioration de l'utilisation du pipeline CI/CD pour l'application de réservation de lits hospitaliers. Elle vise à renforcer la sécurité des échanges via des tokens et à mieux séparer les étapes de build et de test dans le processus d'intégration continue.

### **Nouveautés**

#### **Sécurisation avec JWT et CSRF**
- **Gestion des tokens JWT et CSRF** :
  - Intégration des tokens JWT et CSRF pour sécuriser les échanges entre le frontend et le backend.
  - Mise en place d'un filtre d'authentification JWT et de gestion des tokens CSRF dans le backend Spring Boot.
  - Ajout d'un contrôleur pour la récupération des tokens CSRF (`/test/csrf`).

#### **CI/CD avec GitHub Actions**
- **Optimisation du pipeline CI/CD** :
  - Séparation des étapes de build et de tests dans le pipeline.
  - Les tests ne sont plus exécutés à chaque push, mais peuvent être lancés manuellement via l'interface GitHub avec `workflow_dispatch`.
  - Le build et le test du backend sont désormais distincts et gérés de manière plus précise.
  - Introduction de la validation manuelle des tests via GitHub Actions, permettant de mieux contrôler les actions à effectuer.

## [v1.0.0] - Proof of Concept (PoC) Opérationnel

Cette version marque la finalisation et l'opérationnalité complète du PoC pour l'application de réservation de lits hospitaliers. Elle inclut toutes les fonctionnalités principales, un frontend Angular entièrement fonctionnel, un backend Spring Boot robuste, ainsi qu'une intégration complète de la CI/CD pour assurer le suivi des tests et des déploiements.

### **Nouveautés**
- CI/CD avec GitHub Actions :

  - **Automatisation complète du build, des tests, et de la génération des rapports de couverture de code.**
  - **Détection des erreurs de build et tests échoués dans le pipeline.**
---

## [v0.201] - Initialisation du pipeline CI/CD

### **Ajouts**

#### **Configuration du pipeline CI/CD**
- Création d'un fichier **ci-cd.yml** pour la mise en place d'un pipeline CI/CD automatisé via GitHub Actions.
- Automatisation des étapes suivantes dans le pipeline :
  - **Build Backend** : Compilation du projet backend Maven avec tests unitaires.
  - **Build Frontend** : Construction de l'application Angular avec exécution des tests.
  - **Déploiement Docker** (préparation pour future intégration).
- Validation automatique des commits poussés sur la branche `dev`.

#### **Documentation**
- Inclusion des fichiers `.github/workflows/CiCd.yml` dans le dépôt avec une explication succincte des étapes de configuration.

---
---

## [v0.102] - Couverture des tests et validation du frontend Angular

### **Ajouts**

#### **Tests unitaires et d'intégration frontend**
- Ajout de tests unitaires et d'intégration pour améliorer la robustesse et la couverture du code :
  - **ReservationComponent** :
    - Validation des soumissions de formulaire valides avec des spécialités existantes.
    - Gestion des cas de spécialités invalides avec affichage du popup d’urgence.
    - Simulation de la fermeture du popup et réinitialisation des champs du formulaire.
    - Gestion des erreurs réseau simulées avec des espions sur `fetch` :
      - Vérification des erreurs de type `Network Error`.
      - Gestion des réponses inattendues comme `404` et JSON mal formaté.
    - Réservation d'urgence : simulation du cas où l'utilisateur réserve après une spécialité invalide.
    - Tests de capitalisation correcte des spécialités (e.g., `"médecine générale"` devient `"Médecine générale"`).
  - **AppComponent** :
    - Vérification de la création de l'application Angular.
    - Validation des redirections des routes :
      - Redirection des chemins vides vers `/reservation`.
      - Gestion des routes inconnues avec redirection vers `/home`.
    - Tests de navigation entre `/reservation` et `/home` pour s'assurer du bon fonctionnement des routes.

#### **Commandes de tests**
- Ajout de la commande `ng test --code-coverage` pour exécuter les tests avec un rapport de couverture.

#### **Couverture du code**
- Amélioration significative de la couverture des tests :
  - **Statements** : 82.53% (52/63)
  - **Branches** : 63.63% (7/11)
  - **Functions** : 95.23% (20/21)
  - **Lines** : 81.96% (50/61)

---

### **Correctifs**

- **Gestion des cas d’erreur** :
  - Vérification et gestion des erreurs réseau (`fetch`) :
    - Ajout d'alertes et de messages d'erreur pour notifier les utilisateurs en cas de problème de connexion.
    - Réinitialisation de l'état en cas d'erreur lors de la réservation.
  - Meilleure gestion des JSON mal formatés ou des réponses inattendues du backend.

- **Refactorisation des tests** :
  - Simplification et uniformisation des tests pour éviter les répétitions inutiles.
  - Ajout de commentaires explicatifs dans les fichiers de tests pour en faciliter la maintenance.

---

### **Statut**

#### **Stabilité**
- La version v0.102 est stable avec une couverture de tests élevée.
- Le frontend Angular est robuste et bien testé, prêt pour une intégration complète avec le backend.

#### **Prochaine étape**
- Développement des tests supplémentaires pour des cas métier complexes si nécessaire.
- Optimisation des performances et des tests avant déploiement en production.

---

## [v0.101-] - Implémentation initiale du frontend Angular

### **Frontend Angular**

- **Création du projet Angular** :
  - Mise en place d'un projet Angular standalone pour la gestion du frontend.
  - Implémentation des composants principaux :
    - `ReservationComponent` : Permet la saisie des informations utilisateur et la gestion des réservations.

- **Fonctionnalités principales** :
  - **Formulaire interactif** :
    - Champ d'entrée pour saisir une spécialité avec autocomplétion dynamique.
    - Gestion des suggestions basées sur un fichier JSON de spécialités.
    - Validation des entrées utilisateur avec messages d'erreur.
  - **Popups contextuels** :
    - Popup de confirmation de réservation avec les détails envoyés à l'API backend.
    - Popup d'urgence pour gérer les cas de spécialités invalides ou inconnues :
      - Proposition de réserver en "Médecine d'urgence".
  - **Interactions utilisateur** :
    - Boutons interactifs avec hover pour une meilleure expérience utilisateur.
    - Gestion des états du formulaire : désactivation en cas de popups actifs.

- **Intégration API** :
  - Envoi des données utilisateur au backend via des requêtes **fetch**.
  - Gestion des réponses et affichage dynamique dans le popup de confirmation.

- **Design et expérience utilisateur** :
  - Mise en place d'un design moderne avec **SCSS** :
    - Utilisation de dégradés pour le fond.
    - Boutons cohérents avec le thème principal.
    - Style des suggestions et des popups pour une meilleure lisibilité.
  - Overlay pour bloquer les interactions avec le reste de la page en cas de popup actif.

- **Tests et validation** :
  - Fonctionnalité testée en environnement de développement local avec interactions simulées manuellement.
  - Prêt pour intégration avec le backend.

### **Statut**

- **Stabilité** :
  - Le frontend est stable et prêt pour des interactions complètes avec le backend.
- **Prochaine étape** :
  - Optimisations du frontend basées sur les retours utilisateurs et intégration avec des tests automatisés.

---

## [v0.1] - Backend finalisé sur la branche `main`

### **Finalisation Backend**

- **Implémentation complète** :
  - Le backend est finalisé avec tous les services critiques et endpoints REST fonctionnels :
    - Gestion des patients, hôpitaux et résultats via les services correspondants.
  - Simulation complète des flux API :
    - Recherche des hôpitaux par spécialité.
    - Attribution des patients à un hôpital.
    - Réservation et décrémentation des lits disponibles.

- **Refactorisation et standardisation** :
  - Adoption des conventions Spring pour une meilleure lisibilité et maintenabilité.
  - Utilisation des UUIDs pour garantir l'unicité globale des entités.
  - Gestion centralisée des exceptions avec un `ControllerAdvice`.

- **Tests complets** :
  - Tests unitaires et d'intégration couvrant les entités, services, et contrôleurs.
  - Couverture de code élevée confirmée avec Jacoco.

- **Documentation** :
  - Documentation complète de l'API avec **Swagger**.
  - Export des collections Postman pour simplifier les tests API.

### **Statut**

- **Stabilité** :
  - Le backend atteint une maturité suffisante pour être utilisé en production simulée.
  - Tous les tests passent avec succès, garantissant robustesse et conformité.

- **Prochaine étape** :
  - Intégration avec le frontend Angular pour une solution complète.

---

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

---

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

---

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

---

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

---

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

