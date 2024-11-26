import { Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReservationComponent } from './reservation/reservation.component';

// Définir les routes
export const routes: Routes = [
{ path: '', redirectTo: 'reservation', pathMatch: 'full' },
{ path: 'reservation', component: ReservationComponent }
];

@NgModule({
imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
