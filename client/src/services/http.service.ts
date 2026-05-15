import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class HttpService {
  public serverName = environment.apiUrl;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();

    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  private getJsonHeaders(): HttpHeaders {
    const token = this.authService.getToken();

    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  // =========================
  // Vehicle APIs
  // =========================

  getAllVehicles(): Observable<any[]> {
    return this.http.get<any[]>(`${this.serverName}/api/vehicles`, {
      headers: this.getHeaders()
    });
  }

  addVehicle(vehicle: any): Observable<any> {
    return this.http.post<any>(`${this.serverName}/api/vehicles`, vehicle, {
      headers: this.getJsonHeaders()
    });
  }

  updateVehicle(vehicleId: number, vehicle: any): Observable<any> {
    return this.http.put<any>(`${this.serverName}/api/vehicles/${vehicleId}`, vehicle, {
      headers: this.getJsonHeaders()
    });
  }

  deleteVehicle(vehicleId: number): Observable<any> {
    return this.http.delete<any>(`${this.serverName}/api/vehicles/${vehicleId}`, {
      headers: this.getHeaders()
    });
  }

  getVehicleById(vehicleId: number): Observable<any> {
    return this.http.get<any>(`${this.serverName}/api/vehicles/${vehicleId}`, {
      headers: this.getHeaders()
    });
  }

  searchVehicleByNumber(vehicleNumber: string): Observable<any> {
    return this.http.get<any>(
      `${this.serverName}/api/vehicles/search/number?vehicleNumber=${vehicleNumber}`,
      { headers: this.getHeaders() }
    );
  }

  searchVehicleByBrand(brand: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/vehicles/search/brand?brand=${brand}`,
      { headers: this.getHeaders() }
    );
  }

  filterVehicleByStatus(status: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/vehicles/filter/status?status=${status}`,
      { headers: this.getHeaders() }
    );
  }

  sortVehiclesByYear(order: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/vehicles/sort/year?order=${order}`,
      { headers: this.getHeaders() }
    );
  }

  sortVehiclesByMileage(order: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/vehicles/sort/mileage?order=${order}`,
      { headers: this.getHeaders() }
    );
  }

  assignDriver(vehicleId: number, driverId: number): Observable<any> {
    return this.http.put<any>(
      `${this.serverName}/api/vehicles/${vehicleId}/assign-driver/${driverId}`,
      {},
      { headers: this.getJsonHeaders() }
    );
  }

  // =========================
  // Driver APIs
  // =========================

  getAllDrivers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.serverName}/api/drivers`, {
      headers: this.getHeaders()
    });
  }

  addDriver(driver: any): Observable<any> {
    return this.http.post<any>(`${this.serverName}/api/drivers`, driver, {
      headers: this.getJsonHeaders()
    });
  }

  updateDriver(driverId: number, driver: any): Observable<any> {
    return this.http.put<any>(`${this.serverName}/api/drivers/${driverId}`, driver, {
      headers: this.getJsonHeaders()
    });
  }

  deleteDriver(driverId: number): Observable<any> {
    return this.http.delete<any>(`${this.serverName}/api/drivers/${driverId}`, {
      headers: this.getHeaders()
    });
  }

  getDriverById(driverId: number): Observable<any> {
    return this.http.get<any>(`${this.serverName}/api/drivers/${driverId}`, {
      headers: this.getHeaders()
    });
  }

  searchDriverByName(name: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/drivers/search?name=${name}`,
      { headers: this.getHeaders() }
    );
  }

filterDriverByAvailability(status: string): Observable<any[]> {
  return this.http.get<any[]>(
    `${this.serverName}/api/drivers/filter/availability?status=${status}`,
    { headers: this.getHeaders() }
  );
}

  sortDriversByExperience(order: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/drivers/sort/experience?order=${order}`,
      { headers: this.getHeaders() }
    );
  }

  // =========================
  // Maintenance APIs
  // =========================

  getAllMaintenance(): Observable<any[]> {
    return this.http.get<any[]>(`${this.serverName}/api/maintenance`, {
      headers: this.getHeaders()
    });
  }

  addMaintenance(maintenance: any, vehicleId: number): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/maintenance?vehicleId=${vehicleId}`,
      maintenance,
      { headers: this.getJsonHeaders() }
    );
  }

  updateMaintenance(maintenanceId: number, maintenance: any, vehicleId: number): Observable<any> {
    return this.http.put<any>(
      `${this.serverName}/api/maintenance/${maintenanceId}?vehicleId=${vehicleId}`,
      maintenance,
      { headers: this.getJsonHeaders() }
    );
  }

  deleteMaintenance(maintenanceId: number): Observable<any> {
    return this.http.delete<any>(`${this.serverName}/api/maintenance/${maintenanceId}`, {
      headers: this.getHeaders()
    });
  }

  getMaintenanceById(maintenanceId: number): Observable<any> {
    return this.http.get<any>(`${this.serverName}/api/maintenance/${maintenanceId}`, {
      headers: this.getHeaders()
    });
  }

  searchMaintenanceByType(serviceType: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/maintenance/search?serviceType=${serviceType}`,
      { headers: this.getHeaders() }
    );
  }

  filterMaintenanceByDate(date: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/maintenance/filter/date?date=${date}`,
      { headers: this.getHeaders() }
    );
  }

  sortMaintenanceByCost(order: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/maintenance/sort/cost?order=${order}`,
      { headers: this.getHeaders() }
    );
  }

  getMaintenanceByVehicle(vehicleId: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/maintenance/vehicle/${vehicleId}`,
      { headers: this.getHeaders() }
    );
  }

  // =========================
  // Insurance APIs
  // =========================

  getAllInsurance(): Observable<any[]> {
    return this.http.get<any[]>(`${this.serverName}/api/insurance`, {
      headers: this.getHeaders()
    });
  }

  addInsurance(insurance: any, vehicleId: number): Observable<any> {
    return this.http.post<any>(
      `${this.serverName}/api/insurance?vehicleId=${vehicleId}`,
      insurance,
      { headers: this.getJsonHeaders() }
    );
  }

  updateInsurance(insuranceId: number, insurance: any, vehicleId: number): Observable<any> {
    return this.http.put<any>(
      `${this.serverName}/api/insurance/${insuranceId}?vehicleId=${vehicleId}`,
      insurance,
      { headers: this.getJsonHeaders() }
    );
  }

  deleteInsurance(insuranceId: number): Observable<any> {
    return this.http.delete<any>(`${this.serverName}/api/insurance/${insuranceId}`, {
      headers: this.getHeaders()
    });
  }

  getInsuranceById(insuranceId: number): Observable<any> {
    return this.http.get<any>(`${this.serverName}/api/insurance/${insuranceId}`, {
      headers: this.getHeaders()
    });
  }

  searchInsuranceByProvider(providerName: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/insurance/search?providerName=${providerName}`,
      { headers: this.getHeaders() }
    );
  }

  filterInsuranceByCoverage(coverageType: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/insurance/filter/coverage?coverageType=${coverageType}`,
      { headers: this.getHeaders() }
    );
  }

  sortInsuranceByPremium(order: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.serverName}/api/insurance/sort/premium?order=${order}`,
      { headers: this.getHeaders() }
    );
  }
}