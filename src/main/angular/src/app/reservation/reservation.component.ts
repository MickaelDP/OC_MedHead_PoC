import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Importation directe de specialities.json
import specialitiesData from '../../assets/specialities.json';

@Component({
selector: 'app-reservation',
standalone: true,
imports: [CommonModule, FormsModule],
templateUrl: './reservation.component.html',
styleUrls: ['./reservation.component.scss'],
})
export class ReservationComponent {
qualite: string = 'Dr.';
nom: string = 'Frank Estein';
latitude: number = 48.8566;
longitude: number = 2.3522;
specialite: string = '';                                    // Spécialité entrée par l'utilisateur
reservationResponse: any = null;                            // Réponse de l'API
specialities: string[] = [];                                // Liste des spécialités disponibles
filteredSpecialities: string[] = [];
showUrgencyPopup: boolean = false;                          // Indique si le popup d'urgence est affiché

constructor() {
    // Charger les spécialités au moment de l'initialisation
    this.loadSpecialities();
  }

  // Charger les spécialités à partir du fichier JSON importé
  loadSpecialities(): void {
    this.specialities = Object.keys(specialitiesData).filter((key) => key !== '');
  }

  // Gérer la saisie de spécialité
  onSpecialityInput(value: string): void {
    const inputValue = value.toLowerCase().trim();
    if (inputValue) {
      this.filteredSpecialities = this.specialities.filter((speciality) =>
        speciality.toLowerCase().startsWith(inputValue)
      );
    } else {
      this.filteredSpecialities = []; // Vider les suggestions si le champ est vide
    }
  }

  // Sélectionner une spécialité dans la liste des suggestions
  selectSpeciality(speciality: string): void {
    this.specialite = speciality;
    this.filteredSpecialities = [];
  }

  // Générer un UUID
  generateUUID(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
      const r = (Math.random() * 16) | 0;
      const v = c === 'x' ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    });
  }

  // Méthode pour soumettre le formulaire
  submitForm(): void {
    // Capitaliser la première lettre de la spécialité
    this.specialite = this.capitalizeFirstLetter(this.specialite);

    // Empêche la soumission si une réservation est en cours
    if (event) {
      event.preventDefault();
    }

    // Empêche la soumission si une réservation est en cours
    if (this.reservationResponse) {
      return;
    }

    if (!this.specialite || !this.specialities.includes(this.specialite)) {
      // Afficher le popup d'urgence pour spécialité inconnue
      this.showUrgencyPopup = true;
      return;
    }

    this.processReservation(this.specialite); // Réserver normalement si la spécialité est valide
  }

  // Processus de réservation
  processReservation(specialite: string): void {
    const reservationData = {
      id: this.generateUUID(),
      specialite: this.specialite,
      responsable: this.nom,
      qualite: this.qualite,
      latitude: this.latitude,
      longitude: this.longitude,
    };

    // Envoi des données avec fetch
    fetch('/api/patients/process', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(reservationData),
    })
      .then((response) => response.json())
      .then((data) => {
        this.reservationResponse = data;                      // Affiche la réponse dans la pop-up
      })
      .catch((error) => {
        console.error('Erreur lors de la réservation :', error);
        alert('Une erreur est survenue lors de la réservation.');
      });
  }

  // Fermer la pop-up et réinitialiser la page
  closePopup() {
    console.log('Popup fermée');
    this.reservationResponse = null;                            // Cacher la pop-up
    this.specialite = '';                                       // Réinitialiser le champ de spécialités
    this.filteredSpecialities = []; // Vider les suggestions
  }

  capitalizeFirstLetter(value: string): string {
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  // Réserver en urgence
  reserveInUrgency(): void {
  this.specialite = "Médecine d'urgence"; // Remplit automatiquement avec "Médecine d'urgence"
  this.showUrgencyPopup = false; // Ferme le popup

  // Soumettre les données de réservation
  const reservationData = {
    id: this.generateUUID(),
    specialite: this.specialite,
    responsable: this.nom,
    qualite: this.qualite,
    latitude: this.latitude,
    longitude: this.longitude,
  };

  // Envoi des données avec fetch
  fetch('/api/patients/process', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(reservationData),
  })
    .then((response) => response.json())
    .then((data) => {
      this.reservationResponse = data; // Affiche la réponse dans la pop-up
    })
    .catch((error) => {
      console.error('Erreur lors de la réservation :', error);
      alert('Une erreur est survenue lors de la réservation.');
    });
  }

  // Fermer le popup d'urgence
  closeUrgencyPopup(): void {
    console.log('Popup fermée');
    this.reservationResponse = null;                            // Cacher la pop-up
    this.specialite = '';                                       // Réinitialiser le champ de spécialités
    this.showUrgencyPopup = false;
    this.filteredSpecialities = []; // Vider les suggestions
  }
}
