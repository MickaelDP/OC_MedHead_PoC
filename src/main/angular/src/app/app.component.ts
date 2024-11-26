import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
selector: 'app-root',
standalone: true,
imports: [RouterModule], // Si vous utilisez des routes
template: `<router-outlet></router-outlet>` // Permet de charger les composants en fonction des routes
})
export class AppComponent {}
