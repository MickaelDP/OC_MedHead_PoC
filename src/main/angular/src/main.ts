import { bootstrapApplication } from '@angular/platform-browser';
import { ReservationComponent } from './app/reservation/reservation.component';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';

// Bootstrap de l'application standalone
bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes) // Fournit les routes définies à l'application
  ],
}).catch(err => console.error(err));

bootstrapApplication(ReservationComponent).catch(err => console.error(err));
