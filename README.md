# Application Web de PoC pour la Réservation de Lits d'Hôpital
![Couverture du projet](assets/cover.png)
**Proof of Concept (PoC) pour la gestion de réservations de lits hospitaliers, intégrant des fonctionnalités de localisation, calcul de proximité, et gestion des disponibilités.**

---

## Table des Matières

- [Aperçu du Projet](#aperçu-du-projet)
- [Structure du Projet](#structure-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Exécution des Tests](#exécution-des-tests)
- [Déploiement](#déploiement)
- [Technologies Utilisées](#technologies-utilisées)
- [Contribution](#contribution)
- [License](#license)

---

## Aperçu du Projet

Ce projet est un Proof of Concept (PoC) visant à démontrer la faisabilité d'un système de réservation de lits hospitaliers avec les fonctionnalités suivantes :

- **Recherche de lits disponibles** pour une spécialité donnée.
- **Tri des hôpitaux** par proximité ou autres critères.
- **Validation des réservations** avec gestion en temps réel.
- **Simulation de trajets et gestion de la distance** entre patients et hôpitaux.

L'architecture du backend est basée sur Spring Boot. Les tests unitaires et d'intégration sont réalisés avec JUnit et Mockito. **(Le frontend Angular est indiqué à titre indicatif et n'est pas encore implémenté dans cette version.)**

---

## Structure du Projet

### Backend
- **`src/main/java/com/medHead/poc`** :
    - `controller` : Contrôleurs REST (Patient, Result).
    - `services` : Services métier pour gérer les patients, hôpitaux, et résultats.
    - `entity` : Entités principales utilisées dans le projet (`Patient`, `Hopital`, `Result`).
    - `toMock` : Services simulant des fonctionnalités externes comme la réservation et le calcul de distance.
    - `runner` : Simulations pour tester le flux global via un `CommandLineRunner`.

### Tests
- **`src/test/java/com/medHead/poc`** :
    - `testUnitaires` : Tests unitaires avec JUnit 5.
    - `testIntegration` : Tests d'intégration pour valider les flux API REST.

### **Frontend** *(à titre indicatif)*
- **`src/main/angular`** : Structure du frontend Angular prévue pour les futures versions.

---

## Fonctionnalités

1. **Recherche de Service** :
    - Permet de lister les hôpitaux offrant une spécialité spécifique.
2. **Réservation de Lit** :
    - Sélectionne l'hôpital le plus proche avec un lit disponible et confirme la réservation.
3. **Gestion de Proximité** :
    - Calcule automatiquement la distance et le temps de trajet vers les hôpitaux pertinents.
4. **Simulation de Résultat** :
    - Génère des résultats pour suivre les interactions (patient, hôpital, lit réservé, etc.).

---

## Prérequis

Avant de cloner et exécuter le projet, assurez-vous d'avoir installé :

- **Java 21** (compatible avec Spring Boot).
- **Maven** (pour la gestion des dépendances backend).
- **Git** (pour cloner le dépôt).

*(Les outils pour le frontend, comme Node.js, npm, et Angular CLI, sont indiqués à titre indicatif pour les futures versions.)*

---

## Installation

### Cloner le Dépôt
    ```bash
    git clone https://github.com/votre-utilisateur/medHead-hospital-reservation-poc.git
    cd medHead-hospital-reservation-poc

### Backend : Spring Boot

1. Configurer les dépendances :
  - Utiliser Maven pour installer les dépendances :
	  ```bash
	   mvn clean install
2. Lancer l'application :
	```bash
   mvn spring-boot:run
### Frontend : Angular
1. Installer les dépendances Angular :
	  ```bash
	cd src/main/angular
	npm instal
2. Lancer le serveur de développement Angular :
   ```bash
   ng serve
### Base de Données 

Pour cette PoC, il est possible d'utiliser des données en mémoire pour simuler les hôpitaux et les réservations.

## Exécution des Tests

1. **Tests Unitaires et Mocking avec JUnit et Mockito** :
   - Exécuter les tests :
   ```bash
   mvn test
2. **Couverture des Tests avec Jacoco :**
    - Lancer les tests avec génération du rapport de couverture :
      ```bash
      mvn clean verify
   - Lancer les tests avec génération du rapport de couverture :
      ```bash
      target/site/jacoco/index.html
   ![Couverture du projet](assets/jacoco94.png)
3. **Tests de Performance (ex. avec JMeter)** :
   - Configurer et exécuter les scénarios de performance avec des jeux de données générés.

4. **Exécution du Pipeline CI/CD** :
   - Utilisez Jenkins pour automatiser les tests et le déploiement continu.

## Déploiement

Voici les étapes pour déployer l'application en production (pour une application complète, ces étapes peuvent varier) :

1. **Pipeline CI/CD** :
   - Configuration dans Jenkins pour le déploiement continu. Voir le fichier Jenkinsfile dans le dépôt pour les détails.

## Technologies Utilisées

- **Outils de développement:** Maven, Git.
- **Backend** : Java 21, Spring Boot.
- **Frontend** : Angular.
- **CI/CD** : Jenkins.
- **Tests** : JUnit 5, Mockito pour les tests unitaires et d'intégrations, Jacoco pour la couverture, JMeter pour les tests de performance.

## Contribution

Merci de lire les fichiers suivants avant de contribuer :
- [Changelog.md](Changelog.md)
- [CONTRIBUTING.md](CONTRIBUTING.md)
- [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)

## License

Ce projet est sous licence MIT. Voir le fichier[LICENSE](LICENSE) pour plus de détails.
