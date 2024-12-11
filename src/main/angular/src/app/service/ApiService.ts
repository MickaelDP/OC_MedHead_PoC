import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private readonly apiUrl = 'https://localhost:8443';

  constructor(private http: HttpClient) {}

  /**
   * Récupérer un token CSRF.
   */
  getCsrfToken(): Observable<string> {
    return this.http.get(`${this.apiUrl}/test/csrf`, { responseType: 'text' });
  }

  /**
   * Effectuer une requête POST avec JWT et CSRF.
   */
  postWithTokens(endpoint: string, payload: any, jwtToken: string, csrfToken: string): Observable<any> {
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${jwtToken}`,
      'X-XSRF-TOKEN': csrfToken, // Utilise le token brut ici
    };

    return this.http.post(`${this.apiUrl}${endpoint}`, payload, { headers });
  }
}
