import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpService } from '../services/http.service';
import { AuthService } from '../services/auth.service';
import { environment } from '../environments/environment';

describe('HttpService - Insurance', () => {
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

  it('should get all insurance records', () => {
    service.getAllInsurance().subscribe(data => expect(data.length).toBe(2));
    const req = httpMock.expectOne(`${base}/api/insurance`);
    expect(req.request.method).toBe('GET');
    req.flush([{ insuranceId: 1 }, { insuranceId: 2 }]);
  });

  it('should add insurance with vehicleId param', () => {
    service.addInsurance({ policyNumber: 'POL001' }, 1).subscribe(data => expect(data.insuranceId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/insurance?vehicleId=1`);
    expect(req.request.method).toBe('POST');
    req.flush({ insuranceId: 1, policyNumber: 'POL001' });
  });

  it('should update insurance', () => {
    service.updateInsurance(1, { providerName: 'HDFC' }, 1).subscribe(data => expect(data.providerName).toBe('HDFC'));
    const req = httpMock.expectOne(`${base}/api/insurance/1?vehicleId=1`);
    expect(req.request.method).toBe('PUT');
    req.flush({ insuranceId: 1, providerName: 'HDFC' });
  });

  it('should delete insurance', () => {
    service.deleteInsurance(1).subscribe();
    const req = httpMock.expectOne(`${base}/api/insurance/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should get insurance by id', () => {
    service.getInsuranceById(1).subscribe(data => expect(data.insuranceId).toBe(1));
    const req = httpMock.expectOne(`${base}/api/insurance/1`);
    expect(req.request.method).toBe('GET');
    req.flush({ insuranceId: 1 });
  });

  it('should search insurance by provider name', () => {
    service.searchInsuranceByProvider('HDFC').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/insurance/search?providerName=HDFC`);
    expect(req.request.method).toBe('GET');
    req.flush([{ insuranceId: 1, providerName: 'HDFC Ergo' }]);
  });

  it('should filter insurance by coverage type Full', () => {
    service.filterInsuranceByCoverage('Full').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/insurance/filter/coverage?coverageType=Full`);
    expect(req.request.method).toBe('GET');
    req.flush([{ insuranceId: 1, coverageType: 'Full' }]);
  });

  it('should filter insurance by coverage type Third-party', () => {
    service.filterInsuranceByCoverage('Third-party').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/insurance/filter/coverage?coverageType=Third-party`);
    expect(req.request.method).toBe('GET');
    req.flush([{ insuranceId: 2, coverageType: 'Third-party' }]);
  });

  it('should sort insurance by premium asc', () => {
    service.sortInsuranceByPremium('asc').subscribe(data => expect(data.length).toBe(1));
    const req = httpMock.expectOne(`${base}/api/insurance/sort/premium?order=asc`);
    expect(req.request.method).toBe('GET');
    req.flush([{ insuranceId: 1, premiumAmount: 5000 }]);
  });

  it('should sort insurance by premium desc', () => {
    service.sortInsuranceByPremium('desc').subscribe();
    const req = httpMock.expectOne(`${base}/api/insurance/sort/premium?order=desc`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should include Authorization header for getAllInsurance', () => {
    service.getAllInsurance().subscribe();
    const req = httpMock.expectOne(`${base}/api/insurance`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer mock-token');
    req.flush([]);
  });

  it('should include Content-Type for addInsurance', () => {
    service.addInsurance({}, 1).subscribe();
    const req = httpMock.expectOne(`${base}/api/insurance?vehicleId=1`);
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush({});
  });

  it('should send correct body for updateInsurance', () => {
    const payload = { providerName: 'LIC', premiumAmount: 15000 };
    service.updateInsurance(2, payload, 1).subscribe();
    const req = httpMock.expectOne(`${base}/api/insurance/2?vehicleId=1`);
    expect(req.request.body).toEqual(payload);
    req.flush({});
  });

  it('should call deleteInsurance with correct id', () => {
    service.deleteInsurance(4).subscribe();
    const req = httpMock.expectOne(`${base}/api/insurance/4`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should return empty array when search finds nothing', () => {
    service.searchInsuranceByProvider('Unknown').subscribe(data => expect(data.length).toBe(0));
    const req = httpMock.expectOne(`${base}/api/insurance/search?providerName=Unknown`);
    req.flush([]);
  });
});
