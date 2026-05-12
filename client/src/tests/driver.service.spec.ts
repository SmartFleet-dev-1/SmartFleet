import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpService } from '../services/http.service';
import { AuthService } from '../services/auth.service';
import { environment } from '../environments/environment';

describe('HttpService - Driver', () => {
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

  it('should get all drivers', () => {
    service.getAllDrivers().subscribe(data => expect(data.length).toBe(2));
    const req = httpMock.expectOne(`${base}/api/drivers`);
    expect(req.request.method).toBe('GET');
    req.flush([{ driverId: 1 }, { driverId: 2 }]);
  });

  it('should add a driver', () => {
    service.addDriver({ driverName: 'John' }).subscribe(data => expect(data.driverId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/drivers`);
    expect(req.request.method).toBe('POST');
    req.flush({ driverId: 1, driverName: 'John' });
  });

  it('should update a driver', () => {
    service.updateDriver(1, { driverName: 'Jane' }).subscribe(data => expect(data.driverName).toBe('Jane'));
    const req = httpMock.expectOne(`${base}/api/drivers/1`);
    expect(req.request.method).toBe('PUT');
    req.flush({ driverId: 1, driverName: 'Jane' });
  });

  it('should delete a driver', () => {
    service.deleteDriver(1).subscribe();
    const req = httpMock.expectOne(`${base}/api/drivers/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should get driver by id', () => {
    service.getDriverById(1).subscribe(data => expect(data.driverId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/drivers/1`);
    expect(req.request.method).toBe('GET');
    req.flush({ driverId: 1 });
  });

  it('should search driver by name', () => {
    service.searchDriverByName('John').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/drivers/search?name=John`);
    expect(req.request.method).toBe('GET');
    req.flush([{ driverId: 1, driverName: 'John' }]);
  });

  it('should filter driver by availability', () => {
    service.filterDriverByAvailability('Available').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/drivers/filter/availability?status=Available`);
    expect(req.request.method).toBe('GET');
    req.flush([{ driverId: 1, availabilityStatus: 'Available' }]);
  });

  it('should sort drivers by experience asc', () => {
    service.sortDriversByExperience('asc').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/drivers/sort/experience?order=asc`);
    expect(req.request.method).toBe('GET');
    req.flush([{ driverId: 1 }]);
  });

  it('should sort drivers by experience desc', () => {
    service.sortDriversByExperience('desc').subscribe();
    const req = httpMock.expectOne(`${base}/api/drivers/sort/experience?order=desc`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should include Authorization header for getAllDrivers', () => {
    service.getAllDrivers().subscribe();
    const req = httpMock.expectOne(`${base}/api/drivers`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer mock-token');
    req.flush([]);
  });

  it('should include Content-Type for addDriver', () => {
    service.addDriver({}).subscribe();
    const req = httpMock.expectOne(`${base}/api/drivers`);
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush({});
  });

  it('should send correct body for updateDriver', () => {
    const payload = { driverName: 'Tom', phoneNumber: '9876543210' };
    service.updateDriver(1, payload).subscribe();
    const req = httpMock.expectOne(`${base}/api/drivers/1`);
    expect(req.request.body).toEqual(payload);
    req.flush({});
  });

  it('should call deleteDriver with correct id', () => {
    service.deleteDriver(5).subscribe();
    const req = httpMock.expectOne(`${base}/api/drivers/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should filter driver by Assigned status', () => {
    service.filterDriverByAvailability('Assigned').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/drivers/filter/availability?status=Assigned`);
    expect(req.request.method).toBe('GET');
    req.flush([{ driverId: 2, availabilityStatus: 'Assigned' }]);
  });

  it('should search driver returns empty array when none found', () => {
    service.searchDriverByName('Unknown').subscribe(data => expect(data.length).toBe(0));
    const req = httpMock.expectOne(`${base}/api/drivers/search?name=Unknown`);
    req.flush([]);
  });
});
