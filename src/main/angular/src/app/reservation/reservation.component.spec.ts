import { Component } from '@angular/core';

@Component({
selector: 'app-reservation',
templateUrl: './reservation.component.html',
styleUrls: ['./reservation.component.scss'],
})
export class ReservationComponent {
qualite: string = 'Dr.';
nom: string = 'Frank Estein';
latitude: number = 48.8566; // Paris
longitude: number = 2.3522; // Paris

// Stocker la réponse de la réservation
reservationResponse: any = null;

// Générer un UUID
generateUUID(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      const r = (Math.random() * 16) | 0,
        v = c === 'x' ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    });
  }

  // Soumettre le formulaire
  submitForm(specialite: string): void {
    const reservationData = {
      id: this.generateUUID(),
      specialite: specialite,
      responsable: this.nom,
      qualite: this.qualite,
      latitude: this.latitude,
      longitude: this.longitude,
    };

    // Envoyer la requête POST au backend
    fetch('/api/reserve', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(reservationData),
    })
      .then((response) => response.json())
      .then((data) => {
        alert('Réservation réussie : ' + JSON.stringify(data));
      })
      .catch((error) => {
        console.error('Erreur lors de la réservation:', error);
        alert('Une erreur est survenue. Veuillez réessayer.');
      });
  }

  // Méthode pour fermer la popup
  closePopup(): void {
    this.reservationResponse = null;
  }
}
