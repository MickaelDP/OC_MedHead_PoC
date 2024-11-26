import { TestBed } from '@angular/core/testing';
import { ReservationComponent } from './reservation.component';

describe('ReservationComponent', () => {
  let component: ReservationComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReservationComponent],
    }).compileComponents();

    const fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

it('should process a valid reservation and pass correct data to fetch', () => {
  const fetchSpy = spyOn(window, 'fetch').and.returnValue(
    Promise.resolve(
      new Response(
        JSON.stringify({
          id: '1',
          hopitalNom: 'Hôpital A',
          delai: 10,
          specialiteDisponible: true,
        })
      )
    )
  );

component.specialite = 'Cardiologie';
component.specialities = ['Cardiologie'];
component.processReservation('Cardiologie');

  expect(fetchSpy).toHaveBeenCalledWith(
    '/api/patients/process',
    jasmine.objectContaining({
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: jasmine.any(String), // Vous pouvez vérifier ici les données JSON
    })
  );
});


  it('should not proceed if a reservation is already in progress', () => {
    // Simuler une réservation existante
    component.reservationResponse = [{ id: 1, specialite: 'Cardiologie' }];

    // Espionner `processReservation`
    const processReservationSpy = spyOn(component, 'processReservation');

    // Appeler `submitForm`
    component.submitForm();

    // Vérifier que `processReservation` n'est pas appelée
    expect(processReservationSpy).not.toHaveBeenCalled();
  });

  it('should reset the popup and clear the form on closePopup', () => {
    component.reservationResponse = {
      id: '1',
      specialite: 'Cardiologie',
    };
    component.specialite = 'Cardiologie';
    component.filteredSpecialities = ['Cardiologie'];

    component.closePopup();

    expect(component.reservationResponse).toBeNull(); // Vérifie que la réponse est null
    expect(component.specialite).toBe('');
    expect(component.filteredSpecialities).toEqual([]);
    expect(component.showUrgencyPopup).toBeFalse();
  });

  it('should not allow form submission if reservationResponse exists', () => {
    component.reservationResponse = [{ id: 1, specialite: 'Cardiologie' }];
    const processReservationSpy = spyOn(component, 'processReservation');
    component.submitForm();
    expect(processReservationSpy).not.toHaveBeenCalled(); // La méthode ne doit pas être appelée
  });

  it('should capitalize speciality correctly for complex cases', () => {
    component.specialite = 'médecine générale';
    component.specialities = ['Médecine générale', 'Chirurgie'];

    component.submitForm();
    expect(component.specialite).toEqual('Médecine générale');
  });

  it('should handle fetch error gracefully', async () => {
    const fetchSpy = spyOn(window, 'fetch').and.returnValue(
      Promise.reject(new Error('Network Error'))
    );

    component.specialite = 'Cardiologie';
    component.specialities = ['Cardiologie', 'Chirurgie'];
    await component.processReservation('Cardiologie');
    expect(fetchSpy).toHaveBeenCalled();
    expect(component.reservationResponse).toBeNull(); // Vérifie que la réponse reste null
  });


  it('should handle errors gracefully in reserveInUrgency', async () => {
    const fetchSpy = spyOn(window, 'fetch').and.returnValue(
      Promise.reject(new Error('Network Error'))
    );

    await component.reserveInUrgency();

    expect(fetchSpy).toHaveBeenCalled();
    expect(component.reservationResponse).toBeNull(); // Vérifie que la réponse reste null
  });


  it('should update filteredSpecialities on speciality input', () => {
    component.specialities = ['Cardiologie', 'Chirurgie'];
    component.onSpecialityInput('Car');
    expect(component.filteredSpecialities).toEqual(['Cardiologie']);
  });

  it('should clear filteredSpecialities when input is empty', () => {
    component.specialities = ['Cardiologie', 'Chirurgie'];
    component.onSpecialityInput('');
    expect(component.filteredSpecialities).toEqual([]);
  });

  it('should select a speciality and clear suggestions', () => {
    component.filteredSpecialities = ['Cardiologie'];
    component.selectSpeciality('Cardiologie');
    expect(component.specialite).toBe('Cardiologie');
    expect(component.filteredSpecialities).toEqual([]);
  });

  it('should set speciality to emergency and call processReservation', () => {
    const fetchSpy = spyOn(window, 'fetch').and.returnValue(
      Promise.resolve(new Response(JSON.stringify({ success: true })))
    );

    component.reserveInUrgency();
    expect(component.specialite).toBe("Médecine d'urgence");
    expect(fetchSpy).toHaveBeenCalled();
  });
});
