import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { ReservationComponent } from './reservation/reservation.component';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: '', redirectTo: 'reservation', pathMatch: 'full' },
          { path: 'reservation', component: ReservationComponent },
          { path: 'home', component: AppComponent },
          { path: '**', redirectTo: '/home' }, // Route catch-all
        ]),
        AppComponent, // AppComponent est standalone
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'angular-frontend'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('angular-frontend');
  });

  it('should render the router outlet', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).not.toBeNull();
  });

  it('should navigate to reservation route', async () => {
    const navigateSpy = spyOn(router, 'navigate');
    await router.navigate(['/reservation']);
    expect(navigateSpy).toHaveBeenCalledWith(['/reservation']);
  });

  it('should navigate to reservation and then home', async () => {
    await router.navigate(['/reservation']);
    expect(router.url).toBe('/reservation');
    await router.navigate(['/home']);
    expect(router.url).toBe('/home');
  });

  it('should redirect empty path to /reservation', async () => {
    const fixture = TestBed.createComponent(AppComponent);
    await router.navigate(['']); // Naviguer vers le chemin vide
    expect(router.url).toBe('/reservation');
  });

  it('should redirect unknown routes to /home', async () => {
    await router.navigate(['/unknown-route']);
    expect(router.url).toBe('/home');
  });

  it('should navigate between routes', async () => {
    await router.navigate(['/reservation']);
    expect(router.url).toBe('/reservation');

    await router.navigate(['/home']);
    expect(router.url).toBe('/home');
  });

  it('should display ReservationComponent for /reservation route', async () => {
    const fixture = TestBed.createComponent(AppComponent);
    await router.navigate(['/reservation']);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('app-reservation')).not.toBeNull();
  });

});
