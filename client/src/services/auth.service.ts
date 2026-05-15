

import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

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

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  }

  clearStorage(): void {
    this.logout();
  }
}