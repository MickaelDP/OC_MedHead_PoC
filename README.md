
# Système de réservation de lit d'hôpital en situation d'urgence

  

**Application web de PoC pour la réservation de lits d'hôpital avec fonctionnalités de localisation, gestion de proximité et gestion des disponibilités.**

  

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

- [Auteurs](#auteurs)

- [License](#license)

  

## Aperçu du Projet

  

Ce projet est un Proof of Concept (PoC) d'un système de réservation de lits d'hôpital, permettant de :

- Rechercher des hôpitaux avec des lits disponibles pour une spécialité donnée

- Détermination de l'hôpital le plus proche disponible

- Valider et annuler les réservations en temps réel avec gestion d'un minuteur

  

L'application utilise une architecture backend Spring Boot, une interface frontend Angular et intègre des tests unitaires et fonctionnels pour assurer le son fonctionnement de manière shift left.

  

## Structure du Projet

1110 à titre indicatif pour le moment:

- **src/main/java/com/project/hospital** : Contient le code backend en Java avec Spring Boot.

- **src/main/angular** : Contient le frontend de l'application développé avec Angular.

- **src/test/java** : Contient les tests unitaires et d'intégration.

- **pipeline** : Configurations du pipeline CI/CD avec Jenkins.

  

## Fonctionnalités

1. **Recherche de Service** : Recherche des hôpitaux offrant une spécialité spécifique.

2. **Réservation de Lit** : Sélection de l'hôpital le plus proche avec lit disponible et confirmation de réservation.

3. **Gestion de proximité** : Calcul automatique de la distance entre l'utilisateur et les hôpitaux pertinents.

5. **Gestion du temps de trajet** : Fournit une estimation du temps de trajet vers l'hôpital reservé.

  

## Prérequis

Avant de cloner et d'exécuter le projet en local, assurez-vous d'avoir installé :

- **Java 21** (ou compatible avec le backend Spring Boot)

- **Node.js et npm** (pour le frontend Angular)

- **JDK et Maven** (pour la gestion des dépendances backend)

- **Jenkins** (pour le pipeline CI/CD)

- **Git** (pour cloner le dépôt)

## Installation
### Cloner le Dépôt  
	```bash
	 git clone https://github.com/votre-utilisateur/hospital-bed-reservation-poc.git cd hospital-bed-reservation-poc

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
2. **Tests de Performance (ex. avec JMeter)** :
   - Configurer et exécuter les scénarios de performance avec des jeux de données générés.

3. **Exécution du Pipeline CI/CD** :
   - Utilisez Jenkins pour automatiser les tests et le déploiement continu.

## Déploiement

Voici les étapes pour déployer l'application en production (pour une application complète, ces étapes peuvent varier) :

1. **Pipeline CI/CD** :
   - Configuration dans Jenkins pour le déploiement continu. Voir le fichier Jenkinsfile dans le dépôt pour les détails.

2. **Hébergement** :
   - Utilisez un service d'hébergement compatible avec Spring Boot et Angular pour déployer l'application.

## Technologies Utilisées

- **Backend** : Java, Spring Boot
- **Frontend** : Angular
- **CI/CD** : Jenkins
- **Tests** : JUnit, Mockito pour les tests unitaires, JMeter pour les tests de performance

## Contribution

Merci de lire les fichiers suivants avant de contribuer :

- CONTRIBUTING.md
- CODE_OF_CONDUCT.md

## License

Ce projet est sous licence MIT. Voir le fichier[LICENSE](LICENSE) pour plus de détails.
