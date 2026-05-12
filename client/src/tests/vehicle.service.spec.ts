import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpService } from '../services/http.service';
import { AuthService } from '../services/auth.service';
import { environment } from '../environments/environment';

describe('HttpService - Vehicle', () => {
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

  it('should get all vehicles', () => {
    service.getAllVehicles().subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles`);
    expect(req.request.method).toBe('GET');
    req.flush([{ vehicleId: 1, vehicleNumber: 'TN01AB1234' }]);
  });

  it('should add a vehicle', () => {
    service.addVehicle({ vehicleNumber: 'TN01AB1234' }).subscribe(data => expect(data.vehicleId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles`);
    expect(req.request.method).toBe('POST');
    req.flush({ vehicleId: 1, vehicleNumber: 'TN01AB1234' });
  });

  it('should update a vehicle', () => {
    service.updateVehicle(1, { vehicleNumber: 'TN01AB1234-U' }).subscribe(data => expect(data.vehicleNumber).toBe('TN01AB1234-U'));
    const req = httpMock.expectOne(`${base}/api/vehicles/1`);
    expect(req.request.method).toBe('PUT');
    req.flush({ vehicleId: 1, vehicleNumber: 'TN01AB1234-U' });
  });

  it('should delete a vehicle', () => {
    service.deleteVehicle(1).subscribe();
    const req = httpMock.expectOne(`${base}/api/vehicles/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should get vehicle by id', () => {
    service.getVehicleById(1).subscribe(data => expect(data.vehicleId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles/1`);
    expect(req.request.method).toBe('GET');
    req.flush({ vehicleId: 1 });
  });

  it('should search vehicle by number', () => {
    service.searchVehicleByNumber('TN01AB1234').subscribe(data => expect(data.vehicleNumber).toBe('TN01AB1234'));
    const req = httpMock.expectOne(`${base}/api/vehicles/search/number?vehicleNumber=TN01AB1234`);
    expect(req.request.method).toBe('GET');
    req.flush({ vehicleId: 1, vehicleNumber: 'TN01AB1234' });
  });

  it('should search vehicle by brand', () => {
    service.searchVehicleByBrand('Toyota').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles/search/brand?brand=Toyota`);
    expect(req.request.method).toBe('GET');
    req.flush([{ vehicleId: 1, brand: 'Toyota' }]);
  });

  it('should filter vehicle by status', () => {
    service.filterVehicleByStatus('Active').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles/filter/status?status=Active`);
    expect(req.request.method).toBe('GET');
    req.flush([{ vehicleId: 1, status: 'Active' }]);
  });

  it('should sort vehicles by year asc', () => {
    service.sortVehiclesByYear('asc').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles/sort/year?order=asc`);
    expect(req.request.method).toBe('GET');
    req.flush([{ vehicleId: 1 }]);
  });

  it('should sort vehicles by year desc', () => {
    service.sortVehiclesByYear('desc').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/vehicles/sort/year?order=desc`);
    expect(req.request.method).toBe('GET');
    req.flush([{ vehicleId: 1 }]);
  });

  it('should sort vehicles by mileage', () => {
    service.sortVehiclesByMileage('asc').subscribe();
    const req = httpMock.expectOne(`${base}/api/vehicles/sort/mileage?order=asc`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should assign driver to vehicle', () => {
    service.assignDriver(1, 2).subscribe();
    const req = httpMock.expectOne(`${base}/api/vehicles/1/assign-driver/2`);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });

  it('should include Authorization header for getAllVehicles', () => {
    service.getAllVehicles().subscribe();
    const req = httpMock.expectOne(`${base}/api/vehicles`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer mock-token');
    req.flush([]);
  });

  it('should include Content-Type header for addVehicle', () => {
    service.addVehicle({}).subscribe();
    const req = httpMock.expectOne(`${base}/api/vehicles`);
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush({});
  });

  it('should send correct body for updateVehicle', () => {
    const payload = { vehicleNumber: 'TN01', status: 'Active' };
    service.updateVehicle(1, payload).subscribe();
    const req = httpMock.expectOne(`${base}/api/vehicles/1`);
    expect(req.request.body).toEqual(payload);
    req.flush({});
  });
});
