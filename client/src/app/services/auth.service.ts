import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../model/user';
import { LoginRequest } from '../model/loginrequest';
import { LoginResponse } from '../model/login-response';

@Injectable({ providedIn: 'root' })
export class AuthService {
  public serverName = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.serverName}/api/auth/login`,
      loginRequest
    ).pipe(
      tap((response: LoginResponse) => {
        this.saveLoginData(response);
      })
    );
  }

  saveLoginData(response: LoginResponse): void {
    if (response) {
      const token = response.token || response.jwtToken;

      if (token) {
        this.setToken(token);
      }

      if (response.role) {
        this.setRole(response.role);
      }

      if (response.userId !== undefined && response.userId !== null) {
        this.setUserId(response.userId);
      }

      if (response.username) {
        this.setUsername(response.username);
      }
    }
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(
      `${this.serverName}/api/auth/register`,
      user
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(
      `${this.serverName}/api/auth/user`,
      { headers: this.getAuthHeaders() }
    );
  }

  getLoggedInUser(): Observable<User> {
    return this.getCurrentUser();
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  saveToken(token: string): void {
    this.setToken(token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  setRole(role: string): void {
    localStorage.setItem('role', role);
  }

  saveRole(role: string): void {
    this.setRole(role);
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  setUserId(userId: number): void {
    localStorage.setItem('userId', userId.toString());
  }

  getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? Number(userId) : null;
  }

  setUsername(username: string): void {
    localStorage.setItem('username', username);
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  }

  clearStorage(): void {
    this.logout();
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();

    if (token) {
      return new HttpHeaders({
        Authorization: `Bearer ${token}`
      });
    }

    return new HttpHeaders();
  }
}