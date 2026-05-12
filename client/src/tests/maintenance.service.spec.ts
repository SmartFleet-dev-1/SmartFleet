import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpService } from '../services/http.service';
import { AuthService } from '../services/auth.service';
import { environment } from '../environments/environment';

describe('HttpService - Maintenance', () => {
  let service: HttpService;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  const base = environment.apiUrl;

  beforeEach(() => {
    const authSpy = jasmine.createSpyObj('AuthService', ['getToken']);
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HttpService, { provide: AuthService, useValue: authSpy }]
    });
    service = TestBed.inject(HttpService);
    httpMock = TestBed.inject(HttpTestingController);
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    authServiceSpy.getToken.and.returnValue('mock-token');
  });

  afterEach(() => httpMock.verify());

  it('should get all maintenance records', () => {
    service.getAllMaintenance().subscribe(data => expect(data.length).toBe(2));
    const req = httpMock.expectOne(`${base}/api/maintenance`);
    expect(req.request.method).toBe('GET');
    req.flush([{ maintenanceId: 1 }, { maintenanceId: 2 }]);
  });

  it('should add a maintenance record with vehicleId param', () => {
    service.addMaintenance({ serviceType: 'Oil Change' }, 1).subscribe(data => expect(data.maintenanceId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/maintenance?vehicleId=1`);
    expect(req.request.method).toBe('POST');
    req.flush({ maintenanceId: 1, serviceType: 'Oil Change' });
  });

  it('should update a maintenance record', () => {
    service.updateMaintenance(1, { serviceType: 'Engine Repair' }, 1).subscribe(data => expect(data.serviceType).toBe('Engine Repair'));
    const req = httpMock.expectOne(`${base}/api/maintenance/1?vehicleId=1`);
    expect(req.request.method).toBe('PUT');
    req.flush({ maintenanceId: 1, serviceType: 'Engine Repair' });
  });

  it('should delete a maintenance record', () => {
    service.deleteMaintenance(1).subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should get maintenance by id', () => {
    service.getMaintenanceById(1).subscribe(data => expect(data.maintenanceId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/maintenance/1`);
    expect(req.request.method).toBe('GET');
    req.flush({ maintenanceId: 1 });
  });

  it('should search maintenance by service type', () => {
    service.searchMaintenanceByType('Oil').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/maintenance/search?serviceType=Oil`);
    expect(req.request.method).toBe('GET');
    req.flush([{ maintenanceId: 1, serviceType: 'Oil Change' }]);
  });

  it('should filter maintenance by date', () => {
    service.filterMaintenanceByDate('2026-01-01').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/maintenance/filter/date?date=2026-01-01`);
    expect(req.request.method).toBe('GET');
    req.flush([{ maintenanceId: 1 }]);
  });

  it('should sort maintenance by cost asc', () => {
    service.sortMaintenanceByCost('asc').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/maintenance/sort/cost?order=asc`);
    expect(req.request.method).toBe('GET');
    req.flush([{ maintenanceId: 1, serviceCost: 1000 }]);
  });

  it('should sort maintenance by cost desc', () => {
    service.sortMaintenanceByCost('desc').subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance/sort/cost?order=desc`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should include Authorization header for getAllMaintenance', () => {
    service.getAllMaintenance().subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer mock-token');
    req.flush([]);
  });

  it('should include Content-Type for addMaintenance', () => {
    service.addMaintenance({}, 1).subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance?vehicleId=1`);
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush({});
  });

  it('should send correct body for updateMaintenance', () => {
    const payload = { serviceType: 'Tyre Change', serviceCost: 3000 };
    service.updateMaintenance(2, payload, 1).subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance/2?vehicleId=1`);
    expect(req.request.body).toEqual(payload);
    req.flush({});
  });

  it('should call deleteMaintenance with correct id', () => {
    service.deleteMaintenance(3).subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance/3`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should search maintenance returns empty array when none found', () => {
    service.searchMaintenanceByType('Unknown').subscribe(data => expect(data.length).toBe(0));
    const req = httpMock.expectOne(`${base}/api/maintenance/search?serviceType=Unknown`);
    req.flush([]);
  });

  it('should call addMaintenance with correct vehicleId in URL', () => {
    service.addMaintenance({}, 5).subscribe();
    const req = httpMock.expectOne(`${base}/api/maintenance?vehicleId=5`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });
});
